package mark.tofu.pvpworld.utils.athletic;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import mark.tofu.pvpworld.utils.yamlProperties.athleticTimeUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import javax.xml.soap.Text;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static mark.tofu.pvpworld.utils.yamlProperties.athleticTimeUtils.playerLobbyAthleticTimeData;
import static mark.tofu.pvpworld.utils.yamlProperties.athleticTimeUtils.setPlayerLobbyAthleticTime;

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
        Map<String, Integer> TimesMap = new HashMap<>();
        for (PotionEffect effect: player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.sendMessage("ゴールにつきました!おめでとうございます!");
        Objects.requireNonNull(Config.lobbyAthleticFinish.getWorld()).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
        Config.lobbyAthleticFinish.getWorld().playEffect(player.getLocation(), Effect.DRAGON_BREATH, 0, 2);
        String playerScore = String.valueOf(player.getLevel());
        int playerScoreInt = player.getLevel();
        setPlayerLobbyAthleticTime(player, playerScoreInt, false);
        ScoreBoardUtils.updateScoreBoard(player);
        player.sendTitle(ChatColor.AQUA + "おめでとう!", ChatColor.AQUA + playerScore + "秒でした", 20, 40, 20);
        AthleticTimer.stopTimer(player);
        player.sendMessage("あなたの記録は" + ChatColor.AQUA + playerScore + "秒でした!");
        if (playerScoreInt <= 40) {
            player.sendMessage(ChatColor.AQUA + "40秒を切るなんてすごいです!");
        }
        for (String playerName2 : athleticTimeUtils.playerLobbyAthleticTimeData.getKeys(false)) {
            int time = athleticTimeUtils.playerLobbyAthleticTimeData.getInt(playerName2);
            TimesMap.put(playerName2, time);
        }
        player.setLevel(0);
        if (ScoreBoardUtils.getSortedKey(TimesMap).equals(player.getName())) {
            player.sendMessage(ChatColor.GOLD + "あなたは1位です!!");
        }
        for (String PlayerName: Config.WorldAllPlayerList) {
            ScoreBoardUtils.updateScoreBoard(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)));
        }
    }

    public static void clearAthleticTimes(Player player) {
        setPlayerLobbyAthleticTime(player, 10000, true);
        player.sendMessage("あなたのスコアをリセットしました");
    }

    public static void sendClearAthleticTimeRequest(Player player) {
        player.sendMessage(ChatColor.YELLOW + "注意!" + ChatColor.WHITE + "本当にスコアをリセットしますか?");
        TextComponent yes = new TextComponent(ChatColor.RED + "[はい]");
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pvpworld actions lobbyAthletic clear"));
        player.spigot().sendMessage(yes);
    }

    public static void scheduledAthleticTimeClear() {
        if (Config.systemConfigGetItem("athleticTime") != null && Config.systemConfigGetItem("athleticTime").equals("true")) {
            //allclearmethod
//            if (!athleticTimeUtils.playerLobbyAthleticTimeFile.exists()) return;
//            if (athleticTimeUtils.playerLobbyAthleticTimeFile.delete()) {
//                System.out.println("Successfully deleted");
//            }
//            athleticTimeUtils.playerLobbyAthleticTimeFile =
//            setPlayerLobbyAthleticTime(playerLobbyAthleticTimeData);
//        } else if (Config.systemConfigGetItem("athleticTime").equals("false"))}
        }
    }
}
