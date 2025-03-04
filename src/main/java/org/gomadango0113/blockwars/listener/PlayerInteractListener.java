package org.gomadango0113.blockwars.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.gomadango0113.blockwars.manager.BlockManager;

public class PlayerInteractListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block get_block = event.getClickedBlock();
        Location get_loc = get_block.getLocation();

        Material set_block = BlockManager.getBlock();

        if (get_block.getType() == set_block) {
            get_block.setType(set_block);
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
        }
    }

}
