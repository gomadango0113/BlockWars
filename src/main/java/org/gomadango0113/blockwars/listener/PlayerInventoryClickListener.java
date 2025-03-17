package org.gomadango0113.blockwars.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.gomadango0113.blockwars.manager.ShopManager;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();

        if (item != null) {
            if (inv.getTitle().equalsIgnoreCase(ChatColor.BLACK + "ショップメニュー")) {
                event.setCancelled(true);

                if (event.getSlot() >= 1 && event.getSlot() <= 6) {
                    ShopManager.openShopMenu(player, event.getSlot());
                }
                else {
                    ShopManager.BuyItem buy_item = ShopManager.BuyItem.getBuyItem(item);
                    if (buy_item != null) {
                        ShopManager.buyItem(player, buy_item);
                    }
                }
            }
        }
    }
}
