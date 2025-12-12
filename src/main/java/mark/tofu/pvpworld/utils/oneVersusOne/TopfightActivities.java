package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class TopfightActivities {
    public static World world = Bukkit.getWorld("pvpWorld");

    public static ArrayList<String> topfightQueueingList = new ArrayList<>();

    public static Location player1Location = new Location(world, 137.500, 16.500, -138.500, 180, 0),
                           player2Location = new Location(world, 137.500, 16.500, -160.500);

    public static void topfightStartAction(Player player, PvpWorld plugin) {
        for (String Playername: topfightQueueingList) {
            Objects.requireNonNull(Bukkit.getPlayer(Playername)).getInventory().setItem(8, null);
        }
        Objects.requireNonNull(Bukkit.getPlayer(topfightQueueingList.get(0))).teleport(player1Location);
        Objects.requireNonNull(Bukkit.getPlayer(topfightQueueingList.get(1))).teleport(player2Location);
        TimeUpTimer.startTimer(player, plugin, 300);
        Config.NoWalkList.addAll(topfightQueueingList);
        StartTimerUtils.startTimer(player, plugin, topfightQueueingList);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                OneVersusOneGames.noWalkRemoveAction(topfightQueueingList);
                for (String PlayerName: topfightQueueingList) {
                    Config.DoNotReceiveDamageList.remove(PlayerName);
                }
            }
        }, 100L);
    }

    public static void topfightCloseAction(Player player, PvpWorld plugin) {
        Config.DoNotReceiveDamageList.addAll(topfightQueueingList);
        Config.TeleportToLobbyList.addAll(topfightQueueingList);
        topfightQueueingList.remove(player.getName());
        player.sendTitle(ChatColor.RED + "敗北", ChatColor.YELLOW + "もう一度挑戦しよう", 0, 60, 0);
        Player winner = Bukkit.getPlayer(topfightQueueingList.get(0));
        if (winner == null) return;
        topfightQueueingList.remove(winner.getName());
        winner.sendTitle(ChatColor.GREEN + "勝利", ChatColor.YELLOW + "すごい!!", 0, 60, 0);
        OneVersusOneGames.gameCloseAction(plugin);
    }
}
