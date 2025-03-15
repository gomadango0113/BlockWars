package org.gomadango0113.blockwars.manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.gomadango0113.blockwars.BlockWars;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneratorManager {

    private static final BlockWars instance;
    private static final FileConfiguration config;
    private static final World world;

    static {
        instance = BlockWars.getInstance();
        config = instance.getConfig();
        world = GameManager.getWorld();
    }

    public static void startGenerator() {
        List<Material> ingot_list = Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT,  Material.DIAMOND);
        Random random = new Random();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (TeamManager.GameTeam team : TeamManager.getActiveTeam()) {
                    Location loc = getLocation(team);

                    int random_int = random.nextInt(100);
                    if (random_int < 10) { //10%（0～9）
                        world.dropItemNaturally(loc, new ItemStack(Material.DIAMOND));
                    }
                    else if (random_int < 40) { //30%（10~39）
                        world.dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT));
                    }
                    else { //60%（40~99）
                        world.dropItemNaturally(loc, new ItemStack(Material.IRON_INGOT));
                    }
                }
            }
        }.runTaskTimer(BlockWars.getInstance(), 0L, 20L);
    }
    
    public static Location getLocation(TeamManager.GameTeam team) {
        int x = config.getInt("location." + team.toLower() + ".generator.x");
        int y = config.getInt("location." + team.toLower() + ".generator.y");
        int z = config.getInt("location." + team.toLower() + ".generator.z");

        return new Location(world, x, y, z);
    }

}
