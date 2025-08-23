package mark.tofu.pvpworld.utils;

import org.bukkit.block.data.type.Sign;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SignSettings {
    public static void signAction(Sign sign, Player player, String[] lines) {
        if (Objects.equals(lines[0], "SpeedRunTest")) {
            if (player == null) return;
            SpeedRunAction.onHoldAction(player);
        }
    }
}
