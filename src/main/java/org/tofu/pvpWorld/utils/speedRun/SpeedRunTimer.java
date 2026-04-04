package org.tofu.pvpWorld.utils.speedRun;

import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.titleMaker;

import java.util.HashMap;

import static org.tofu.pvpWorld.utils.lobbyAthletic.AthleticTimer.tasks;

public class SpeedRunTimer {
    private static BukkitRunnable timerTask;

    public static int speedRunTime;

    public static HashMap<Player, Integer>playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin) {
        if (playerTimes.containsKey(player)) {
            SpeedRunTimer.stopTimer(player);
        }
        playerTimes.put(player, 16);
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                int elapsedTime = playerTimes.get(player) - 1; //残り時間
                if (elapsedTime == 0) { //cancel
                    player.setLevel(0);
                    SpeedRunAction.startSingleMode(player, plugin);
                    Config.SpeedRunSingleOnHoldList.remove(player.getName());
                    stopTimer(player);
                    SpeedRunTimer.getTaskId(player).cancel();
                    return;
                }
                if (elapsedTime <= 5) {
                    sendMessage(player, elapsedTime);
                }
                playerTimes.put(player, elapsedTime);
                player.setLevel(elapsedTime);
            }
        };
        tasks.put(player, timerTask.runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), 0, 20));
    }

    public static BukkitTask getTaskId(Player player) {
        return PvpWorld.getPlugin(PvpWorld.class).getServer().getScheduler().runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), new Runnable() {
            @Override
            public void run() {
                if (speedRunTime == 0) {
                    player.sendMessage(textComponent.parse("<aqua>test"));
                    return;
                }
                player.setLevel(speedRunTime);
                speedRunTime--;
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
        player.showTitle(titleMaker.title(textComponent.parse(String.valueOf(elapsedTime)), textComponent.parse(""), 0, 1000 ,0));
    }
 }
