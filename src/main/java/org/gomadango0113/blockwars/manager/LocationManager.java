package org.gomadango0113.blockwars.manager;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.gomadango0113.blockwars.BlockWars;

public class LocationManager {

    private static final BlockWars instance;
    private static final FileConfiguration config;
    private static final World world;

    static {
        instance = BlockWars.getInstance();
        config = instance.getConfig();
        world = GameManager.getWorld();
    }

    public static Location getLobby() {
        int x = config.getInt("location.lobby.x");
        int y = config.getInt("location.lobby.y");
        int z = config.getInt("location.lobby.z");

        return new Location(world, x, y, z);
    }

    public static Location getTeamSpawn(TeamManager.GameTeam team) {
        if (TeamManager.getGameTeam().contains(team)) {
            String low = team.toLower();

            int x = config.getInt("location." + low + ".spawn.x");
            int y = config.getInt("location." + low + ".spawn.y");
            int z = config.getInt("location." + low + ".spawn.z");

            return new Location(world, x, y, z);
        }
        else {
            throw new IllegalArgumentException("ゲームチーム以外のスポーン地点は取得できません。");
        }
    }

    public static Location getTeamBlock(TeamManager.GameTeam team) {
        if (TeamManager.getGameTeam().contains(team)) {
            String low = team.toLower();

            int x = config.getInt("location." + low + ".block.x");
            int y = config.getInt("location." + low + ".block.y");
            int z = config.getInt("location." + low + ".block.z");

            return new Location(world, x, y, z);
        }
        else {
            throw new IllegalArgumentException("ゲームチーム以外のブロックは取得できません。");
        }
    }

}
