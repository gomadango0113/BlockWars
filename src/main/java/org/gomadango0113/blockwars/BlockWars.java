package org.gomadango0113.blockwars;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockWars extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommand();
        registerListener();

        getLogger().info("[BlockWars] プラグインの準備ができました。");
    }

    @Override
    public void onDisable() {

    }

    public void registerCommand() {

    }

    public void registerListener() {
        PluginManager plm = getServer().getPluginManager();
    }

    public static BlockWars getInstance() {
        return getPlugin(BlockWars.class);
    }

}
