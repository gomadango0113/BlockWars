package org.gomadango0113.blockwars.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.gomadango0113.blockwars.BlockWars;

public class ShopManager {

    public static void spawnShopVillager(Location location) {
        World world = location.getWorld();
        Villager villager = world.spawn(location, Villager.class);
        villager.setProfession(Villager.Profession.FARMER);
        villager.setAgeLock(true);
        villager.setCustomNameVisible(false);
        villager.setMetadata(ChatColor.AQUA + "BlockWars_Villager", new FixedMetadataValue(BlockWars.getInstance(), true));
    }

    public static void openShopMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, ChatColor.BLACK + "ショップメニュー");

        player.openInventory(inv);
    }
}
