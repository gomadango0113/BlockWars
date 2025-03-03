package org.gomadango0113.blockwars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gomadango0113.blockwars.manager.GameManager;

public class GameStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender send, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blockwars_start")) {
            GameManager.startGame();
        }
        return false;
    }

}
