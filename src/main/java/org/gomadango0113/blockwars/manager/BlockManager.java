package org.gomadango0113.blockwars.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Bed;
import org.bukkit.scheduler.BukkitRunnable;
import org.gomadango0113.blockwars.BlockWars;
import org.gomadango0113.blockwars.util.ChatUtil;

import java.util.*;

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

        if (block.getType() == getBlock()) {
            if (loc.equals(team_block)) {
                event.setCancelled(true);
                block.setType(Material.AIR);

                ChatUtil.sendGlobalMessage(player.getName() + "が" + team.getTeamString(false) + "のブロックを破壊しました。");

                new BukkitRunnable() {
                    int time = 30;
                    @Override
                    public void run() {
                        if (TeamManager.getTeamSize() == 2) {
                            ChatUtil.sendMessage(player, "ゲーム終了!!");
                            this.cancel();
                        }
                        else {
                            if (TeamManager.getDeadTeamList().isEmpty() || TeamManager.getDeadTeamList().size() == 1) {
                                if (time == 0) {
                                    Set<OfflinePlayer> leave_list = new HashSet<>(TeamManager.getTeamPlayers(team));
                                    TeamManager.getDeadTeamList().add(team);
                                    for (OfflinePlayer break_team_player : leave_list) {
                                        TeamManager.leaveTeam(break_team_player);

                                        if (break_team_player.isOnline()) {
                                            TeamManager.joinTeam(break_team_player.getPlayer());
                                        }
                                    }

                                    ChatUtil.sendMessage(player,  team.getTeamString(false) + "は、別チームへ移籍しました。");
                                    this.cancel();
                                }
                                else {
                                    if (loc.getBlock().getType() == BlockManager.getBlock()) {
                                        ChatUtil.sendMessage(player, "ブロックが再設置されました。");
                                        this.cancel();
                                    }

                                    if (time == 45 || time == 30 || time == 15) {
                                        ChatUtil.sendGlobalMessage(team.getTeamString(false) + "が破壊されるまで" + time + "秒。");
                                    }

                                    time--;
                                }
                            }
                            else {
                                ChatUtil.sendMessage(player, "ゲーム終了!!");
                                this.cancel();
                            }
                        }
                    }
                }.runTaskTimer(BlockWars.getInstance(), 0L, 20L);
            }
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

    public static TeamManager.GameTeam getLocationTeamBlock(Location location) {
        location = location.clone().add(0, 0, 0);

        for (TeamManager.GameTeam team : TeamManager.getGameTeam()) {
            if (location.equals(LocationManager.getTeamBlock(team))) {
                return team;
            }
        }

        return TeamManager.GameTeam.UNKNOWN;
    }

}
