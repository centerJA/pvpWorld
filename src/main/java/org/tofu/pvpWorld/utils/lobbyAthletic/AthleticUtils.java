package org.tofu.pvpWorld.utils.lobbyAthletic;

import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.scoreBoard.ScoreBoardUtils;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.titleMaker;
import org.tofu.pvpWorld.utils.yamlProperties.athleticTimeUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.tofu.pvpWorld.utils.yamlProperties.athleticTimeUtils.setPlayerLobbyAthleticTime;

public class AthleticUtils {
    public static void startAthleticAction(Player player, PvpWorld plugin) throws IOException {
        player.sendMessage(textComponent.parse("<aqua>アスレチックスタート!"));
        for (PotionEffect effect: player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        if (Config.lobbyAthleticStart == null) {
            player.sendMessage(textComponent.parse("<white>問題が発生しました"));
        }
        Objects.requireNonNull(Config.lobbyAthleticStart.getWorld()).playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0, 10);
        AthleticTimer.startTimer(player, plugin);
    }


    public static void stopAthleticAction(Player player) {
        Map<String, Integer> TimesMap = new HashMap<>();
        for (PotionEffect effect: player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.sendMessage(textComponent.parse("<white>ゴールにつきました!おめでとうございます!"));
        Objects.requireNonNull(Config.lobbyAthleticFinish.getWorld()).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
        Config.lobbyAthleticFinish.getWorld().playEffect(player.getLocation(), Effect.DRAGON_BREATH, 0, 2);
        String playerScore = String.valueOf(player.getLevel());
        int playerScoreInt = player.getLevel();
        setPlayerLobbyAthleticTime(player, playerScoreInt, false);
        ScoreBoardUtils.updateScoreBoard(player);
        player.showTitle(titleMaker.title(textComponent.parse("<aqua>おめでとう!"), textComponent.parse("<aqua>" + playerScore + "秒でした!"), 1000, 2000, 1000));
        AthleticTimer.stopTimer(player);
        player.sendMessage(textComponent.parse("<white>あなたの記録は<aqua>" + playerScore + "秒<white>でした!"));
        if (playerScoreInt <= 40) {
            player.sendMessage(textComponent.parse("<aqua>40秒を切るなんてすごいです!"));
        }
        for (String playerName2 : athleticTimeUtils.playerLobbyAthleticTimeData.getKeys(false)) {
            int time = athleticTimeUtils.playerLobbyAthleticTimeData.getInt(playerName2);
            TimesMap.put(playerName2, time);
        }
        player.setLevel(0);
        if (ScoreBoardUtils.getSortedKey(TimesMap).equals(player.getName())) {
            player.sendMessage(textComponent.parse("<gold>あなたは1位です!!"));
        }
        for (String PlayerName: Config.WorldAllPlayerList) {
            ScoreBoardUtils.updateScoreBoard(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)));
        }
    }

    public static void clearAthleticTimes(Player player) {
        setPlayerLobbyAthleticTime(player, 10000, true);
        player.sendMessage(textComponent.parse("<white>あなたのスコアをリセットしました"));
    }

    public static void sendClearAthleticTimeRequest(Player player) {
        player.sendMessage(textComponent.parse("<yellow>注意!<white>本当にスコアをリセットしますか?"));
        player.sendMessage(textComponent.parse("<click:run_command:'/pvpworld actions lobbyAthletic clear'><red>[はい]</click>"));
    }
}
