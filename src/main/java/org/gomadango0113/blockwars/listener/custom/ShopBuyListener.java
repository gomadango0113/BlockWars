package org.gomadango0113.blockwars.listener.custom;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.gomadango0113.blockwars.event.ShopBuyEvent;
import org.gomadango0113.blockwars.util.ChatUtil;

public class ShopBuyListener implements Listener {

    @EventHandler
    public void onBuy(ShopBuyEvent event) {
        ShopBuyEvent.BuyResult result = event.getResult();
        Player player = event.getPlayer();
        if (result == ShopBuyEvent.BuyResult.SUCCESS) {
            ChatUtil.sendMessage(player, "アイテムを購入しました。");
        }
        else if (result == ShopBuyEvent.BuyResult.FULL) {
            ChatUtil.sendMessage(player, "インベントリが満杯です。");
        }
        else if (result == ShopBuyEvent.BuyResult.NOT_ENOUGH){
            ChatUtil.sendMessage(player, "残り" + ChatColor.YELLOW + event.getRest() + ChatColor.RESET + "個必要です!");
        }
    }

}
