package com.rmaafs.arenapvp.KitControl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rmaafs.arenapvp.util.Extra;

import com.rmaafs.arenapvp.util.FileKits;
import com.rmaafs.arenapvp.managers.Kit;

import static com.rmaafs.arenapvp.util.Extra.*;
import static com.rmaafs.arenapvp.KitControl.CreateKit.Action.*;
import static com.rmaafs.arenapvp.ArenaPvP.guis;
import static com.rmaafs.arenapvp.ArenaPvP.hotbars;
import static com.rmaafs.arenapvp.ArenaPvP.plugin;
import static org.bukkit.GameMode.CREATIVE;
import static org.bukkit.enchantments.Enchantment.LUCK;

import com.rmaafs.arenapvp.managers.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CreateKit {

    public enum Action {
        NAME, COLOR_NAME, INVENTORY, ITEMS_TO_DELETE, POTIONS, ITEM
    }

    public String name;
    public String colorName;
    public String inventory;
    public String itemsToDelete;
    public String potions;
    public String item;
    public String created;
    public Action action = Action.NAME;
    public Player player;
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
    List<PotionEffect> potionList = new ArrayList<>();
    boolean regen = false;
    boolean combo = false;
    boolean natural = true;

    public CreateKit(Player player) {
        this.player = player;
        setConfig();
        nexStep();
    }

    public void setConfig() {
        inv = Bukkit.createInventory(null, cconfig.getInt("creating.gui.rows") * 9, tc(clang.getString("creating.gui.guiname")));

        time = cconfig.getInt("defaultmaxtime");

        itemSpeed = crearId(cconfig.getInt("creating.gui.speed.id"),
                cconfig.getInt("creating.gui.speed.data-value"),
                tc(clang.getString("creating.gui.speed.name").replaceAll("<level>", "" + levelSpeed)),
                tCC(clang.getStringList("creating.gui.speed.lore")),
                1
        );
        itemStrength = crearId(cconfig.getInt("creating.gui.strength.id"),
                cconfig.getInt("creating.gui.strength.data-value"),
                tc(clang.getString("creating.gui.strength.name").replaceAll("<level>", "" + levelStrength)),
                tCC(clang.getStringList("creating.gui.strength.lore")),
                1
        );
        itemSlow = crearId(cconfig.getInt("creating.gui.slowness.id"),
                cconfig.getInt("creating.gui.slowness.data-value"),
                tc(clang.getString("creating.gui.slowness.name").replaceAll("<level>", "" + levelSlow)),
                tCC(clang.getStringList("creating.gui.slowness.lore")),
                1
        );
        itemFire = crearId(cconfig.getInt("creating.gui.fire_resistance.id"),
                cconfig.getInt("creating.gui.fire_resistance.data-value"),
                tc(clang.getString("creating.gui.fire_resistance.name")),
                tCC(clang.getStringList("creating.gui.fire_resistance.lore")),
                1
        );
        itemCombo = crearId(cconfig.getInt("creating.gui.hitdelay.id"),
                cconfig.getInt("creating.gui.hitdelay.data-value"),
                tc(clang.getString("creating.gui.hitdelay.name")),
                tCC(clang.getStringList("creating.gui.hitdelay.lore")),
                1
        );
        itemTime = crearId(cconfig.getInt("creating.gui.matchtime.id"),
                cconfig.getInt("creating.gui.matchtime.data-value"),
                tc(clang.getString("creating.gui.matchtime.name").replaceAll("<time>", Extra.secToMin(time))),
                tCC(clang.getStringList("creating.gui.matchtime.lore")),
                1
        );
        itemReady = crearId(cconfig.getInt("creating.gui.ready.id"),
                cconfig.getInt("creating.gui.ready.data-value"),
                tc(clang.getString("creating.gui.ready.name")),
                tCC(clang.getStringList("creating.gui.ready.lore")),
                1
        );
        itemNatural = crearId(cconfig.getInt("creating.gui.naturalregeneration.id"),
                cconfig.getInt("creating.gui.naturalregeneration.data-value"),
                tc(clang.getString("creating.gui.naturalregeneration.name")),
                tCC(clang.getStringList("creating.gui.naturalregeneration.lore")),
                1
        );
        inv.setItem(cconfig.getInt("creating.gui.speed.slot"), itemSpeed);
        inv.setItem(cconfig.getInt("creating.gui.strength.slot"), itemStrength);
        inv.setItem(cconfig.getInt("creating.gui.slowness.slot"), itemSlow);
        inv.setItem(cconfig.getInt("creating.gui.fire_resistance.slot"), itemFire);
        inv.setItem(cconfig.getInt("creating.gui.hitdelay.slot"), itemCombo);
        inv.setItem(cconfig.getInt("creating.gui.matchtime.slot"), itemTime);
        inv.setItem(cconfig.getInt("creating.gui.ready.slot"), itemReady);
        inv.setItem(cconfig.getInt("creating.gui.naturalregeneration.slot"), itemNatural);

        name = tc(clang.getString("creating.kit.name"));
        colorName = tc(clang.getString("creating.kit.namecolor"));
        inventory = tc(clang.getString("creating.kit.inventory"));
        itemsToDelete = tc(clang.getString("creating.kit.itemstodelete"));
        potions = tc(clang.getString("creating.kit.potions"));
        item = tc(clang.getString("creating.kit.item"));
        created = tc(clang.getString("creating.kit.created"));
    }

    private void nexStep() {
        switch (action) {
            case NAME:
                player.sendMessage(name);
                break;
            case COLOR_NAME:
                player.sendMessage(colorName);
                break;
            case INVENTORY:
                player.sendMessage(inventory);
                Extra.cleanPlayer(player);
                player.setGameMode(CREATIVE);
                break;
            case ITEMS_TO_DELETE:
                player.sendMessage(itemsToDelete);
                Extra.cleanPlayer(player);
                player.setGameMode(CREATIVE);
                break;
            case POTIONS:
                player.sendMessage(potions);
                Extra.cleanPlayer(player);
                player.openInventory(inv);
                break;
            case ITEM:
                player.sendMessage(item);
                player.setGameMode(CREATIVE);
                break;
        }
    }

    public void setKitName(String s, boolean color) {
        if (color) {
            kitNameColor = tc(s);
            action = INVENTORY;
        } else {
            kitName = s;
            action = COLOR_NAME;
        }
        nexStep();
    }

    public void setInventory() {
        armor = player.getInventory().getArmorContents();
        hotbar = player.getInventory().getContents();
        action = ITEMS_TO_DELETE;
        nexStep();
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
        action = POTIONS;
        nexStep();
    }

    public void click(ItemStack item, boolean right) {
        int level = 1;
        int maxLevel = 2;

        if (item.isSimilar(itemSpeed)) {
            level = adjustLevel(right, levelSpeed, maxLevel);
            itemSpeed = item;
            Extra.changeName(item, replacePlaceholder(clang.getString("creating.gui.speed.name"), "<level>", "" + levelSpeed));
        } else if (item.isSimilar(itemStrength)) {
            level = adjustLevel(right, levelStrength, maxLevel);
            itemStrength = item;
            Extra.changeName(item, replacePlaceholder(clang.getString("creating.gui.strength.name"), "<level>", "" + levelStrength));
        } else if (item.isSimilar(itemSlow)) {
            level = adjustLevel(right, levelSlow, maxLevel);
            itemSlow = item;
            Extra.changeName(item, replacePlaceholder(clang.getString("creating.gui.slowness.name"), "<level>", "" + levelSlow));
        } else if (item.isSimilar(itemTime)) {
            time = adjustTime(right, time);
            itemTime = item;
            Extra.changeName(item, replacePlaceholder(clang.getString("creating.gui.matchtime.name"), "<time>", Extra.secToMin(time)));
        } else if (item.isSimilar(itemReady)) {
            saveEnchant();
        }

        if (!right && !item.equals(itemTime) && !item.equals(itemReady)) {
            updateEnchantment(item, level);
        }
    }

    private int adjustLevel(boolean right, int currentLevel, int maxLevel) {
        if (right) {
            currentLevel = (currentLevel % maxLevel) + 1;
        } else {
            currentLevel = ((currentLevel - 2 + maxLevel) % maxLevel) + 1;
        }
        return currentLevel;
    }

    private int adjustTime(boolean right, int currentTime) {
        int change = 60;
        if (!right) {
            change = -60;
        }

        int newTime = currentTime + change;
        if (newTime % 60 != 0) {
            newTime = 60;
        } else if (newTime < 10) {
            newTime = 30;
        }
        return newTime;
    }

    private void updateEnchantment(ItemStack item, int level) {
        if (item.containsEnchantment(LUCK)) {
            item.removeEnchantment(LUCK);
        } else {
            item.addUnsafeEnchantment(LUCK, level);
        }
    }

    private String replacePlaceholder(String text, String placeholder, String value) {
        return text.replaceAll(placeholder, value);
    }


    private void saveEnchant() {
        if (itemSpeed.containsEnchantment(LUCK)) {
            potionList.add(new PotionEffect(PotionEffectType.SPEED, 99999, levelSpeed));
        }
        if (itemStrength.containsEnchantment(LUCK)) {
            potionList.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, levelStrength));
        }
        if (itemSlow.containsEnchantment(LUCK)) {
            potionList.add(new PotionEffect(PotionEffectType.SLOW, 99999, levelSlow));
        }
        if (itemFire.containsEnchantment(LUCK)) {
            potionList.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 1));
        }
        if (itemCombo.containsEnchantment(LUCK)) {
            combo = true;
        }
        if (itemNatural.containsEnchantment(LUCK)) {
            natural = false;
        }
        action = Action.ITEM;
        player.closeInventory();
        nexStep();
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
        createDirectories();
        guis.saveItems();
        findSlotForItem();
        Kit k = createKitInstance();
        updateDataStructures(k);
        saveKitToFile(k);
    }

    private void createDirectories() {
        createDirectory(plugin.getDataFolder() + File.separator + "kits");
        createDirectory(plugin.getDataFolder() + File.separator + "hotbar");
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private void findSlotForItem() {
        for (int i = 0; i < guis.acomodacion.getContents().length; i++) {
            if (guis.acomodacion.getContents()[i] != null && guis.acomodacion.getContents()[i].isSimilar(itemOnGui)) {
                slot = i;
            }
        }
    }

    private Kit createKitInstance() {
        Kit k = new Kit(kitName, kitNameColor, deleteBlocks, potionList, hotbar, armor, itemOnGui, slot, time, combo, natural);
        kits.put(kitName, k);
        guis.itemKits.put(itemOnGui, k);
        guis.saveItems();
        player.sendMessage(created.replaceAll("<kit>", kitName));
        if (CreateKitEvent.creatingKit.containsKey(player)) {
            CreateKitEvent.creatingKit.remove(player);
        }
        hotbars.setMain(player);
        mapLibres.put(k, new ArrayList<>());
        mapOcupadas.put(k, new ArrayList<>());
        return k;
    }

    private void updateDataStructures(Kit k) {
        List<Map> lista1 = new ArrayList<>();
        mapLibres.put(k, lista1);

        List<Map> lista2 = new ArrayList<>();
        mapOcupadas.put(k, lista2);
    }

    private void saveKitToFile(Kit k) {
        File elkit = new File(plugin.getDataFolder() + File.separator + "kits" + File.separator + kitName + ".yml");
        try {
            elkit.createNewFile();
            FileConfiguration ckit = YamlConfiguration.loadConfiguration(elkit);
            // Set properties in ckit here

            Extra.guardar(elkit, ckit);

            File hot = new File(plugin.getDataFolder() + File.separator + "hotbar" + File.separator + kitName + ".yml");
            hot.createNewFile();
            FileConfiguration chot = YamlConfiguration.loadConfiguration(hot);
            guis.kitsHotbar.put(k, new FileKits(hot, chot));
            Extra.guardar(hot, chot);
        } catch (IOException ex) {
            Logger.getLogger(CreateKit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
