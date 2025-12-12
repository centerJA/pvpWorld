package mark.tofu.pvpworld.utils.ffaGanes;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.oneVersusOne.SumoActivities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class StartTimerUtils {
    public static HashMap<Player, BukkitTask> tasks = new HashMap<>();

    private static BukkitRunnable timerTask;

    public static int ffaGamesTime;

    public static HashMap<Player, Integer>playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin, ArrayList<String> arrayList) {
        if (playerTimes.containsKey(player)) {
            stopTimer(player);
        }
        playerTimes.put(player, 6);
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                int elapsedTime = playerTimes.get(player) - 1;
                if (elapsedTime == 0) {
                    stopTimer(player);
                    mark.tofu.pvpworld.utils.oneVersusOne.StartTimerUtils.getTaskId(player);
                    return;
                }
                if (elapsedTime <= 5) {
                    for (String PlayerName: arrayList) {
                        Player player = Bukkit.getPlayer(PlayerName);
                        if (player == null) return;
                        player.sendTitle(ChatColor.RED + String.valueOf(elapsedTime), "", 0, 20, 0);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
                    }
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
