package org.gomadango0113.blockwars.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.gomadango0113.blockwars.manager.BlockManager;

public class BlockFromToListener implements Listener {

    @EventHandler
    public void onFrom(BlockFromToEvent event) {
        Material wars_block = BlockManager.getBlock();
        Block block = event.getBlock();

        if (wars_block == Material.DRAGON_EGG) {
            if (block.getType() == Material.DRAGON_EGG) {
                event.setCancelled(true);
            }
        }
    }
}
