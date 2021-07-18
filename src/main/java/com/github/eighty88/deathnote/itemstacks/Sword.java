package com.github.eighty88.deathnote.itemstacks;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Sword {
    public static ItemStack getItemStack() {
        ItemStack result = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = result.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1000, true);
        result.setItemMeta(meta);
        return result;
    }

    public static ItemStack getFake() {
        ItemStack result = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = result.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 1000, true);
        result.setItemMeta(meta);
        return result;
    }
}
