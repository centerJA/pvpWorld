package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OneVersusOneGames {
    public static void sumoStartAction(Player player, PvpWorld plugin) {
        //teleport
        StartTimerUtils.startTimer(player, plugin);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() { //スタート
            @Override
            public void run() {

            }
        }, 100L);
    }
}
