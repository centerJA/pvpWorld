package mark.tofu.pvpworld.utils.speedRun;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Objects;

import static mark.tofu.pvpworld.utils.lobbyAthletic.AthleticTimer.tasks;

public class SpeedRunTimerMulti {

    // プレイヤーごとの残り時間を管理
    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    /**
     * タイマーを開始する
     */
    public static void startTimer(Player player, PvpWorld plugin) {
        // すでに動いているタイマーがあれば止める
        stopTimer(player);

        // 初期時間を設定 (例: 16秒)
        playerTimes.put(player, 16);

        // 新しいタスクを作成して実行
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // プレイヤーがオフラインなら終了
                if (!player.isOnline()) {
                    stopTimer(player);
                    return;
                }

                // 現在の残り時間を取得して1秒減らす
                int currentTime = playerTimes.getOrDefault(player, 0);
                int nextTime = currentTime - 1;

                // 終了判定
                if (nextTime <= 0) {
                    player.setLevel(0);
                    player.sendMessage(ChatColor.RED + "タイムアップ！");
                    SpeedRunActionMulti.startAction();
                    stopTimer(player);
                    return;
                }

                // カウントダウン通知 (5秒以下)
                if (nextTime <= 5) {
                    sendMessage(player, nextTime);
                }

                // 経験値バーに残り時間を表示
                for (String PlayerName: SpeedRunActionMulti.multiPlayingList) {
                    Player player = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                    player.setLevel(nextTime);
                }

                // HashMapの数値を更新
                playerTimes.put(player, nextTime);
            }
        }.runTaskTimer(plugin, 0, 20);

        // tasksマップに保存 (AthleticTimer.tasksを流用)
        tasks.put(player, task);
    }

    /**
     * 外部から時間を追加するメソッド (今回のメイン修正)
     * @param player 対象のプレイヤー
     * @param seconds 追加する秒数
     */
    public static void addTime(Player player, int seconds) {
        if (playerTimes.containsKey(player)) {
            int newTime = playerTimes.get(player) + seconds;
            playerTimes.put(player, newTime);
            for (String PlayerName: SpeedRunActionMulti.multiPlayingList) {
                Player player1 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                player.setLevel(newTime);
            }
        }
    }

    /**
     * タイマーを停止し、データを削除する
     */
    public static void stopTimer(Player player) {
        if (tasks.containsKey(player)) {
            tasks.get(player).cancel();
            tasks.remove(player);
        }
        playerTimes.remove(player);
    }

    /**
     * メッセージとタイトルの送信
     */
    public static void sendMessage(Player player, int elapsedTime) {
        for (String PlayerName: SpeedRunActionMulti.multiPlayingList) {
            Player player1 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            player1.sendMessage(ChatColor.AQUA + String.valueOf(elapsedTime) + "秒!");
            player1.sendTitle(ChatColor.RED + String.valueOf(elapsedTime), "", 0, 20, 0);
        }
    }
}