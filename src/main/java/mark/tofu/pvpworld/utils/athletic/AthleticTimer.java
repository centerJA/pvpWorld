package mark.tofu.pvpworld.utils.athletic;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class AthleticTimer {
    private static BukkitRunnable timerTask;

    public static int athleticTime;

    public static HashMap<Player, BukkitTask> tasks = new HashMap<>();

    public static HashMap<Player, Integer> playerTimes = new HashMap<>();

    public static void startTimer(Player player, PvpWorld plugin) {
        if(playerTimes.containsKey(player)) {
            AthleticTimer.stopTimer(player);
        }
        playerTimes.put(player, 0);
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                int elapsedTime = playerTimes.get(player) + 1;
                if (elapsedTime > 2000) {
                    //cancel
                    AthleticTimer.getTaskId(player).cancel();
                    stopTimer(player);
                    return;
                }
                playerTimes.put(player, elapsedTime);
                player.setLevel(elapsedTime);
            }
        };
        tasks.put(player, timerTask.runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), 0, 20));
        athleticTime = 0;
    }


    public static BukkitTask getTaskId(Player player) {
        return PvpWorld.getPlugin(PvpWorld.class).getServer().getScheduler().runTaskTimer(PvpWorld.getPlugin(PvpWorld.class), new Runnable() {
            @Override
            public void run() {
                if (athleticTime > 2000) {
                    player.sendMessage(ChatColor.AQUA + "時間制限です!");
                    return;
                }
                player.setLevel(athleticTime);
                athleticTime++;
            }

        }, 0, 10);
    }

    public static void stopTimer(Player player) {
        if(tasks.get(player) != null) {
            tasks.get(player).cancel();
        }
    }
 }
