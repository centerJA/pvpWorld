package org.tofu.pvpWorld.utils.oneVersusOne;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.titleMaker;

import java.util.ArrayList;
import java.util.HashMap;

public class StartTimerUtils {
    public static HashMap<Player, BukkitTask> tasks = new HashMap<>();

    private static BukkitRunnable timerTask;

    public static int oneVersusOneTime;

    public static HashMap<Player, Integer>playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin, ArrayList<String> arrayList) {
        if (playerTimes.containsKey(player)) {
            stopTimer(player);
        }
        playerTimes.put(player, 6);
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                int elapsedTime = playerTimes.get(player) - 1;
                if (elapsedTime == 0) {
                    stopTimer(player);
                    StartTimerUtils.getTaskId(player);
                    return;
                }
                if (elapsedTime <= 5) {
                    for (String PlayerName: arrayList) {
                        Player player = Bukkit.getPlayer(PlayerName);
                        if (player == null) return;
                        player.showTitle(titleMaker.title(textComponent.parse("<red>" + elapsedTime), textComponent.parse(""), 0, 20 ,0));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
                    }
                }
                playerTimes.put(player, elapsedTime);
            }
        };
        tasks.put(player, timerTask.runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), 0, 20));
    }

    public static BukkitTask getTaskId(Player player) {
        return PvpWorld.getPlugin(PvpWorld.class).getServer().getScheduler().runTaskLater(PvpWorld.getPlugin(PvpWorld.class), new Runnable() {
            @Override
            public void run() {
                if (oneVersusOneTime == 0) {
                    player.sendMessage(textComponent.parse("<aqua>test"));
                    return;
                }
                oneVersusOneTime--;
            }
        }, 0);
    }

    public static void stopTimer(Player player) {
        if (tasks.get(player) != null) {
            tasks.get(player).cancel();
        }
    }
}
