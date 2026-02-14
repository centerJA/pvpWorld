package mark.tofu.pvpworld.utils.ffaGames;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.oneVersusOne.OneVersusOneGames;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunTimer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

import static mark.tofu.pvpworld.utils.lobbyAthletic.AthleticTimer.tasks;

public class TimeUpTimer {
    private static BukkitRunnable timerTask;

    public static int Time;

    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin, int time) {
        if (playerTimes.containsKey(player)) {
            SpeedRunTimer.stopTimer(player);
        }
        playerTimes.put(player, time);
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                int elapsedTime = playerTimes.get(player) - 1; //残り時間
                if (elapsedTime == 0) { //cancel
                    player.sendMessage("aaa");
                    OneVersusOneGames.timeUpAction(player, plugin);

                    stopTimer(player);
                    SpeedRunTimer.getTaskId(player).cancel();
                    return;
                }
                playerTimes.put(player, elapsedTime);
            }
        };
        tasks.put(player, timerTask.runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), 0, 20));
    }

    public static BukkitTask getTaskId(Player player) {
        return PvpWorld.getPlugin(PvpWorld.class).getServer().getScheduler().runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), new Runnable() {
            @Override
            public void run() {
                if (Time == 0) {
                    player.sendMessage(ChatColor.AQUA + "test");
                    return;
                }
                player.setLevel(Time);
                Time--;
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
