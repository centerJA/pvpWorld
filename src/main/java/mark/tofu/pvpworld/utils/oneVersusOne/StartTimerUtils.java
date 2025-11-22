package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class StartTimerUtils {
    public static HashMap<Player, BukkitTask> tasks = new HashMap<>();

    private static BukkitRunnable timerTask;

    public static int oneVersusOneTime;

    public static HashMap<Player, Integer>playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin) {
        if (playerTimes.containsKey(player)) {
            stopTimer(player);
        }
        playerTimes.put(player, 6);
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                int elapsedTime = playerTimes.get(player) - 1;
                if (elapsedTime == 0) {
                    player.sendMessage("test888");
                    stopTimer(player);
                    StartTimerUtils.getTaskId(player);
                    return;
                }
                if (elapsedTime <= 5) {
                    player.sendTitle(ChatColor.RED + String.valueOf(elapsedTime), "", 0, 20, 0);
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
                    player.sendMessage(ChatColor.AQUA + "test");
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
