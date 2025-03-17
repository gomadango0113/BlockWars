package org.gomadango0113.blockwars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.gomadango0113.blockwars.manager.ShopManager;
import org.gomadango0113.blockwars.util.ChatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShopCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blockwars_shop")) {
            Player player = (Player) send;
            if (args[0].equalsIgnoreCase("spawn")) {
                ShopManager.spawnShopVillager(player.getLocation());
                ChatUtil.sendMessage(player, "ショップ村人をスポーンさせました。");
            }
            else if (args[0].equalsIgnoreCase("open")) {
                ShopManager.openShopMenu(player, 1);
                ChatUtil.sendMessage(player, "ショップを開きました。");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender send, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blockwars_shop")) {
            if (args.length == 1) {
                return Stream.of("spawn", "open").filter(tab -> tab.startsWith(args[0])).collect(Collectors.toList());
            }
        }
        return null;
    }

}
