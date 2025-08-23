package mark.tofu.pvpworld.utils;

import mark.tofu.pvpworld.Config;
import org.bukkit.entity.Player;

public class SpeedRunAction {
    public static void onHoldAction(Player player) {
        player.teleport(Config.speedRunOnholdRoom);
        Config.SpeedRunOnHoldList.add(player.getName());
        if (Config.SpeedRunOnHoldList.size() == 1) {

        }
    }
}
