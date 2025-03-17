package org.gomadango0113.blockwars.manager;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.gomadango0113.blockwars.BlockWars;
import org.gomadango0113.blockwars.util.ItemUtil;

public class ShopManager {

    public static void spawnShopVillager(Location location) {
        World world = location.getWorld();
        Villager villager = world.spawn(location, Villager.class);
        villager.setProfession(Villager.Profession.FARMER);
        villager.setAgeLock(true);
        villager.setCustomNameVisible(false);
        villager.setMetadata(ChatColor.AQUA + "BlockWars_Villager", new FixedMetadataValue(BlockWars.getInstance(), true));
    }

    public static void openShopMenu(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, ChatColor.BLACK + "ショップメニュー");

        inv.setItem(1, new ItemUtil(Material.GRASS).getItemStack("ブロック", null));
        inv.setItem(2, new ItemUtil(Material.WOOD_SWORD).getItemStack("剣", null));
        inv.setItem(3, new ItemUtil(Material.IRON_CHESTPLATE).getItemStack("装備", null));
        inv.setItem(4, new ItemUtil(Material.STONE_PICKAXE).getItemStack("ピッケル", null));
        inv.setItem(5, new ItemUtil(Material.BOW).getItemStack("弓矢", null));
        inv.setItem(6, new ItemUtil(Material.TNT).getItemStack("その他", null));

        if (page == 1) {
            inv.setItem(19, new ItemStack(Material.WOOL, 16));
            inv.setItem(20, new ItemStack(Material.STAINED_CLAY, 16));
            inv.setItem(21, new ItemUtil(Material.GLASS).getItemStack(null, null));
            inv.setItem(22, new ItemStack(Material.ENDER_STONE));
            inv.setItem(23, new ItemStack(Material.WOOD));
        }

        player.openInventory(inv);
    }
}
