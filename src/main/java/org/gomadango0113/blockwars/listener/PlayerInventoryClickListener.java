package org.gomadango0113.blockwars.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        Inventory inv = event.getInventory();

        if (inv.getTitle().equalsIgnoreCase(ChatColor.BLACK + "ショップメニュー")) {
            event.setCancelled(true);
        }
    }
}
