package org.gomadango0113.blockwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.gomadango0113.blockwars.manager.ShopManager;

public class PlayerEntityClickListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity click_entity = event.getRightClicked();

        if (click_entity instanceof Villager) {
            if (click_entity.getCustomName().equalsIgnoreCase(ChatColor.AQUA + "BlockWars_Villager")) {
                ShopManager.openShopMenu(player);
                event.setCancelled(true);
            }
        }
    }

}
