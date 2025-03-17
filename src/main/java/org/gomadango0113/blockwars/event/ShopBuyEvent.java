package org.gomadango0113.blockwars.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ShopBuyEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final int rest;
    private final ItemStack buy_item;
    private final BuyResult result;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ShopBuyEvent(Player player, int rest, BuyResult result, ItemStack buy_item) {
        this.player = player;
        this.rest = rest;
        this.buy_item = buy_item;
        this.result = result;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getBuyItem() {
        return buy_item;
    }

    public int getRest() {
        return rest;
    }

    public BuyResult getResult() {
        return result;
    }

    public String getResultMessage() {
        if (getResult() == BuyResult.SUCCESS) {
            return "アイテムを購入にしました。";
        }
        else if (getResult() == BuyResult.FULL) {
            return "インベントリが満杯です。";
        }
        else if (getResult() == BuyResult.NOT_ENOUGH){
            return "金が残り" + ChatColor.YELLOW + rest + ChatColor.RESET +"個必要です!";
        }
        return null;
    }

    public enum BuyResult {
        SUCCESS,
        FULL,
        NOT_ENOUGH,
        NO_ITEM
    }

}
