package org.gomadango0113.blockwars.manager;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.gomadango0113.blockwars.BlockWars;
import org.gomadango0113.blockwars.event.ShopBuyEvent;
import org.gomadango0113.blockwars.util.ItemUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShopManager {

    private static final PluginManager plm = Bukkit.getPluginManager();

    public static void spawnShopVillager(Location location) {
        World world = location.getWorld();
        Villager villager = world.spawn(location, Villager.class);
        villager.setProfession(Villager.Profession.FARMER);
        villager.setAgeLock(true);
        villager.setCustomNameVisible(false);
        villager.setMetadata(ChatColor.AQUA + "BlockWars_Villager", new FixedMetadataValue(BlockWars.getInstance(), true));
    }

    public static void openShopMenu(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, ChatColor.BLACK + "ショップメニュー");

        inv.setItem(1, new ItemUtil(Material.GRASS).getItemStack("ブロック", null));
        inv.setItem(2, new ItemUtil(Material.WOOD_SWORD).getItemStack("剣", null));
        inv.setItem(3, new ItemUtil(Material.IRON_CHESTPLATE).getItemStack("装備", null));
        inv.setItem(4, new ItemUtil(Material.STONE_PICKAXE).getItemStack("ピッケル", null));
        inv.setItem(5, new ItemUtil(Material.BOW).getItemStack("弓矢", null));
        inv.setItem(6, new ItemUtil(Material.TNT).getItemStack("その他", null));

        if (page == 1) {
            inv.setItem(19, new ItemStack(Material.WOOL, 16));
            inv.setItem(20, new ItemStack(Material.STAINED_CLAY, 16));
            inv.setItem(21, new ItemUtil(Material.GLASS).getItemStack(null, null));
            inv.setItem(22, new ItemStack(Material.ENDER_STONE));
            inv.setItem(23, new ItemStack(Material.WOOD));
        }

        player.openInventory(inv);
    }

    public static boolean buyItem(Player player, BuyItem buy_item) {
        PlayerInventory inv = player.getInventory();

        if (canBuy(player, buy_item.getNeedItem(), buy_item.getReturnItem())) {
            if (player.getInventory().firstEmpty() == -1) {
                plm.callEvent(new ShopBuyEvent(player, 0, ShopBuyEvent.BuyResult.FULL, buy_item.getReturnItem()));
                return false;
            }
            else {
                inv.removeItem(buy_item.getNeedItem());
                inv.addItem(buy_item.getReturnItem());
                plm.callEvent(new ShopBuyEvent(player, 0, ShopBuyEvent.BuyResult.SUCCESS, buy_item.getReturnItem()));
                return true;
            }
        }
        return false;
    }

    public static boolean canBuy(Player player, ItemStack need_item, ItemStack buy_item) {
        int inv_amount = 0;
        for (ItemStack item : Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).collect(Collectors.toList())) {
            if (item.getType().equals(need_item.getType())) {
                inv_amount+=item.getAmount();

                if (inv_amount >= need_item.getAmount()) {
                    return true;
                }
            }
        }

        int rest = need_item.getAmount() - inv_amount;
        plm.callEvent(new ShopBuyEvent(player, rest, ShopBuyEvent.BuyResult.NOT_ENOUGH, buy_item));
        return false;
    }

    public enum BuyItem {
        WOOL(new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.WOOL, 16));

        private final ItemStack need_item;
        private final ItemStack return_item;

        BuyItem(ItemStack need_item, ItemStack return_item) {
            this.need_item = need_item;
            this.return_item = return_item;
        }

        public ItemStack getNeedItem() {
            return need_item;
        }

        public ItemStack getReturnItem() {
            return return_item;
        }

        public static BuyItem getBuyItem(ItemStack return_item) {
            BuyItem[] buy_items = BuyItem.values();
            return Arrays.stream(buy_items)
                    .filter(buyitem -> buyitem.getReturnItem().isSimilar(return_item))
                    .findFirst()
                    .orElse(null);
        }
    }
}
