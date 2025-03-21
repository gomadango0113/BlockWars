package org.gomadango0113.blockwars.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    private ItemStack itemstack;

    public ItemUtil(Material material) {
        this.itemstack = new ItemStack(material);
    }

    public ItemUtil(Material material, int amount) {
        this.itemstack = new ItemStack(material, amount);
    }

    public ItemUtil(ItemStack itemStack) {
        this.itemstack = itemStack;
    }

    public ItemStack getItemStack(String name, List<String> lore) {
        ItemMeta meta = itemstack.getItemMeta();
        List<String> get_lore = meta.getLore() == null ? new ArrayList<>() : new ArrayList<>(meta.getLore());
        if (lore != null) {
            get_lore.addAll(lore);
        }
        setNameLore(itemstack, name, get_lore);

        return itemstack;
    }

    public ItemStack getRawItemStack() {
        return itemstack;
    }

    private static ItemStack setNameLore(ItemStack itemStack, String name, List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        if (name != null) meta.setDisplayName(name);
        if (lore != null) meta.setLore(lore);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

}
