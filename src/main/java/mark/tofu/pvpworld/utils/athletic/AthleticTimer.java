package mark.tofu.pvpworld.utils.athletic;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class AthleticTimer {

    // プレイヤーごとに実行中のタスクを保存するマップ
    public static HashMap<Player, BukkitTask> tasks = new HashMap<>();

    // プレイヤーごとに経過時間を保存するマップ
    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    /**
     * プレイヤーのタイマーを開始します。
     *
     * @param player プレイヤー
     * @param plugin PvpWorldプラグインのインスタンス
     */
    public static void startTimer(Player player, PvpWorld plugin) {
        // 既にタイマーが動いていたら、一度停止してリセットする
        if (tasks.containsKey(player)) {
            AthleticTimer.stopTimer(player);
        }

        // プレイヤーの時間を0に初期化
        playerTimes.put(player, 0);

        // プレイヤー専用のBukkitRunnableを作成
        BukkitRunnable playerTask = new BukkitRunnable() {
            @Override
            public void run() {
                // playerTimesから現在の時間を取得（もし存在しなければ0）して1足す
                // このrun()が呼ばれる前にstopTimerで削除されてもエラーにならないよう getOrDefault を使用
                int elapsedTime = playerTimes.getOrDefault(player, 0) + 1;

                if (elapsedTime > 500) {
                    // 時間制限に達した場合
                    player.sendMessage(ChatColor.AQUA + "時間制限です!");

                    // stopTimerを呼んで、タスクのキャンセルとマップからの削除を行う
                    AthleticTimer.stopTimer(player);

                    return; // run()メソッドを終了（これ以上レベル設定などをしない）
                }

                // 時間とレベルを更新
                playerTimes.put(player, elapsedTime);
                player.setLevel(elapsedTime);
            }
        };

        // タスクを開始し（0ティック後から20ティック＝1秒ごと）、そのIDをtasksマップに保存
        BukkitTask task = playerTask.runTaskTimer(plugin, 0L, 20L);
        tasks.put(player, task);
    }


    /**
     * プレイヤーのタイマーを停止し、関連データをクリーンアップします。
     *
     * @param player プレイヤー
     */
    public static void stopTimer(Player player) {
        // プレイヤーのタスクがtasksマップに存在するか確認
        if (tasks.containsKey(player)) {
            // タスクIDを取得してキャンセル
            tasks.get(player).cancel();
        }

        // ‼️ 重要: タスクを停止したら、必ずマップからプレイヤーの情報を削除します
        tasks.remove(player);
        playerTimes.remove(player);

        // (任意) プレイヤーのレベル表示をリセット
        player.setLevel(0);
    }
}