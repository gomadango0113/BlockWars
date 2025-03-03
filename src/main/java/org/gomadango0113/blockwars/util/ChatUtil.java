package org.gomadango0113.blockwars.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtil {

    private static final String TEXT_INFO = ChatColor.AQUA + "[BlockWars] " + ChatColor.RESET;

    public static void sendGlobalMessage(String message) {
        Bukkit.broadcastMessage(TEXT_INFO + message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(TEXT_INFO + message);
    }

}
