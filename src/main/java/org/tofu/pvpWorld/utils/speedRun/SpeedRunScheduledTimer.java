package org.tofu.pvpWorld.utils.speedRun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.tofu.pvpWorld.PvpWorld;

import java.util.HashMap;

public class SpeedRunScheduledTimer {

    // 【修正】AthleticTimer等の他のタスクと干渉しないよう独自のマップで管理
    public static HashMap<Player, BukkitTask> scheduledTasks = new HashMap<>();
    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin, boolean multi) {
        // 既存のタイマーが動いていれば確実に止める
        stopTimer(player);

        playerTimes.put(player, 11);

        BukkitTask timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    stopTimer(player);
                    return;
                }

                int elapsedTime = playerTimes.getOrDefault(player, 11) - 1; //残り時間

                if (elapsedTime <= 0) { //cancel
                    this.cancel();
                    player.setLevel(0);
                    if (multi) {
                        for (String PlayerName: SpeedRunActionMulti.multiPlayingList) {
                            Player player1 = Bukkit.getPlayerExact(PlayerName);
                            if (player1 != null) {
                                SpeedRunAction.randomEvent(player1, plugin);
                            }
                        }
                    } else {
                        SpeedRunAction.randomEvent(player, plugin);
                    }
                    // 新しいイベント発生後に再帰的にタイマーをスタート
                    SpeedRunScheduledTimer.startTimer(player, plugin, multi);
                    return;
                }

                playerTimes.put(player, elapsedTime);
                player.setLevel(elapsedTime);

                // 【修正】マルチプレイの場合、参加者全員のEXPバーを同期させる処理を追加
                if (multi) {
                    for (String playerName : SpeedRunActionMulti.multiPlayingList) {
                        Player p = Bukkit.getPlayerExact(playerName);
                        if (p != null) {
                            p.setLevel(elapsedTime);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);

        scheduledTasks.put(player, timerTask);
    }

    public static void stopTimer(Player player) {
        if (scheduledTasks.containsKey(player)) {
            scheduledTasks.get(player).cancel();
            scheduledTasks.remove(player);
        }
        playerTimes.remove(player);
    }
}