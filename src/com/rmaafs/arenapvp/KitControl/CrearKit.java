package com.rmaafs.arenapvp.KitControl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rmaafs.arenapvp.Extra;

import com.rmaafs.arenapvp.Convertor;

import com.rmaafs.arenapvp.FileKits;
import com.rmaafs.arenapvp.Kit;

import static com.rmaafs.arenapvp.Extra.*;
import static com.rmaafs.arenapvp.KitControl.CrearKit.Action.ITEM;
import static com.rmaafs.arenapvp.KitControl.CrearKitEvent.creatingKit;
import static com.rmaafs.arenapvp.Main.guis;
import static com.rmaafs.arenapvp.Main.hotbars;
import static com.rmaafs.arenapvp.Main.plugin;
<<<<<<< Updated upstream
import com.rmaafs.arenapvp.Mapa;
=======
import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;
import static org.bukkit.enchantments.Enchantment.LUCK;
import static org.bukkit.potion.PotionEffectType.*;

import com.rmaafs.arenapvp.Map;
>>>>>>> Stashed changes
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CrearKit {

    public enum Action {
        NAME, NAME_COLOR, INVENTORY, ITEMS_TO_DELETE, POTIONS, ITEM
    }

    private String name;
    private String nameColor;
    private String inventory;
    private String itemsToDelete;
    private String potions;
    private String item;
    private String created;
    public Action action = Action.NAME;
    private final Player player;
    public Inventory inv;
    public String kitName, kitNameColor;
    public ItemStack[] hotbar, armor;
    public List<ItemStack> deleteBlocks = new ArrayList<>();
    public ItemStack itemSpeed;
    public ItemStack itemStrength;
    public ItemStack itemSlow;
    public ItemStack itemFire;
    public ItemStack itemCombo;
    public ItemStack itemTime;
    public ItemStack itemReady;
    public ItemStack itemNatural;
    public ItemStack itemOnGui;
    int levelSpeed = 1;
    int levelStrength = 1;
    int levelSlow = 1;
    int slot = 0;
    int time;
    public List<PotionEffect> potionList = new ArrayList<>();
    boolean regen = false;
    boolean combo = false;
    boolean natural = true;

    public CrearKit(Player player) {
        this.player = player;
        setConfig();
        step();
    }

    public void setConfig() {
        inv = Bukkit.createInventory(null, cconfig.getInt("creating.gui.rows") * 9, Extra.tc(clang.getString("creating.gui.guiname")));

        time = cconfig.getInt("defaultmaxtime");

        itemSpeed = Extra.createId(cconfig.getInt("creating.gui.speed.id"), cconfig.getInt("creating.gui.speed.data-value"), Extra.tc(clang.getString("creating.gui.speed.name").replaceAll("<level>", "" + levelSpeed)), Extra.tCC(clang.getStringList("creating.gui.speed.lore")), 1);
        itemStrength = Extra.createId(cconfig.getInt("creating.gui.strength.id"), cconfig.getInt("creating.gui.strength.data-value"), Extra.tc(clang.getString("creating.gui.strength.name").replaceAll("<level>", "" + levelStrength)), Extra.tCC(clang.getStringList("creating.gui.strength.lore")), 1);
        itemSlow = Extra.createId(cconfig.getInt("creating.gui.slowness.id"), cconfig.getInt("creating.gui.slowness.data-value"), Extra.tc(clang.getString("creating.gui.slowness.name").replaceAll("<level>", "" + levelSlow)), Extra.tCC(clang.getStringList("creating.gui.slowness.lore")), 1);
        itemFire = Extra.createId(cconfig.getInt("creating.gui.fire_resistance.id"), cconfig.getInt("creating.gui.fire_resistance.data-value"), Extra.tc(clang.getString("creating.gui.fire_resistance.name")), Extra.tCC(clang.getStringList("creating.gui.fire_resistance.lore")), 1);
        itemCombo = Extra.createId(cconfig.getInt("creating.gui.hitdelay.id"), cconfig.getInt("creating.gui.hitdelay.data-value"), Extra.tc(clang.getString("creating.gui.hitdelay.name")), Extra.tCC(clang.getStringList("creating.gui.hitdelay.lore")), 1);
        itemTime = Extra.createId(cconfig.getInt("creating.gui.matchtime.id"), cconfig.getInt("creating.gui.matchtime.data-value"), Extra.tc(clang.getString("creating.gui.matchtime.name").replaceAll("<time>", Extra.secToMin(time))), Extra.tCC(clang.getStringList("creating.gui.matchtime.lore")), 1);
        itemReady = Extra.createId(cconfig.getInt("creating.gui.ready.id"), cconfig.getInt("creating.gui.ready.data-value"), Extra.tc(clang.getString("creating.gui.ready.name")), Extra.tCC(clang.getStringList("creating.gui.ready.lore")), 1);
        itemNatural = Extra.createId(cconfig.getInt("creating.gui.naturalregeneration.id"), cconfig.getInt("creating.gui.naturalregeneration.data-value"), Extra.tc(clang.getString("creating.gui.naturalregeneration.name")), Extra.tCC(clang.getStringList("creating.gui.naturalregeneration.lore")), 1);

        inv.setItem(cconfig.getInt("creating.gui.speed.slot"), itemSpeed);
        inv.setItem(cconfig.getInt("creating.gui.strength.slot"), itemStrength);
        inv.setItem(cconfig.getInt("creating.gui.slowness.slot"), itemSlow);
        inv.setItem(cconfig.getInt("creating.gui.fire_resistance.slot"), itemFire);
        inv.setItem(cconfig.getInt("creating.gui.hitdelay.slot"), itemCombo);
        inv.setItem(cconfig.getInt("creating.gui.matchtime.slot"), itemTime);
        inv.setItem(cconfig.getInt("creating.gui.ready.slot"), itemReady);
        inv.setItem(cconfig.getInt("creating.gui.naturalregeneration.slot"), itemNatural);

        name = Extra.tc(clang.getString("creating.kit.name"));
        nameColor = Extra.tc(clang.getString("creating.kit.namecolor"));
        inventory = Extra.tc(clang.getString("creating.kit.inventory"));
        itemsToDelete = Extra.tc(clang.getString("creating.kit.itemstodelete"));
        potions = Extra.tc(clang.getString("creating.kit.potions"));
        item = Extra.tc(clang.getString("creating.kit.item"));
        created = Extra.tc(clang.getString("creating.kit.created"));
    }

    private void step() {
        switch (action) {
            case NAME:
                player.sendMessage(name);
                break;
            case NAME_COLOR:
                player.sendMessage(nameColor);
                break;
<<<<<<< Updated upstream
            case INVENTARIO:
                p.sendMessage(inventory);
                Extra.limpiarP(p);
                p.setGameMode(GameMode.CREATIVE);
                break;
            case ITEMSBORRAR:
                p.sendMessage(itemstodelete);
                Extra.limpiarP(p);
                p.setGameMode(GameMode.CREATIVE);
                break;
            case POTIONS:
                p.sendMessage(potions);
                Extra.limpiarP(p);
                p.openInventory(inv);
=======
            case INVENTORY:
                player.sendMessage(inventory);
                Extra.cleanPlayer(player);
                player.setGameMode(GameMode.CREATIVE);
                break;
            case ITEMS_TO_DELETE:
                player.sendMessage(itemsToDelete);
                Extra.cleanPlayer(player);
                player.setGameMode(GameMode.CREATIVE);
                break;
            case POTIONS:
                player.sendMessage(potions);
                Extra.cleanPlayer(player);
                player.openInventory(inv);
>>>>>>> Stashed changes
                break;
            case ITEM:
                player.sendMessage(item);
                player.setGameMode(GameMode.CREATIVE);
                break;
        }
    }

    public void setKitName(String s, boolean color) {
        if (color) {
            kitNameColor = Extra.tc(s);
            action = Action.INVENTORY;
        } else {
            kitName = s;
            action = Action.NAME_COLOR;
        }
        step();
    }

    public void setInventory() {
        armor = player.getInventory().getArmorContents();
        hotbar = player.getInventory().getContents();
        action = Action.ITEMS_TO_DELETE;
        step();
    }

    public void setRegenItems() {
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null) {
                if (i.getAmount() != 1) {
                    i.setAmount(1);
                }
                deleteBlocks.add(i);
                regen = true;
            }
        }
        action = Action.POTIONS;
        step();
    }

    public void click(ItemStack item, boolean right) {
        int level = 1;
        if (item.isSimilar(itemSpeed)) {
            level = handleEnchantmentLevel(right, levelSpeed, 2);
            levelSpeed = level;
            itemSpeed = item;
            updateItemName(item, "creating.gui.speed.name", levelSpeed);
        } else if (item.isSimilar(itemStrength)) {
            level = handleEnchantmentLevel(right, levelStrength, 2);
            levelStrength = level;
            itemStrength = item;
            updateItemName(item, "creating.gui.strength.name", levelStrength);
        } else if (item.isSimilar(itemSlow)) {
            level = handleEnchantmentLevel(right, levelSlow, 2);
            levelSlow = level;
            itemSlow = item;
            updateItemName(item, "creating.gui.slowness.name", levelSlow);
        } else if (item.isSimilar(itemTime)) {
            handleTimeChange(right);
            itemTime = item;
            updateItemName(item, "creating.gui.matchtime.name", time);
        } else if (item.isSimilar(itemReady)) {
            saveEnchant();
        }

        if (!right && !item.equals(itemTime) && !item.equals(itemReady)) {
            handleEnchantmentApplication(item, level);
        }
    }

    private int handleEnchantmentLevel(boolean right, int currentLevel, int maxLevel) {
        return right ? (currentLevel % maxLevel) + 1 : currentLevel;
    }

    private void handleTimeChange(boolean right) {
        if (right) {
            time = (time + 60) % 60 == 0 ? time + 60 : 60;
        } else {
            time = time - 60 < 10 ? 30 : time - 60;
        }
    }

    private void updateItemName(ItemStack item, String messageKey, int value) {
        Extra.changeName(item, clang.getString(messageKey).replaceAll("<level>", String.valueOf(value)));
    }

    private void handleEnchantmentApplication(ItemStack item, int level) {
        if (item.containsEnchantment(LUCK)) {
            item.removeEnchantment(LUCK);
        } else {
            item.addUnsafeEnchantment(LUCK, level);
        }
    }


    private void saveEnchant() {
        if (itemSpeed.containsEnchantment(LUCK)) {
            potionList.add(new PotionEffect(SPEED, 99999, levelSpeed));
        }
        if (itemStrength.containsEnchantment(LUCK)) {
            potionList.add(new PotionEffect(INCREASE_DAMAGE, 99999, levelStrength));
        }
        if (itemSlow.containsEnchantment(LUCK)) {
            potionList.add(new PotionEffect(SLOW, 99999, levelSlow));
        }
        if (itemFire.containsEnchantment(LUCK)) {
            potionList.add(new PotionEffect(FIRE_RESISTANCE, 99999, 1));
        }
        if (itemCombo.containsEnchantment(LUCK)) {
            combo = true;
        }
        if (itemNatural.containsEnchantment(LUCK)) {
            natural = false;
        }
        action = ITEM;
        player.closeInventory();
        step();
    }

    public void clickItem(ItemStack item) {
        if (item.getAmount() != 1) {
            item.setAmount(1);
        }
        Extra.changeName(item, kitNameColor);
        itemOnGui = item;
        player.openInventory(guis.acomodacion);
    }

    public void createKit() {
        File kitsFolder = new File(plugin.getDataFolder() + File.separator + "kits");
        File hotbarFolder = new File(plugin.getDataFolder() + File.separator + "hotbar");

        kitsFolder.mkdirs();
        hotbarFolder.mkdirs();

        int slot = -1;
        for (int i = 0; i < guis.acomodacion.getContents().length; i++) {
            if (guis.acomodacion.getContents()[i] != null && guis.acomodacion.getContents()[i].isSimilar(itemOnGui)) {
                slot = i;
                break; // No need to continue searching
            }
        }

        Kit k = new Kit(kitName, kitNameColor, deleteBlocks, potionList, hotbar, armor, itemOnGui, slot, time, combo, natural);
        kits.put(kitName, k);
        guis.itemKits.put(itemOnGui, k);
        guis.saveItems();
        player.sendMessage(created.replaceAll("<kit>", kitName));
        creatingKit.remove(player);
        hotbars.setMain(player);
        mapLibres.put(k, new ArrayList<>());
        mapOcupadas.put(k, new ArrayList<>());

        File kitFile = new File(kitsFolder, kitName + ".yml");
        File hotbarFile = new File(hotbarFolder, kitName + ".yml");

<<<<<<< Updated upstream
        List<Mapa> lista1 = new ArrayList<>();
        mapLibres.put(k, lista1);
        
        List<Mapa> lista2 = new ArrayList<>();
        mapOcupadas.put(k, lista2);
        
        File elkit = new File(plugin.getDataFolder() + File.separator + "kits" + File.separator + kitName + ".yml");
=======
>>>>>>> Stashed changes
        try {
            kitFile.createNewFile();
            FileConfiguration kitConfig = loadConfiguration(kitFile);
            kitConfig.set("name", kitName);
            kitConfig.set("namecolor", kitNameColor);
            kitConfig.set("regen", regen);
            kitConfig.set("slot", slot);
            kitConfig.set("time", time);
            kitConfig.set("naturalregeneration", natural);
            kitConfig.set("combo", combo);

            for (PotionEffect e : potionList) {
                kitConfig.set("potionlist." + e.getType().getName(), e.getAmplifier());
            }

            kitConfig.set("item", Convertor.itemToBase64(new ItemStack[] { itemOnGui }));
            kitConfig.set("delete", Convertor.itemToBase64(deleteBlocks.toArray(new ItemStack[0])));
            kitConfig.set("hotbar", Convertor.itemToBase64(hotbar));
            kitConfig.set("armor", Convertor.itemToBase64(armor));

            save(kitFile, kitConfig);

            hotbarFile.createNewFile();
            FileConfiguration hotbarConfig = loadConfiguration(hotbarFile);
            guis.kitsHotbar.put(k, new FileKits(hotbarFile, hotbarConfig));
            save(hotbarFile, hotbarConfig);
        } catch (IOException ex) {
            Logger.getLogger(CrearKit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
