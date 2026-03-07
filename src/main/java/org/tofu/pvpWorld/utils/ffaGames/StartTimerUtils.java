package org.tofu.pvpWorld.utils.ffaGames;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.titleMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static org.tofu.pvpWorld.utils.lobbyAthletic.AthleticTimer.tasks;

public class StartTimerUtils {

    private static BukkitRunnable timerTask;

    public static int ffaGamesTime;

    public static HashMap<Player, Integer>playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin, ArrayList<String> arrayList) {
        if (playerTimes.containsKey(player)) {
            StartTimerUtils.stopTimer(player);
        }
        playerTimes.put(player, 16);
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                int elapsedTime = playerTimes.get(player) - 1; //残り時間
                if (elapsedTime == 0) { //cancel
                    for (String PlayerName: arrayList) {
                        if (arrayList.equals(SpleefActivities.spleefQueueingList)) {
                            SpleefActivities.spleefStartAction(player, plugin);
                        }
                        Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).setLevel(0);
                    }
                    stopTimer(player);
                    StartTimerUtils.getTaskId(player).cancel();
                    return;
                }
                if (elapsedTime <= 5) {
                    for (String PlayerName: arrayList) {
                        player.sendMessage("3sc:" + arrayList);
                        sendMessage(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), elapsedTime);
                    }
                }
                playerTimes.put(player, elapsedTime);
                for (String PlayerName: arrayList) {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).setLevel(elapsedTime);
                }
            }
        };
        tasks.put(player, timerTask.runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), 0, 20));
    }

    public static BukkitTask getTaskId(Player player) {
        return PvpWorld.getPlugin(PvpWorld.class).getServer().getScheduler().runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), new Runnable() {
            @Override
            public void run() {
                if (ffaGamesTime == 0) {
                    player.sendMessage(textComponent.parse("<aqua>test"));
                    return;
                }
                player.setLevel(ffaGamesTime);
                ffaGamesTime--;
            }
        }, 0, 10);
    }
    public static void stopTimer(Player player) {
        if (tasks.get(player) != null) {
            tasks.get(player).cancel();
        }
    }

    public static void sendMessage(Player player, int elapsedTime) {
        player.sendMessage(textComponent.parse("<aqua>" + elapsedTime + "秒!"));
        player.showTitle(titleMaker.title(textComponent.parse(String.valueOf(elapsedTime)), textComponent.parse(""), 0, 20 ,0));
    }
}
