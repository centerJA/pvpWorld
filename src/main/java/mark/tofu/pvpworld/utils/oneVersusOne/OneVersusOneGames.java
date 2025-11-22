package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.athletic.AthleticTimer;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunScheduledTimer;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Time;
import java.util.ArrayList;

public class OneVersusOneGames {
    public static ArrayList<String> sumoQueueingList;
    public static void sumoStartAction(Player player, PvpWorld plugin) {
        //teleport
        TimeUpTimer.startTimer(player, plugin, 300);
        Config.NoWalkList.addAll(sumoQueueingList);
        StartTimerUtils.startTimer(player, plugin);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() { //スタート
            @Override
            public void run() {
                noWalkRemoveAction();
            }
        }, 100L);
    }

    public static void sumoCloseAction(Player player, PvpWorld plugin) {
        Config.TeleportToLobbyList.addAll(sumoQueueingList);
        sumoQueueingList.remove(player.getName());
        player.sendTitle(ChatColor.RED + "敗北", ChatColor.YELLOW + "もう一度挑戦しよう", 0, 60, 0);
        Player winner = Bukkit.getPlayer(sumoQueueingList.get(0));
        if (winner == null) return;
        sumoQueueingList.remove(winner.getName());
        winner.sendTitle(ChatColor.GREEN + "勝利", ChatColor.YELLOW + "すごい!!", 0, 60, 0);
        gameCloseAction(plugin);
    }

    public static void noWalkRemoveAction() {
        for (String PlayerName: sumoQueueingList) {
            Config.NoWalkList.remove(PlayerName);
        }
    }

    public static void gameCloseAction(PvpWorld plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (String PlayerName: Config.TeleportToLobbyList) {
                    Player player = Bukkit.getPlayer(PlayerName);
                    if (player == null) return;
                    player.getInventory().clear();String playerName = player.getName();
                    StartTimerUtils.stopTimer(player);
                    TimeUpTimer.stopTimer(player);
                    player.teleport(Config.lobby);
                    player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
                }
                for (String PlayerName: Config.TeleportToLobbyList) {
                    Config.TeleportToLobbyList.remove(PlayerName);
                }
            }
        }, 60L);
    }

    public static void drawAction() {

    }
}
