package com.rmaafs.arenapvp.API;

import static com.rmaafs.arenapvp.Main.duelControl;
import org.bukkit.entity.Player;

public class CreateDuel {

    public static void createFakeDuel(Player player, Player bot){
        duelControl.createFakeDuel(player, bot);
    }
}
