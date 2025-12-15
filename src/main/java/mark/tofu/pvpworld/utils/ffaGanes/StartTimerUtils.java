package mark.tofu.pvpworld.utils.ffaGanes;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.oneVersusOne.SumoActivities;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class StartTimerUtils {
    public static HashMap<Player, BukkitTask> tasks = new HashMap<>();

    private static BukkitRunnable timerTask;

    public static int ffaGamesTime;

    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin, ArrayList<String> arrayList) {
        if (playerTimes.containsKey(player)) {
            stopTimer(player);
        }
        playerTimes.put(player, 21);
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                int elapsedTime = playerTimes.get(player) - 1;
                if (elapsedTime == 0) {
                    stopTimer(player);
                    StartTimerUtils.getTaskId(player);
                    return;
                }
                for (String PlayerName: arrayList) {
                    ScoreBoardUtils.setFfaScoreBoard(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)), elapsedTime, true, arrayList);
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
                if (ffaGamesTime == 0) {
                    player.sendMessage(ChatColor.AQUA + "test");
                    return;
                }
                ffaGamesTime--;
            }
        }, 0);
    }

    public static void stopTimer(Player player) {
        if (tasks.get(player) != null) {
            tasks.get(player).cancel();
        }
    }
}
