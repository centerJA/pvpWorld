package mark.tofu.pvpworld.utils;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.util.Objects;

public class AthleticUtils {
    public static void startAthleticAction(Player player, PvpWorld plugin) throws IOException {
        player.sendMessage(ChatColor.AQUA + "アスレチックスタート!");
        for (PotionEffect effect: player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        if (Config.lobbyAthleticStart == null) {
            player.sendMessage("問題が発生しました");
        }
        Objects.requireNonNull(Config.lobbyAthleticStart.getWorld()).playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0, 10);
        AthleticTimer.startTimer(player, plugin);
    }


    public static void stopAthleticAction(Player player) {
        player.sendMessage("ゴールにつきました!おめでとうございます!");
        Objects.requireNonNull(Config.lobbyAthleticFinish.getWorld()).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
        Config.lobbyAthleticFinish.getWorld().playEffect(player.getLocation(), Effect.DRAGON_BREATH, 0, 2);
        String playerScore = String.valueOf(player.getLevel());
        int playerScoreInt = player.getLevel();
        player.sendTitle(ChatColor.AQUA + "おめでとう!", ChatColor.AQUA + playerScore + "秒でした", 20, 40, 20);
        AthleticTimer.stopTimer(player);
        player.sendMessage("あなたの記録は" + ChatColor.AQUA + playerScore + "秒でした!");
        if (playerScoreInt <= 40) {
            player.sendMessage(ChatColor.AQUA + "40秒を切るなんてすごいです!");
        }
        player.setLevel(0);
    }
}
