package org.gomadango0113.blockwars.manager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.gomadango0113.blockwars.BlockWars;
import org.gomadango0113.blockwars.util.ChatUtil;

public class GameManager {

    private static GameStatus status;
    private static int game_time;
    private static World world;

    static {
        status = GameStatus.WAITING;
        game_time = 60*30;
        world = Bukkit.getWorlds().get(0);
    }

    public static void startGame() {
        final int[] counttime = {10};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (status == GameStatus.WAITING || status == GameStatus.COUNTING) {
                    status = GameStatus.COUNTING;

                    if (counttime[0] == 0) {
                        ChatUtil.sendGlobalMessage("ゲーム開始!");
                        ScoreboardManager.setScoreboard(1);
                        GeneratorManager.startGenerator();
                        status = GameStatus.RUNNING;
                    }
                    else {
                        ChatUtil.sendGlobalMessage("ゲーム開始まであと" + counttime[0] + "秒!");
                        counttime[0]--;
                    }
                }
                else if (status == GameStatus.RUNNING){
                    if (game_time == 0) {
                        ChatUtil.sendGlobalMessage("ゲーム終了!");
                        status = GameStatus.ENDING;
                    }
                    else {
                        game_time--;
                    }
                }
            }
        }.runTaskTimer(BlockWars.getInstance(), 0L, 20L);
    }

    public static void resetGame() {
        status = GameStatus.WAITING;
        game_time = 60*30;
        world = Bukkit.getWorlds().get(0);
    }

    public static int getTime() {
        return game_time;
    }

    public static GameStatus getStatus() {
        return status;
    }

    public static World getWorld() {
        return world;
    }

    public static void setWorld(World world) {
        GameManager.world = world;
    }

    public enum GameStatus {
        WAITING,
        COUNTING,
        RUNNING,
        ENDING;
    }

}
