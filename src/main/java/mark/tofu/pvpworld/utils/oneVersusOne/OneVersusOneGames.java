package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.athletic.AthleticTimer;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunScheduledTimer;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Time;
import java.util.ArrayList;

public class OneVersusOneGames {
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

    public static void overlappingGames(String gameName, Player player) {
        player.sendMessage("あなたは他のゲーム: " + ChatColor.AQUA + gameName + ChatColor.WHITE + "に参加しています!");
        player.sendMessage("退出してから他のゲームに参加してください");
    }
}
