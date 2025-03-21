package org.gomadango0113.blockwars.manager;

import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
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
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShopManager {

    private static final PluginManager plm = Bukkit.getPluginManager();

    public static void spawnShopVillager(Location location) {
        World world = location.getWorld();
        Villager villager = world.spawn(location, Villager.class);
        villager.setProfession(Villager.Profession.FARMER);
        villager.setAgeLock(true);
        villager.setCustomName(ChatColor.AQUA + "ショップ");
        villager.setCustomNameVisible(true);
    }

    public static void openShopMenu(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, ChatColor.BLACK + "ショップメニュー");

        inv.setItem(1, new ItemUtil(Material.GRASS).getItemStack("ブロック", null));
        inv.setItem(2, new ItemUtil(Material.WOOD_SWORD).getItemStack("剣", null));
        inv.setItem(3, new ItemUtil(Material.IRON_CHESTPLATE).getItemStack("装備", null));
        inv.setItem(4, new ItemUtil(Material.STONE_PICKAXE).getItemStack("ツール", null));
        inv.setItem(5, new ItemUtil(Material.BOW).getItemStack("弓矢", null));
        inv.setItem(6, new ItemUtil(Material.TNT).getItemStack("その他", null));

        if (page == 1) {
            //ブロックメニュー
            inv.setItem(19, new ItemUtil(Material.WOOL, 16).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が16個必要です。")));
            inv.setItem(20, new ItemUtil(Material.STAINED_CLAY, 16).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(21, new ItemUtil(Material.GLASS).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(22, new ItemUtil(Material.ENDER_STONE).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(23, new ItemUtil(Material.WOOD).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
        }
        else if (page == 2) {
            //剣メニュー
            inv.setItem(19, new ItemUtil(Material.WOOD_SWORD).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(20, new ItemUtil(Material.STONE_SWORD).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(21, new ItemUtil(Material.IRON_SWORD).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(22, new ItemUtil(Material.DIAMOND_SWORD).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
        }
        else if (page == 3) {
            //装備メニュー
            inv.setItem(20, new ItemUtil(Material.DIAMOND_HELMET).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(29, new ItemUtil(Material.DIAMOND_CHESTPLATE).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(38, new ItemUtil(Material.DIAMOND_LEGGINGS).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(47, new ItemUtil(Material.DIAMOND_BOOTS).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
        }
        else if (page == 4) {
            //ツールメニュー
            inv.setItem(19, new ItemUtil(Material.WOOD_PICKAXE).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(20, new ItemUtil(Material.STONE_PICKAXE).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(21, new ItemUtil(Material.IRON_PICKAXE).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(22, new ItemUtil(Material.DIAMOND_PICKAXE).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
            inv.setItem(23, new ItemUtil(Material.GOLD_PICKAXE).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
        }
        else if (page == 5) {
            //弓矢メニュー
            inv.setItem(19, new ItemUtil(Material.BOW).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
        }
        else if (page == 6) {
            //その他メニュー
            inv.setItem(19, new ItemUtil(Material.TNT).getItemStack(null, Collections.singletonList(ChatColor.AQUA + "鉄が個必要です。")));
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

        //ブロック
        WOOL(BuyType.BLOCK, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.WOOL, 16)),
        STAINED_CLAY(BuyType.BLOCK, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.STAINED_CLAY, 16)),
        GLASS(BuyType.BLOCK, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.GLASS)),
        END_STONE(BuyType.BLOCK, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.ENDER_STONE)),
        WOOD(BuyType.BLOCK, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.WOOD)),

        //剣
        WOODEN_SWORD(BuyType.SWORD, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.WOOD_SWORD)),
        STONE_SWORD(BuyType.SWORD, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.STONE_SWORD)),
        IRON_SWORD(BuyType.SWORD, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.IRON_SWORD)),
        DIAMOND_SWORD(BuyType.SWORD, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.DIAMOND_SWORD)),

        //装備
        HELMET(BuyType.EQUIPMENT, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.DIAMOND_HELMET)),
        CHESTPLATE(BuyType.EQUIPMENT, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.DIAMOND_CHESTPLATE)),
        LEGGINGS(BuyType.EQUIPMENT, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.DIAMOND_LEGGINGS)),
        BOOTS(BuyType.EQUIPMENT, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.DIAMOND_BOOTS)),

        //ツール
        WOODEN_PICKAXE(BuyType.TOOL, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.WOOD_PICKAXE)),
        STONE_PICKAXE(BuyType.TOOL, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.STONE_PICKAXE)),
        IRON_PICKAXE(BuyType.TOOL, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.IRON_PICKAXE)),
        DIAMOND_PICKAXE(BuyType.TOOL, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.DIAMOND_PICKAXE)),
        GOLD_PICKAXE(BuyType.TOOL, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.GOLD_PICKAXE)),

        //弓矢
        BOW(BuyType.BOW, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.BOW)),

        //その他
        TNT(BuyType.OTHER, new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.TNT));

        private final BuyType type;
        private final ItemStack need_item;
        private final ItemStack return_item;

        BuyItem(BuyType type, ItemStack need_item, ItemStack return_item) {
            this.type = type;
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

        public static BuyItem getBuyItem(Material material) {
            BuyItem[] buy_items = BuyItem.values();
            return Arrays.stream(buy_items)
                    .filter(buyitem -> buyitem.getReturnItem().getType() == material)
                    .findFirst()
                    .orElse(null);
        }

        public BuyType getType() {
            return type;
        }
    }

    public enum BuyType {
        BLOCK,
        SWORD,
        TOOL,
        EQUIPMENT,
        BOW,
        OTHER,
    }
}
