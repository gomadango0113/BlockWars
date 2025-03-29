package org.gomadango0113.blockwars;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.gomadango0113.blockwars.command.GameStartCommand;
import org.gomadango0113.blockwars.command.ShopCommand;
import org.gomadango0113.blockwars.command.TeamCommand;
import org.gomadango0113.blockwars.listener.*;
import org.gomadango0113.blockwars.listener.custom.ShopBuyListener;
import org.gomadango0113.blockwars.manager.ScoreboardManager;

public final class BlockWars extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommand();
        registerListener();
        ScoreboardManager.setScoreboard(0);

        getLogger().info("[BlockWars] プラグインの準備ができました。");
    }

    @Override
    public void onDisable() {

    }

    public void registerCommand() {
        getCommand("blockwars_start").setExecutor(new GameStartCommand());
        getCommand("blockwars_shop").setExecutor(new ShopCommand());
        getCommand("blockwars_teamupdate").setExecutor(new ShopCommand());
        getCommand("blockwars_team").setExecutor(new TeamCommand());
    }

    public void registerListener() {
        PluginManager plm = getServer().getPluginManager();

        plm.registerEvents(new PlayerInteractListener(), this);
        plm.registerEvents(new PlayerEntityClickListener(), this);
        plm.registerEvents(new PlayerInventoryClickListener(), this);
        plm.registerEvents(new BlockBreakListener(), this);
        plm.registerEvents(new BlockFromToListener(), this);
        plm.registerEvents(new ShopBuyListener(), this);
    }

    public static BlockWars getInstance() {
        return getPlugin(BlockWars.class);
    }

}
