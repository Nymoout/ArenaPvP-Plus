package com.rmaafs.arenapvp.API;

import java.util.ArrayList;
import java.util.List;

import com.rmaafs.arenapvp.Party.Party;
<<<<<<< Updated upstream
import com.rmaafs.arenapvp.Kit;
import com.rmaafs.arenapvp.Mapa;
=======
import com.rmaafs.arenapvp.managers.Kit;
import com.rmaafs.arenapvp.managers.Map;
>>>>>>> Stashed changes
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartyEventFFAFinishEvent extends Event {

    private final Player owner;
    private final Player winner;
    private final String kitName;
    private final String mapName;
    private final List<Player> players;
    private final List<Player> spectators;
    private final Location spawn1;
    private final Location spawn2;
    private Location corner1;
    private Location corner2;
    
    public PartyEventFFAFinishEvent(Party party, Kit kit, Mapa mapa, List<Player> spec, Player w){
        players = new ArrayList<>();
        spectators = new ArrayList<>();
        owner = party.owner;
        winner = w;
        kitName = kit.getKitName();
        mapName = mapa.getName();
        players.addAll(party.players);
        spectators.addAll(spec);
        spawn1 = mapa.getSpawn1();
        spawn2 = mapa.getSpawn2();
        if (kit.isRegen()){
            corner1 = mapa.getCorner1();
            corner2 = mapa.getCorner2();
        }
    }

    public Player getOwner() {
        return owner;
    }

    public String getKitName() {
        return kitName;
    }

    public String getMapName() {
        return mapName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public Player getWinner() {
        return winner;
    }
    
    private static final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
