package org.gomadango0113.blockwars.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;
import org.gomadango0113.blockwars.manager.TeamManager;
import org.gomadango0113.blockwars.util.ChatUtil;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TeamCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blockwars_team")) {
            if (args.length == 0) {

            }
            else {
                if (args[0].equalsIgnoreCase("random")) {
                    TeamManager.teamRandom();
                    ChatUtil.sendMessage(send, "チームを割り当てました。");
                }
                else if (args[0].equalsIgnoreCase("random_join")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    TeamManager.leaveTeam(player);
                    TeamManager.GameTeam join_team = TeamManager.joinTeam(player);
                    ChatUtil.sendMessage(send, String.format("プレイヤーを%sに割り当てました。", join_team.getTeamString(false)));
                }
                else if (args[0].equalsIgnoreCase("join")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    TeamManager.GameTeam team = TeamManager.GameTeam.valueOf(args[2]);

                    if (team == TeamManager.GameTeam.ADMIN) {
                        ChatUtil.sendMessage(send, "このコマンドでは運営モードにすることができません。");
                    }
                    else {
                        TeamManager.GameTeam get_team = TeamManager.getJoinTeam(player);
                        if (get_team == TeamManager.GameTeam.UNKNOWN) {
                            TeamManager.joinTeam(team, player);
                            ChatUtil.sendMessage(send, String.format("プレイヤーを%sに参加させました。", team.getTeamString(false)));
                        }
                        else {
                            ChatUtil.sendMessage(send, String.format("プレイヤーを%sから%sに移動させました。", get_team.getTeamString(false), team.getTeamString(false)));
                            TeamManager.leaveTeam(player);
                            TeamManager.joinTeam(team, player);
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase("leave")) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    TeamManager.leaveTeam(player);
                    ChatUtil.sendMessage(send, "プレイヤーをチームから離脱させました。");
                }
                else if (args[0].equalsIgnoreCase("list")) {
                    Map<TeamManager.GameTeam, Team> teams = TeamManager.getTeamToBoardTeamMap();

                    ChatUtil.sendMessage(send, "チーム一覧");
                    StringBuilder list = new StringBuilder();

                    for (Map.Entry<TeamManager.GameTeam, Team> entry : teams.entrySet()) {
                        list.setLength(0);
                        TeamManager.GameTeam team = entry.getKey();
                        Team board_team = entry.getValue();
                        Set<OfflinePlayer> team_player = board_team.getPlayers();
                        for (OfflinePlayer offlineplayer : team_player) {
                            String status = offlineplayer.isOnline() ? ChatColor.GREEN + "(オンライン)" : ChatColor.GRAY + "(オフライン)";
                            list.append(offlineplayer.getName()).append(status).append(",");
                        }
                        Serializable team_string = team_player.isEmpty() ? "なし" : list.toString();
                        ChatUtil.sendMessage(send, team.getTeamString(true) + team_string);
                    }
                }
                else if (args[0].equalsIgnoreCase("empty")) {
                    if (args.length == 1) {
                        TeamManager.teamEmpty(null);
                        ChatUtil.sendMessage(send, "全チームを空にしました。");
                    }
                    else {
                        TeamManager.GameTeam team = TeamManager.GameTeam.valueOf(args[1]);
                        TeamManager.teamEmpty(team);
                        ChatUtil.sendMessage(send, "チームを空にしました。");
                    }
                }
                else if (args[0].equalsIgnoreCase("size")) {
                    if (args.length == 1) {
                        ChatUtil.sendMessage(send, "現在のチーム数は" + ChatColor.YELLOW + TeamManager.getTeamSize() + ChatColor.RESET + "です。");
                    }
                    else {
                        int size = Integer.parseInt(args[1]);
                        TeamManager.setTeamSize(size);
                        ChatUtil.sendMessage(send, "チーム数を変更しました。");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender send, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blockwars_team")){
            List<String> gameteam_list = TeamManager.getGameTeam().stream().map(TeamManager.GameTeam::name).collect(Collectors.toList());

            if (args.length == 1) {
                return Arrays.asList("join", "leave", "list", "empty", "random", "size");
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave")) {
                    return Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
                }
                if (args[0].equalsIgnoreCase("empty")) {
                    return gameteam_list;
                }
                if (args[0].equalsIgnoreCase("size")) {
                    return Arrays.asList("2", "3", "4");
                }
            }

            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("join")) {
                    return gameteam_list;
                }
            }
        }
        return Collections.emptyList();
    }

}
