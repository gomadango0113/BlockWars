package org.gomadango0113.blockwars;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.gomadango0113.blockwars.command.GameStartCommand;
import org.gomadango0113.blockwars.command.ShopCommand;
import org.gomadango0113.blockwars.listener.PlayerEntityClickListener;
import org.gomadango0113.blockwars.listener.PlayerInteractListener;
import org.gomadango0113.blockwars.listener.PlayerInventoryClickListener;
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
    }

    public void registerListener() {
        PluginManager plm = getServer().getPluginManager();

        plm.registerEvents(new PlayerInteractListener(), this);
        plm.registerEvents(new PlayerEntityClickListener(), this);
        plm.registerEvents(new PlayerInventoryClickListener(), this);
        plm.registerEvents(new ShopBuyListener(), this);
    }

    public static BlockWars getInstance() {
        return getPlugin(BlockWars.class);
    }

}
