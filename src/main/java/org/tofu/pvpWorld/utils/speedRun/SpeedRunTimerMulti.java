package org.tofu.pvpWorld.utils.speedRun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.titleMaker;

import java.util.ArrayList;
import java.util.HashMap;

import static org.tofu.pvpWorld.utils.lobbyAthletic.AthleticTimer.tasks;

public class SpeedRunTimerMulti {

    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    public static void startTimer(Player starter, PvpWorld plugin) {
        stopTimer(SpeedRunActionMulti.multiPlayingList);

        playerTimes.put(starter, 16);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!starter.isOnline()) {
                    this.cancel();
                    stopTimer(SpeedRunActionMulti.multiPlayingList);
                    return;
                }

                int currentTime = playerTimes.getOrDefault(starter, 0);
                int nextTime = currentTime - 1;

                if (nextTime <= 0) {
                    this.cancel();
                    // 【修正】新タイマー開始の「前」にカウントダウンタスクを片付ける
                    stopTimer(SpeedRunActionMulti.multiPlayingList);
                    SpeedRunActionMulti.startAction(plugin);
                    return;
                }

                if (nextTime <= 5) {
                    sendMessage(nextTime);
                }

                for (String playerName : SpeedRunActionMulti.multiPlayingList) {
                    Player p = Bukkit.getPlayerExact(playerName);
                    if (p != null) {
                        p.setLevel(nextTime);
                    }
                }

                playerTimes.put(starter, nextTime);
            }
        }.runTaskTimer(plugin, 0, 20);

        tasks.put(starter, task);
    }

    public static void addTime(Player starter, int seconds) {
        if (playerTimes.containsKey(starter)) {
            int newTime = playerTimes.get(starter) + seconds;
            playerTimes.put(starter, newTime);

            for (String playerName : SpeedRunActionMulti.multiPlayingList) {
                Player p = Bukkit.getPlayerExact(playerName);
                if (p != null) {
                    p.setLevel(newTime);
                }
            }
        }
    }

    public static void stopTimer(ArrayList<String> playerNames) {
        for (String playerName : playerNames) {
            Player p = Bukkit.getPlayerExact(playerName);
            if (p != null) {
                if (tasks.containsKey(p)) {
                    tasks.get(p).cancel();
                    tasks.remove(p);
                }
                playerTimes.remove(p);
            }
        }
    }

    public static void sendMessage(int elapsedTime) {
        for (String playerName : SpeedRunActionMulti.multiPlayingList) {
            Player p = Bukkit.getPlayerExact(playerName);
            if (p != null) {
                p.sendMessage(textComponent.parse("<aqua>" + elapsedTime + "秒!"));
                p.showTitle(titleMaker.title(textComponent.parse(String.valueOf(elapsedTime)), textComponent.parse(""), 0, 20 ,0));
            }
        }
    }
}