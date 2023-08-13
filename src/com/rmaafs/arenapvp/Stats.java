package com.rmaafs.arenapvp;

import java.util.HashMap;
import java.util.Map;

import static com.rmaafs.arenapvp.Extra.cstats;
import static com.rmaafs.arenapvp.Extra.kits;
import static com.rmaafs.arenapvp.Main.extraLang;

import org.bukkit.entity.Player;

public class Stats {

    public HashMap<Kit, Integer> elo = new HashMap<>();
    public HashMap<Kit, Integer> played = new HashMap<>();
    public HashMap<Kit, Integer> wins = new HashMap<>();
    public HashMap<Kit, Rangos> rango = new HashMap<>();

    public int rankeds = 0;
    public int unrankeds = 0;

    public Player player;
    public String playerName;
    public String statsString = "";

    public Stats(Player pp) {
        player = pp;
        playerName = player.getName();
        statsString = SQL.getStats(player);
        descomponer();
        reloadRankedsAndUnrankeds();
    }

    public void reloadRankedsAndUnrankeds() {
        if (Extra.regenRankedsUnrankeds && Extra.addPlayerNextDay(player)) {
            if (rankeds < extraLang.defaultRankeds) {
                rankeds = extraLang.defaultRankeds;
            }

            if (unrankeds < extraLang.defaultUnRankeds) {
                unrankeds = extraLang.defaultUnRankeds;
            }
            for (int i = 100; i > 1; i--) {
                if (player.hasPermission("apvp.rankeds." + i)) {
                    if (rankeds < i) {
                        rankeds = i;
                    }
                    break;
                }
            }
            for (int i = 100; i > 1; i--) {
                if (player.hasPermission("apvp.unrankeds." + i)) {
                    if (unrankeds < i) {
                        unrankeds = i;
                    }
                    break;
                }
            }
        }
    }

    public void removeKit(Kit k) {
        elo.remove(k);
        played.remove(k);
        wins.remove(k);
        componer();
    }

    //BuildUHC,played,wins,elo@
    public void descomponer() {
        String[] m = statsString.split("@");
        for (String m1 : m) {
            String[] l = m1.split(",");
            if (l.length == 4) {
                Kit k = kits.get(l[0]);
                played.put(k, Integer.valueOf(l[1]));
                wins.put(k, Integer.valueOf(l[2]));
                elo.put(k, Integer.valueOf(l[3]));
                rango.put(k, Extra.setRank(elo.get(k)));
            } else if (l.length == 3) {
                playerName = l[0];
                rankeds = Integer.parseInt(l[1]);
                unrankeds = Integer.parseInt(l[2]);
            }
        }
    }

    public void componer() {
        String total = "";
        for (Map.Entry<String, Kit> entry : kits.entrySet()) {
            String name = entry.getKey();
            Kit k = entry.getValue();
            if (kits.containsKey(k.kitName) && elo.containsKey(k)) {
                total = total + name + ",";
                total = total + getPlayed(k) + ",";
                total = total + getWins(k) + ",";
                total = total + getElo(k) + "@";
            }
        }
        if (rankeds != extraLang.defaultRankeds || unrankeds != extraLang.defaultUnRankeds) {
            total = total + playerName + "," + rankeds + "," + unrankeds;
        }
        if (!total.equals("")) {
            statsString = total;
            cstats.set(player.getUniqueId().toString() + ".o", total);
            Extra.save(Extra.stats, cstats);
        }
    }

    public Integer getElo(Kit k) {
        if (elo.containsKey(k)) {
            return elo.get(k);
        }
        return extraLang.defaultElo;
    }

    public Integer getWins(Kit k) {
        if (wins.containsKey(k)) {
            return wins.get(k);
        }
        return 0;
    }

    public Integer getPlayed(Kit k) {
        if (played.containsKey(k)) {
            return played.get(k);
        }
        return 0;
    }

    public String getRank(Kit k) {
        if (rango.containsKey(k)) {
            return rango.get(k).getName();
        }
        return Extra.rangoDefault.getName();
    }

    public Integer getRankeds() {
        return rankeds;
    }

    public Integer getUnRankeds() {
        return unrankeds;
    }

    public void setElo(Kit k, int i) {
        elo.put(k, i);
        rango.put(k, Extra.setRank(i));
    }

    public void setPlayed(Kit k, int i) {
        played.put(k, i);
    }

    public void setWins(Kit k, int i) {
        wins.put(k, i);
    }

    public void setRankeds(int i) {
        rankeds = i;
    }

    public void setUnRankeds(int i) {
        unrankeds = i;
    }
}
