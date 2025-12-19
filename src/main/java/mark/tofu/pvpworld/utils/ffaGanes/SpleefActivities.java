package mark.tofu.pvpworld.utils.ffaGanes;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class SpleefActivities {
    public static World world = Bukkit.getWorld("pvpWorld");

    public static ArrayList<String> spleefQueueingList = new ArrayList<>(),
                                    spleefPlayingList = new ArrayList<>();

    public static Location spawnPoint = new Location(world, 183.500, 4.500, -24.500);

    public static void spleefStartAction(Player player, PvpWorld plugin) {
        for (String PlayerName: spleefQueueingList) {
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).getInventory().setItem(8, null);
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(spawnPoint);
        }
        TimeUpTimer.startTimer(player, plugin, 600);
        StartTimerUtils.startTimer(player, plugin, spleefQueueingList);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (String PlayerName: spleefQueueingList) {
                    Config.DoNotReceiveDamageList.remove(PlayerName);
                    spleefPlayingList.addAll(spleefQueueingList);
                }
            }
        }, 100L);
    }

    public static void spleefCloseAction(Player player, PvpWorld plugin) {
        Config.DoNotReceiveDamageList.addAll(spleefQueueingList);
        Config.TeleportToLobbyList.addAll(spleefQueueingList);
    }

    public static void voidAction(Player player, PvpWorld plugin) {
        spleefPlayingList.remove(player.getName());
        winnerChecker(player, plugin);
    }

    public static void winnerChecker(Player player, PvpWorld plugin) {
        if (spleefPlayingList.size() == 1) {
            spleefCloseAction(player, plugin);
        }
    }

}
