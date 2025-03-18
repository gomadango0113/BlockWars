package org.gomadango0113.blockwars.manager;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.gomadango0113.blockwars.BlockWars;
import org.gomadango0113.blockwars.util.ItemUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TeamUpdateManager {

    private static final Map<TeamManager.GameTeam, Map<TeamUpdate, Integer>> team_update_map;
    private static BukkitTask task = null;

    static {
        team_update_map = new HashMap<>();

        for (TeamManager.GameTeam team : TeamManager.getActiveTeam()) {
            for (TeamUpdate update : TeamUpdate.values()) {
                Map<TeamUpdate, Integer> unlock_map = new HashMap<>();
                unlock_map.put(update, 0);
                team_update_map.put(team, unlock_map);
            }
        }

        if (task == null) {
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        PlayerInventory inv = player.getInventory();
                        TeamManager.GameTeam p_team = TeamManager.joinTeam(player);
                        Map<TeamUpdate, Integer> unlock_map = unlockUpdateMap(p_team);
                        for (Map.Entry<TeamUpdate, Integer> entry : unlock_map.entrySet()) {
                            if (entry.getKey() == TeamUpdate.SWARD) {
                                for (ItemStack itemStack : inv.getContents()) {
                                    if (itemStack != null) {
                                        if (itemStack.getType() == Material.DIAMOND_SWORD) {
                                            ItemMeta meta = itemStack.getItemMeta();
                                            meta.addEnchant(Enchantment.DAMAGE_ALL, entry.getValue(), true);
                                            itemStack.setItemMeta(meta);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(BlockWars.getInstance(), 0L, 20L);
        }
    }

    public static void spawnTeamShop(Location location) {
        World world = location.getWorld();
        Villager villager = world.spawn(location, Villager.class);
        villager.setProfession(Villager.Profession.FARMER);
        villager.setAgeLock(true);
        villager.setCustomName(ChatColor.AQUA + "チームアップデート");
        villager.setCustomNameVisible(true);
    }

    public static void openTeamUpdate(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, ChatColor.BLACK + "チームアップデート");

        TeamManager.GameTeam p_team = TeamManager.joinTeam(player);
        Map<TeamUpdate, Integer> unlock_map = team_update_map.get(p_team);

        inv.setItem(10, new ItemUtil(Material.IRON_SWORD).getItemStack("ダメージ増加（現在：" + unlock_map.get(TeamUpdate.SWARD) + ")" , Collections.singletonList("ダメージ増加をチームに付ける")));

        player.openInventory(inv);
    }

    public static int updateTeam(TeamManager.GameTeam team, TeamUpdate update) {
        Map<TeamUpdate, Integer> unlock_map = unlockUpdateMap(team);
        Integer now_level = unlock_map.get(update);
        int plus_level = now_level + 1;

        if (plus_level <= update.getMaxLevel()) {
            unlock_map.put(update, now_level + 1);
            return 0;
        }
        else {
            return 1;
        }
    }

    public static Map<TeamUpdate, Integer> unlockUpdateMap(TeamManager.GameTeam team) {
        return team_update_map.get(team);
    }

    public enum TeamUpdate {
        SWARD(new ItemStack(Material.DIAMOND, 10), 3);

        private final ItemStack need_item;
        private final int max_level;

        TeamUpdate(ItemStack need_item, int max_level) {
            this.need_item = need_item;
            this.max_level = max_level;
        }

        public ItemStack getNeedItem() {
            return need_item;
        }

        public int getMaxLevel() {
            return max_level;
        }
    }
}
