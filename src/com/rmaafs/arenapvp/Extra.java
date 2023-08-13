package com.rmaafs.arenapvp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.rmaafs.arenapvp.GUIS.GuiEvent;
import com.rmaafs.arenapvp.KitControl.CrearKitEvent;
import com.rmaafs.arenapvp.MapControl.CrearMapaEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import static java.util.logging.Level.SEVERE;

public class Extra {

    public static File config;
    public static File lang;
    public static File spawns;
    public static File stats;
    public static File scoreboards;
    public static File ranks;
    public static File cache;
    public static FileConfiguration cconfig;
    public static FileConfiguration clang;
    public static FileConfiguration cspawns;
    public static FileConfiguration cstats;
    public static FileConfiguration cscoreboards;
    public static FileConfiguration cranks;
    public static FileConfiguration ccache;

    public static HashMap<Player, PlayerConfig> playerConfig = new HashMap<>();
    public static HashMap<String, Kit> kits = new HashMap<>();
    public static HashMap<Kit, List<Mapa>> mapLibres = new HashMap<>();
    public static HashMap<Kit, List<Mapa>> mapOcupadas = new HashMap<>();
    public static HashMap<Kit, List<MapaMeetup>> mapMeetupLibres = new HashMap<>();
    public static HashMap<Kit, List<MapaMeetup>> mapMeetupOcupadas = new HashMap<>();
<<<<<<< Updated upstream

    public static HashMap<Player, Partida> jugandoUno = new HashMap<>();

    public static List<Partida> preEmpezandoUno = new ArrayList<>();

    public static HashMap<Player, Score> scores = new HashMap<>();
=======
    public static HashMap<Player, Game> jugandoUno = new HashMap<>();
    public static HashMap<Player, Score> scores = new HashMap<>();

    public static List<Game> preEmpezandoUno = new ArrayList<>();
>>>>>>> Stashed changes
    public static List<Rangos> rangos = new ArrayList<>();

    public static Rangos rangoDefault;
    public static String nopermission;
    public static boolean regenRankedsUnrankeds = false;

    public static void setFiles() {
        config = Main.plugin.getcConfig();
        cconfig = YamlConfiguration.loadConfiguration(config);
        lang = Main.plugin.getcLang();
        clang = YamlConfiguration.loadConfiguration(lang);
        spawns = Main.plugin.getcSpawns();
        cspawns = YamlConfiguration.loadConfiguration(spawns);
        stats = Main.plugin.getcSstats();
        cstats = YamlConfiguration.loadConfiguration(stats);
        scoreboards = Main.plugin.getcScoreboards();
        cscoreboards = YamlConfiguration.loadConfiguration(scoreboards);
        ranks = Main.plugin.getcRanks();
        cranks = YamlConfiguration.loadConfiguration(ranks);
        cache = Main.plugin.getcCache();
        ccache = YamlConfiguration.loadConfiguration(cache);
        nopermission = tc(clang.getString("nopermission"));
        for (String path : cranks.getConfigurationSection("").getKeys(false)) {
            if (cranks.contains(path + ".name") && cranks.contains(path + ".elo")) {
                rangos.add(new Rangos(tc(cranks.getString(path + ".name")), cranks.getInt(path + ".elo")));
            }
        }
        detectNextDay();
    }

    public static Rangos setRank(int elo) {
        for (Rangos r : rangos) {
            if (elo >= r.getElo()) {
                return r;
            }
        }
        return rangos.get(0);
    }

    public static String getKitVariable(String m, String type) {
        String kit = "";
        boolean inicio = false;
        for (int i = 0; i < m.length(); i++) {
            if (!inicio) {
                if (m.charAt(i) == '<' && i + 1 < m.length() && m.charAt(i + 1) == type.charAt(0)) {
                    inicio = true;
                    kit = kit + m.charAt(i);
                }
            } else {
                if (m.charAt(i) == '>') {
                    break;
                } else {
                    kit = kit + m.charAt(i);
                }
            }
        }
        kit = kit.replaceAll("<" + type + ":", "");
        if (kits.containsKey(kit)) {
            return kit;
        }
        return "Unknown Kit";
    }

    public static void detectNextDay() {
        String s = new SimpleDateFormat("dd").format(new Date());
        if (ccache.contains("day")) {
            if (!ccache.getString("day").equals(s)) {
                regenRankedsUnrankeds = true;
                ccache.set("day", s);
                ccache.set("players", null);
                ccache.set("gift", null);
                save(cache, ccache);
            }
        } else {
            regenRankedsUnrankeds = true;
            ccache.set("day", s);
            ccache.set("players", null);
            ccache.set("gift", null);
            save(cache, ccache);
        }
    }

    public static boolean addPlayerNextDay(Player p) {
        List<String> players = new ArrayList<>();
        if (ccache.contains("players")) {
            players = ccache.getStringList("players");
            if (players.contains(p.getName())) {
                return false;
            } else {
                players.add(p.getName());
                ccache.set("players", players);
                save(cache, ccache);
            }
        } else {
            players.add(p.getName());
            ccache.set("players", players);
            save(cache, ccache);
        }
        return true;
    }

    public static boolean noHaDadoRankeds(Player p) {
        List<String> players = new ArrayList<>();
        if (ccache.contains("gift")) {
            players = ccache.getStringList("gift");
            if (players.contains(p.getName())) {
                return false;
            } else {
                players.add(p.getName());
                ccache.set("gift", players);
                save(cache, ccache);
            }
        } else {
            players.add(p.getName());
            ccache.set("gift", players);
            save(cache, ccache);
        }
        return true;
    }

    public static void resetRankedsUnRankeds() {
        regenRankedsUnrankeds = true;
        ccache.set("day", new SimpleDateFormat("dd").format(new Date()));
        ccache.set("players", null);
        ccache.set("gift", null);
        save(cache, ccache);
    }

    public static boolean isExist(Player checar, Player online) {
        if (checar != null && checar.isOnline()) {
            return true;
        }
        online.sendMessage(Main.extraLang.playerOffline);
        return false;
    }

    public static boolean isPlaying(Player p) {
        return !jugandoUno.containsKey(p)
                && !Main.duelControl.esperandoRanked.containsValue(p)
                && !Main.duelControl.esperandoUnRanked.containsValue(p)
                && !CrearKitEvent.creatingKit.containsKey(p)
                && !CrearKitEvent.editingKit.containsKey(p)
                && !CrearKitEvent.waitingEditKit.contains(p)
                && !GuiEvent.esperandoEliminarKit.contains(p)
                && !CrearMapaEvent.creandoMapa.containsKey(p)
                && !Main.meetupControl.meetupsPlaying.containsKey(p)
                && !Main.meetupControl.creandoEventoMeetup.containsKey(p)
                && !Main.meetupControl.creandoMapaMeetup.containsKey(p)
                && !Main.meetupControl.esperandoCrearEvento.contains(p)
                && !Main.meetupControl.esperandoMapaMeetup.contains(p)
                && !Main.hotbars.editingSlotHotbar.containsKey(p)
                && !Main.hotbars.editingSlotHotbar.containsKey(p)
                && !Main.hotbars.esperandoEscojaHotbar.contains(p)
                && !Main.partyControl.partys.containsKey(p)
                && Main.extraLang.worlds.contains(p.getWorld().getName())
                && !Main.specControl.mirando.containsKey(p);
    }

    public static boolean isCheckPlayerPlaying(Player p, Player mensaje) {
        if (jugandoUno.containsKey(p)
                || Main.duelControl.esperandoRanked.containsValue(p)
                || Main.duelControl.esperandoUnRanked.containsValue(p)
                || CrearKitEvent.creatingKit.containsKey(p)
                || CrearKitEvent.editingKit.containsKey(p)
                || CrearKitEvent.waitingEditKit.contains(p)
                || GuiEvent.esperandoEliminarKit.contains(p)
                || CrearMapaEvent.creandoMapa.containsKey(p)
                || Main.meetupControl.meetupsPlaying.containsKey(p)
                || Main.meetupControl.creandoEventoMeetup.containsKey(p)
                || Main.meetupControl.creandoMapaMeetup.containsKey(p)
                || Main.meetupControl.esperandoCrearEvento.contains(p)
                || Main.meetupControl.esperandoMapaMeetup.contains(p)
                || Main.hotbars.editingSlotHotbar.containsKey(p)
                || Main.hotbars.esperandoEscojaHotbar.contains(p)
                || Main.meetupControl.meetupsPlaying.containsKey(p)
                || Main.meetupControl.esperandoMapaMeetup.contains(p)
                || Main.meetupControl.esperandoCrearEvento.contains(p)
                || Main.meetupControl.creandoMapaMeetup.containsKey(p)
                || Main.meetupControl.creandoEventoMeetup.containsKey(p)
                || Main.specControl.mirando.containsKey(p)) {
            mensaje.sendMessage(Main.extraLang.playerPlayingOne);
            return false;
        } else if (Main.partyControl.partys.containsKey(p)) {
            mensaje.sendMessage(Main.extraLang.playerInParty);
            return false;
        } else if (!Main.extraLang.worlds.contains(p.getWorld().getName())) {
            mensaje.sendMessage(Main.extraLang.playernotintheworld);
            return false;
        }
        return true;
    }

    public static boolean isCheckYouPlaying(Player p) {
        if (jugandoUno.containsKey(p)
                || Main.duelControl.esperandoRanked.containsValue(p)
                || Main.duelControl.esperandoUnRanked.containsValue(p)
                || CrearKitEvent.creatingKit.containsKey(p)
                || CrearKitEvent.editingKit.containsKey(p)
                || CrearKitEvent.waitingEditKit.contains(p)
                || GuiEvent.esperandoEliminarKit.contains(p)
                || CrearMapaEvent.creandoMapa.containsKey(p)
                || Main.meetupControl.meetupsPlaying.containsKey(p)
                || Main.meetupControl.creandoEventoMeetup.containsKey(p)
                || Main.meetupControl.creandoMapaMeetup.containsKey(p)
                || Main.meetupControl.esperandoCrearEvento.contains(p)
                || Main.meetupControl.esperandoMapaMeetup.contains(p)
                || Main.hotbars.editingSlotHotbar.containsKey(p)
                || Main.hotbars.esperandoEscojaHotbar.contains(p)
                || Main.specControl.mirando.containsKey(p)) {
            p.sendMessage(Main.extraLang.youPlayingOne);
            return false;
        } else if (Main.partyControl.partys.containsKey(p)) {
            p.sendMessage(Main.extraLang.youAreInParty);
            return false;
        } else if (!Main.extraLang.worlds.contains(p.getWorld().getName())) {
            p.sendMessage(Main.extraLang.playernotintheworld);
            return false;
        }
        return true;
    }

    public static void remove(Player p) {
        SQL.saveStats(p, true);
        Main.specControl.leave(p, false);
        CrearKitEvent.creatingKit.remove(p);
        CrearKitEvent.editingKit.remove(p);
        CrearKitEvent.waitingEditKit.remove(p);
        GuiEvent.esperandoEliminarKit.remove(p);

        CrearMapaEvent.creandoMapa.remove(p);

        if (jugandoUno.containsKey(p)) {
            jugandoUno.get(p).finish(p);
        }
        if (Main.meetupControl.meetupsPlaying.containsKey(p)) {
            Main.meetupControl.meetupsPlaying.get(p).leave(p, false);
        }
        if (playerConfig.containsKey(p)) {
            playerConfig.get(p).stats.componer();
            playerConfig.remove(p);
        }

        Main.guis.sacar(p);
        Main.duelControl.sacar(p);
        Main.hotbars.sacar(p);
        Main.partyControl.sacar(p);
        Main.meetupControl.sacar(p);
    }

    public static Mapa getMap(Kit k) {
        Mapa m;
        if (Main.extraLang.chooserandommaps) {
            Random r = new Random();
            m = mapLibres.get(k).get(r.nextInt(mapLibres.get(k).size()));
        } else {
            m = mapLibres.get(k).get(0);
        }
        mapLibres.get(k).remove(m);
        mapOcupadas.get(k).add(m);
        return m;
    }

    public static boolean checkAvailableMaps(Kit kit) {
        return mapLibres.containsKey(kit) && !mapLibres.get(kit).isEmpty();
    }

<<<<<<< Updated upstream
    public static void terminarMapa(Mapa m, Kit k) {
        m.regen(k);
        mapOcupadas.get(k).remove(m);
        if (!mapLibres.get(k).contains(m)) {
            mapLibres.get(k).add(m);
=======
    public static void endMap(Map map, Kit kit) {
        map.regen(kit);
        mapOcupadas.get(kit).remove(map);
        if (!mapLibres.get(kit).contains(map)) {
            mapLibres.get(kit).add(map);
>>>>>>> Stashed changes
        }
    }

    public static void endMeetupMap(MapaMeetup m, Kit k) {
        m.regen(k);
        mapMeetupOcupadas.get(k).remove(m);
        mapMeetupLibres.get(k).add(m);
    }

    public static boolean isPerm(Player p, String perm) {
        if (p.hasPermission(perm)) {
            return true;
        }
        p.sendMessage(nopermission + " " + perm);
        playSound(p, NOTE_BASS);
        return false;
    }

    public static boolean isPerm2(Player p, String perm, String perm2) {
        if (p.hasPermission(perm) || p.hasPermission(perm2)) {
            return true;
        }
        p.sendMessage(nopermission + " " + perm + ", " + perm2);
        playSound(p, NOTE_BASS);
        return false;
    }

    public static void setScore(Player p, Score.TipoScore t) {
        scores.put(p, new Score(p, t));
    }

    public static void copy(InputStream in, File file) {
        try {
            OutputStream out = Files.newOutputStream(file.toPath());
            byte[] buf = new byte['?'];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(File file, FileConfiguration fc) {
        try {
            fc.save(file);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(SEVERE, null, ex);
        }
    }

    public static String tc(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> tCC(List<String> list) {
        List<String> finalList = new ArrayList<>();
        int size = list.size();
        for (String s : list) {
            String string = ChatColor.translateAlternateColorCodes('&', s);
            finalList.add(string);
        }
        return finalList;
    }

    public static void changeName(ItemStack item, String s) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Extra.tc(s));
        item.setItemMeta(meta);
    }

    public static void changeLore(ItemStack item, List<String> l) {
        ItemMeta meta = item.getItemMeta();
        if (!(l == null)) {
            List<String> Lore = new ArrayList<>(tCC(l));
            meta.setLore(Lore);
        }
        item.setItemMeta(meta);
    }

    public static ItemStack createId(int id, int dv, String d, List<String> l, int a) {
        ItemStack item = new ItemStack(Material.getMaterial(id), a, (byte) dv);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', d));
        if (!(l == null)) {
            List<String> Lore = new ArrayList<>(tCC(l));
            meta.setLore(Lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static void playSound(Player player, String sound) {
        Sounds.song(player, sound);
    }

    public static void warning(String s) {
        Main.plugin.getServer().getConsoleSender().sendMessage(s);
    }

    public static boolean existColor(String color) {
        switch (color) {
            case "AQUA":
            case "BLACK":
            case "BLUE":
            case "DARK_AQUA":
            case "DARK_BLUE":
            case "DARK_GRAY":
            case "DARK_GREEN":
            case "DARK_PURPLE":
            case "DARK_RED":
            case "GOLD":
            case "GRAY":
            case "GREEN":
            case "LIGHT_PURPLE":
            case "RED":
            case "WHITE":
            case "YELLOW":
                return true;
        }
        return false;
    }

    public static void text(Player p, String s, String hover, String cmd, String color) {
        TextComponent l1 = new TextComponent();
        l1.setText(s);
        l1.setColor(net.md_5.bungee.api.ChatColor.valueOf(color));
        l1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        l1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
        p.spigot().sendMessage(l1);
    }

    public static void limpiarP(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.setLevel(0);
        player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setHealth(20.0);
        player.setFireTicks(0);
    }

<<<<<<< Updated upstream
    public static double getSangre(double sangre) {
        if (sangre >= 19) {
            sangre = 10.0;
        } else if (sangre >= 18) {
            sangre = 9.5;
        } else if (sangre >= 17) {
            sangre = 9.0;
        } else if (sangre >= 16) {
            sangre = 8.5;
        } else if (sangre >= 15) {
            sangre = 8.0;
        } else if (sangre >= 14) {
            sangre = 7.5;
        } else if (sangre >= 13) {
            sangre = 7.0;
        } else if (sangre >= 12) {
            sangre = 6.5;
        } else if (sangre >= 11) {
            sangre = 6.0;
        } else if (sangre >= 10) {
            sangre = 5.5;
        } else if (sangre >= 9) {
            sangre = 5.0;
        } else if (sangre >= 8) {
            sangre = 4.5;
        } else if (sangre >= 7) {
            sangre = 4.0;
        } else if (sangre >= 6) {
            sangre = 3.5;
        } else if (sangre >= 5) {
            sangre = 3.0;
        } else if (sangre >= 4) {
            sangre = 2.5;
        } else if (sangre >= 3) {
            sangre = 2.0;
        } else if (sangre >= 2) {
            sangre = 1.5;
        } else if (sangre >= 1) {
            sangre = 1.0;
        } else if (sangre >= 0.01) {
            sangre = 0.5;
=======
    public static double getHealth(double health) {
        if (health >= 19) {
            health = 10.0;
        } else if (health >= 0.01) {
            health = Math.ceil(health * 2) / 2;
>>>>>>> Stashed changes
        }
        return health;
    }

    public static String NOTE_BASS, CHICKEN_EGG_POP, NOTE_PLING, BURP, ORB_PICKUP, SPLASH2, LEVEL_UP, WITHER_DEATH,
            ANVIL_USE, CAT_MEOW, DRINK, EAT, NOTE_STICKS, ZOMBIE_WOODBREAK, WITHER_SPAWN, FIREWORK_LARGE_BLAST,
            CHEST_CLOSE, CHEST_OPEN, ITEM_PICKUP, VILLAGER_YES, VILLAGER_NO, HORSE_ARMOR, EXPLODE;

    public static void setSoundsVersion() {
        if (Main.CVERSION.contains("1_7") || Main.CVERSION.contains("1_8")) {
            NOTE_BASS = "NOTE_BASS";
            CHICKEN_EGG_POP = "CHICKEN_EGG_POP";
            NOTE_PLING = "NOTE_PLING";
            BURP = "BURP";
            ORB_PICKUP = "ORB_PICKUP";
            SPLASH2 = "SPLASH2";
            LEVEL_UP = "LEVEL_UP";
            WITHER_DEATH = "WITHER_DEATH";
            ANVIL_USE = "ANVIL_USE";
            CAT_MEOW = "CAT_MEOW";
            DRINK = "DRINK";
            EAT = "EAT";
            NOTE_STICKS = "NOTE_STICKS";
            ZOMBIE_WOODBREAK = "ZOMBIE_WOODBREAK";
            WITHER_SPAWN = "WITHER_SPAWN";
            FIREWORK_LARGE_BLAST = "FIREWORK_LARGE_BLAST";
            CHEST_CLOSE = "CHEST_CLOSE";
            CHEST_OPEN = "CHEST_OPEN";
            ITEM_PICKUP = "ITEM_PICKUP";
            VILLAGER_YES = "VILLAGER_YES";
            VILLAGER_NO = "VILLAGER_NO";
            HORSE_ARMOR = "HORSE_ARMOR";
            EXPLODE = "EXPLODE";
        } else {
            //if (version.equals("9")){
            NOTE_BASS = "BLOCK_NOTE_BASS";
            CHICKEN_EGG_POP = "ENTITY_CHICKEN_EGG";
            NOTE_PLING = "BLOCK_NOTE_PLING";
            BURP = "ENTITY_PLAYER_BURP";
            ORB_PICKUP = "ENTITY_EXPERIENCE_ORB_PICKUP";
            SPLASH2 = "ENTITY_PLAYER_SPLASH";
            LEVEL_UP = "ENTITY_PLAYER_LEVELUP";
            WITHER_DEATH = "ENTITY_WITHER_DEATH";
            ANVIL_USE = "BLOCK_ANVIL_USE";
            CAT_MEOW = "ENTITY_CAT_AMBIENT";
            DRINK = "ENTITY_WITCH_DRINK";
            EAT = "ENTITY_PLAYER_BURP";
            NOTE_STICKS = "BLOCK_NOTE_SNARE";
            ZOMBIE_WOODBREAK = "ENTITY_ZOMBIE_BREAK_DOOR_WOOD";
            WITHER_SPAWN = "ENTITY_WITHER_SPAWN";
            FIREWORK_LARGE_BLAST = "ENTITY_FIREWORK_LARGE_BLAST";
            CHEST_CLOSE = "BLOCK_CHEST_CLOSE";
            CHEST_OPEN = "BLOCK_CHEST_OPEN";
            ITEM_PICKUP = "ENTITY_ITEM_PICKUP";
            VILLAGER_YES = "ENTITY_VILLAGER_YES";
            VILLAGER_NO = "ENTITY_VILLAGER_NO";
            HORSE_ARMOR = "ENTITY_HORSE_ARMOR";
            EXPLODE = "BLOCK_GLASS_BREAK";
        }
    }

    public static String secToMin(int i) {
        if (i < 0) {
            i = 0;
        }
        int hours = i / 3600;
        int minutes = (i % 3600) / 60;
        int seconds = i % 60;
        String formattedTime = String.format("%d:%02d:%02d", hours, minutes, seconds);
        if (hours >= 1 && hours <= 3) {
            formattedTime = hours + formattedTime.substring(1);
        }
        return formattedTime;
    }

}
