package org.tofu.pvpWorld.utils.speedRun;

import net.kyori.adventure.text.Component;
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

    // プレイヤーごとの残り時間を管理
    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    /**
     * タイマーを開始する
     */
    public static void startTimer(Player starter, PvpWorld plugin) {
        // すでに動いているタイマーがあれば止める
        stopTimer(SpeedRunActionMulti.multiPlayingList);

        // 初期時間を設定 (例: 16秒)
        playerTimes.put(starter, 16);

        // 新しいタスクを作成して実行
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // プレイヤーがオフラインなら確実にタスクを終了
                if (!starter.isOnline()) {
                    this.cancel(); // 自身のタスクを確実にキャンセル
                    stopTimer(SpeedRunActionMulti.multiPlayingList);
                    return;
                }

                // 現在の残り時間を取得して1秒減らす
                int currentTime = playerTimes.getOrDefault(starter, 0);
                int nextTime = currentTime - 1;

                // 終了判定
                if (nextTime <= 0) {
                    this.cancel(); // 自身のタスクを確実にキャンセル
                    SpeedRunActionMulti.startAction(plugin);
                    stopTimer(SpeedRunActionMulti.multiPlayingList);
                    return;
                }

                // カウントダウン通知 (5秒以下)
                if (nextTime <= 5) {
                    sendMessage(nextTime); // starter引数は不要になったため除外
                }

                // 経験値バーに残り時間を表示 (Nullチェックを追加)
                for (String playerName : SpeedRunActionMulti.multiPlayingList) {
                    Player p = Bukkit.getPlayerExact(playerName);
                    if (p != null) {
                        p.setLevel(nextTime);
                    }
                }

                // HashMapの数値を更新
                playerTimes.put(starter, nextTime);
            }
        }.runTaskTimer(plugin, 0, 20);

        // tasksマップに保存 (AthleticTimer.tasksを流用)
        tasks.put(starter, task);
    }

    /**
     * 外部から時間を追加するメソッド
     * @param starter 対象のプレイヤー
     * @param seconds 追加する秒数
     */
    public static void addTime(Player starter, int seconds) {
        if (playerTimes.containsKey(starter)) {
            int newTime = playerTimes.get(starter) + seconds;
            playerTimes.put(starter, newTime);

            for (String playerName : SpeedRunActionMulti.multiPlayingList) {
                Player p = Bukkit.getPlayerExact(playerName);
                // 修正箇所: starterではなくリストから取得したプレイヤー p に対してsetLevelを行う
                if (p != null) {
                    p.setLevel(newTime);
                }
            }
        }
    }

    /**
     * タイマーを停止し、データを削除する
     */
    public static void stopTimer(ArrayList<String> playerNames) {
        for (String playerName : playerNames) {
            // NullPointerExceptionを避けるため、requireNonNullではなくnullチェックを使用
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

    /**
     * メッセージとタイトルの送信
     */
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