package com.rmaafs.arenapvp.KitControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.rmaafs.arenapvp.Main.guis;
import static com.rmaafs.arenapvp.Main.plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class CrearKitEvent implements Listener {

    public static HashMap<Player, CrearKit> creatingKit = new HashMap<>();
    public static HashMap<Player, EditandoKit> editingKit = new HashMap<>();
    public static List<Player> waitingEditKit = new ArrayList<>();

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        if (creatingKit.containsKey(e.getPlayer())) {
            e.setCancelled(true);
            CrearKit ck = creatingKit.get(e.getPlayer());
            if (ck.action == CrearKit.Action.NAME) {
                ck.setKitName(e.getMessage(), false);
            } else if (ck.action == CrearKit.Action.NAME_COLOR) {
                ck.setKitName(e.getMessage(), true);
            } else if (ck.action == CrearKit.Action.INVENTORY && e.getMessage().toLowerCase().contains("ready")) {
                ck.setInventory();
            } else if (ck.action == CrearKit.Action.ITEMS_TO_DELETE && e.getMessage().toLowerCase().contains("ready")) {
                ck.setRegenItems();
            } else {
                e.setCancelled(false);
            }
        } else if (editingKit.containsKey(e.getPlayer())) {
            e.setCancelled(true);
            EditandoKit ck = editingKit.get(e.getPlayer());
            if (ck.accion == EditandoKit.Accion.NOMBRECOLOR) {
                ck.setKitName(e.getMessage(), true);
            } else if (ck.accion == EditandoKit.Accion.INVENTARIO && e.getMessage().toLowerCase().contains("ready")) {
                ck.setInventory();
            } else if (ck.accion == EditandoKit.Accion.ITEMSBORRAR && e.getMessage().toLowerCase().contains("ready")) {
                ck.setRegenItems();
            } else {
                e.setCancelled(false);
            }
        }
    }

//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent e) {
//        Player p = (Player) e.getWhoClicked();
//        if (creandoKit.containsKey(p)) {
//            CrearKit ck = creandoKit.get(p);
//            if (e.getCurrentItem() != null) {
//                if (ck.accion == Accion.POTIONS) {
//                    e.setCancelled(true);
//                    ck.click(e.getCurrentItem(), e.isRightClick());
//                }
//            }
//        }
//    }

    @EventHandler
    public void onInventoryCreativeClick(InventoryCreativeEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (creatingKit.containsKey(p)) {
            CrearKit ck = creatingKit.get(p);
            if (e.getCurrentItem() != null) {
                if (ck.action == CrearKit.Action.ITEM) {
                    p.closeInventory();
                    ck.clickItem(e.getCursor());
                }
            }
        } else if (editingKit.containsKey(p)) {
            EditandoKit ck = editingKit.get(p);
            if (e.getCurrentItem() != null) {
                if (ck.accion == EditandoKit.Accion.ITEM) {
                    p.closeInventory();
                    ck.clickItem(e.getCursor());
                }
            }
        } else if  (!p.spigot().getCollidesWithEntities()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        final Player p = (Player) e.getPlayer();
        if (creatingKit.containsKey(p)) {
            final CrearKit ck = creatingKit.get(p);
            if (ck.action == CrearKit.Action.POTIONS) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        p.openInventory(ck.inv);
                    }
                }, 1L);
            } else if (ck.action == CrearKit.Action.ITEM && e.getInventory().getName().equals(guis.acomodacion.getName())) {
                boolean tiene = false;
                for (ItemStack i : p.getInventory().getContents()) {
                    if (i != null) {
                        tiene = true;
                        break;
                    }
                }
                if (!tiene) {
                    ck.createKit();
                } else {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            p.openInventory(guis.acomodacion);
                        }
                    }, 1L);
                }
            }
        } else if (editingKit.containsKey(p)) {
            final EditandoKit ck = editingKit.get(p);
            if (ck.accion == EditandoKit.Accion.POTIONS) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        p.openInventory(ck.inv);
                    }
                }, 1L);
            } else if (ck.accion == EditandoKit.Accion.ITEM && e.getInventory().getName().equals(guis.acomodacion.getName())) {
                boolean tiene = false;
                for (ItemStack i : p.getInventory().getContents()) {
                    if (i != null) {
                        tiene = true;
                        break;
                    }
                }
                if (!tiene) {
                    ck.createKit();
                } else {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            p.openInventory(guis.acomodacion);
                        }
                    }, 1L);
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        if (creatingKit.containsKey(p)) {
            e.setCancelled(true);
            if (creatingKit.get(p).action == CrearKit.Action.ITEM && !p.getOpenInventory().getTitle().equals(guis.acomodacion.getName())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        p.getInventory().clear();
                    }
                }, 1L);
            }
        } else if (editingKit.containsKey(p)) {
            e.setCancelled(true);
            if (editingKit.get(p).accion == EditandoKit.Accion.ITEM && !p.getOpenInventory().getTitle().equals(guis.acomodacion.getName())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        p.getInventory().clear();
                    }
                }, 1L);
            }
        }
    }
}
