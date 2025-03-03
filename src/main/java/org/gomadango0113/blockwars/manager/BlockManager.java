package org.gomadango0113.blockwars.manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.gomadango0113.blockwars.BlockWars;
import org.gomadango0113.blockwars.util.ChatUtil;

import java.util.HashMap;
import java.util.Map;

public class BlockManager {

    //plugin
    private static final BlockWars instance;
    private static final FileConfiguration config;

    static {
        //plugin
        instance = BlockWars.getInstance();
        config = instance.getConfig();
    }

    public static void breakBlock(BlockBreakEvent event, TeamManager.GameTeam team) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location loc = block.getLocation().clone();
        Location team_block = LocationManager.getTeamBlock(team);

        if (loc.equals(team_block)) {
            event.setCancelled(true);
            block.setType(Material.AIR);

            ChatUtil.sendGlobalMessage(player.getName() + "が" + team.getTeamString(false) + "のブロックを破壊しました。");
        }
    }

    public static boolean isBreak(TeamManager.GameTeam team) {
        Location block_loc = LocationManager.getTeamBlock(team);
        Block get_block = block_loc.getBlock();
        Material set_block = getBlock();

        //設定ブロックと違かったら、trueを返す
        return get_block.getType() != set_block;
    }

    public static Material getBlock() {
        String string_block = config.getString("game.block_type");
        return Material.valueOf(string_block);
    }

    public static boolean setBlock(Material material) {
        if (GameManager.getStatus() != GameManager.GameStatus.RUNNING) {
            config.set("game.block_type", material.name());
            instance.saveConfig();

            return true;
        }

        return false;
    }

}
