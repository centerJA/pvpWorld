package mark.tofu.pvpworld.utils.speedRun;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

import static mark.tofu.pvpworld.utils.lobbyAthletic.AthleticTimer.tasks;

public class SpeedRunTimerMulti {
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
                    player.sendMessage("aaa");
                    //multistart
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
                    player.sendMessage(ChatColor.AQUA + "test");
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
        player.sendMessage(ChatColor.AQUA + String.valueOf(elapsedTime) + "秒!");
        player.sendTitle(String.valueOf(elapsedTime), "", 0, 20, 0);
    }
}
