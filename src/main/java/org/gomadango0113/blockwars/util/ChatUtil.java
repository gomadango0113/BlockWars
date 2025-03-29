package org.gomadango0113.blockwars.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class ChatUtil {

    private static final String TEXT_INFO = ChatColor.AQUA + "[BlockWars] " + ChatColor.RESET;

    public static void sendGlobalMessage(String message) {
        Bukkit.broadcastMessage(TEXT_INFO + message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(TEXT_INFO + message);
    }

    public static void sendMessage(Set<OfflinePlayer> players, String message) {
        for (OfflinePlayer player : players) {
            if (player != null) {
                if (player.isOnline()) {
                    sendMessage(player.getPlayer(), message);
                }
            }
        }
    }

}
