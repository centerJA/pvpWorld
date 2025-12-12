package mark.tofu.pvpworld.utils.ffaGanes;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.oneVersusOne.StartTimerUtils;
import mark.tofu.pvpworld.utils.oneVersusOne.TimeUpTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class FfaGames {
    public static void gameCloseAction(PvpWorld plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (String PlayerName: Config.TeleportToLobbyList) {
                    Player player = Bukkit.getPlayer(PlayerName);
                    if (player == null) return;
                    player.getInventory().clear();
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

    public static void queueingActivities(Player player, InventoryClickEvent e, PvpWorld plugin, ArrayList<String> arrayList) {

    }

    public static void noWalkRemoveAction(ArrayList<String> arrayList) {
        for (String PlayerName: arrayList) {
            Config.NoWalkList.remove(PlayerName);
        }
    }
}
