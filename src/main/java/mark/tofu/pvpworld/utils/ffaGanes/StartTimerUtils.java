package mark.tofu.pvpworld.utils.ffaGanes;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class StartTimerUtils {

    // プレイヤーごとのカウントダウン
    public static HashMap<Player, BukkitTask> tasks = new HashMap<>();
    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    // FFA開始タイマー（全体で1つ）
    private static BukkitTask ffaTask;
    private static int ffaGamesTime;

    // FFAが既に開始されたかどうか
    private static boolean ffaStarted = false;

    /**
     * 開始前カウントダウン（20秒）
     */
    public static void startTimer(Player player, PvpWorld plugin, ArrayList<String> arrayList) {

        stopTimer(player);
        playerTimes.put(player, 20);

        BukkitRunnable timerTask = new BukkitRunnable() {
            @Override
            public void run() {

                Integer time = playerTimes.get(player);
                if (time == null) {
                    cancel();
                    return;
                }

                if (time <= 0) {
                    stopTimer(player);
                    startFfaGameTimer(player, plugin);
                    return;
                }

                for (String name : arrayList) {
                    Player target = Bukkit.getPlayer(name);
                    if (target != null) {
                        ScoreBoardUtils.setFfaScoreBoard(target, time, true, arrayList);
                    }
                }

                playerTimes.put(player, time - 1);
            }
        };

        tasks.put(player, timerTask.runTaskTimer(plugin, 0L, 20L));
    }

    /**
     * FFA開始タイマー（1回のみ）
     */
    private static void startFfaGameTimer(Player player, PvpWorld plugin) {

        // すでに開始していたら何もしない
        if (ffaStarted) return;
        ffaStarted = true;

        ffaGamesTime = 5;

        ffaTask = new BukkitRunnable() {
            @Override
            public void run() {

                if (ffaGamesTime <= 0) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "test111G");
                    SpleefActivities.spleefStartAction(player, plugin);
                    cancel();
                    ffaTask = null;
                    return;
                }

                ffaGamesTime--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * タイマー停止
     */
    public static void stopTimer(Player player) {
        BukkitTask task = tasks.remove(player);
        if (task != null) {
            task.cancel();
        }
        playerTimes.remove(player);
    }
}
