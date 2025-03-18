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
import org.gomadango0113.blockwars.manager.TeamManager;
import org.gomadango0113.blockwars.manager.TeamUpdateManager;
import org.gomadango0113.blockwars.util.ChatUtil;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();
        int slot = event.getSlot();

        if (item != null) {
            if (inv.getTitle().equalsIgnoreCase(ChatColor.BLACK + "ショップメニュー")) {
                event.setCancelled(true);

                if (slot >= 1 && slot <= 6) {
                    ShopManager.openShopMenu(player, event.getSlot());
                }
                else {
                    ShopManager.BuyItem buy_item = ShopManager.BuyItem.getBuyItem(item);
                    if (buy_item != null) {
                        ShopManager.buyItem(player, buy_item);
                    }
                }
            }
            else if (inv.getTitle().equalsIgnoreCase(ChatColor.BLACK + "チームアップデート")) {
                if (slot == 10) {
                    TeamManager.GameTeam p_team = TeamManager.joinTeam(player);
                    int update = TeamUpdateManager.updateTeam(p_team, TeamUpdateManager.TeamUpdate.SWARD);

                    if (update == 0) {
                        ChatUtil.sendMessage(player, "アップデートしました。");
                    }
                    else {
                        ChatUtil.sendMessage(player, "すでに上限です。");
                    }
                }

                event.setCancelled(true);
            }
        }
    }
}
