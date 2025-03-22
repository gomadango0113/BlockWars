package org.gomadango0113.blockwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.gomadango0113.blockwars.manager.BlockManager;
import org.gomadango0113.blockwars.manager.LocationManager;
import org.gomadango0113.blockwars.manager.TeamManager;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location loc = block.getLocation();

        Bukkit.broadcastMessage(block.getType().name());
        if (block.getType() == BlockManager.getBlock()) {
            TeamManager.GameTeam break_team = BlockManager.getLocationTeamBlock(loc);
            if (TeamManager.getActiveTeam().contains(break_team)) {
                BlockManager.breakBlock(event, break_team);
            }
        }
    }

}
