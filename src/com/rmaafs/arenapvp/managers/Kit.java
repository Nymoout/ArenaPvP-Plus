package com.rmaafs.arenapvp.managers;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Kit {

    public String kitName, kitNameColor;
    public List<String> description = new ArrayList<>();
    public List<ItemStack> deleteBlocks = new ArrayList<>();
    public List<PotionEffect> potionList = new ArrayList<>();
    public ItemStack[] hotbar, armor;
    public ItemStack itemOnGui;
    public boolean regen = false, natural = true, combo = false;
    public int slot, maxTime;

    public String getKitName() {
        return kitName;
    }

    public String getKitNameColor() {
        return kitNameColor;
    }

    public boolean isRegen() {
        return regen;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public Kit(String ki, String kitN, List<ItemStack> del, List<PotionEffect> pot,
            ItemStack[] hot, ItemStack[] ar, ItemStack it, int s, int time, boolean co, boolean na) {
        kitName = ki;
        kitNameColor = kitN;
        potionList = pot;
        hotbar = hot;
        armor = ar;
        itemOnGui = it;
        deleteBlocks = del;
        regen = !deleteBlocks.isEmpty();
        slot = s;
        combo = co;
        natural = na;
        maxTime = time;
    }

}
