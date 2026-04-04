package org.tofu.pvpWorld.utils.scoreBoard;

import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.*;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.yamlProperties.athleticTimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

import static org.tofu.pvpWorld.utils.yamlProperties.athleticTimeUtils.getPlayerLobbyAthleticTime;
import static org.tofu.pvpWorld.utils.yamlProperties.coinUtils.getPlayerCoin;
import static org.tofu.pvpWorld.utils.yamlProperties.expUtils.getPlayerExp;

public class ScoreBoardUtils {
    public static Scoreboard createScoreBoard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return null;
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("lobby", Criteria.DUMMY, textComponent.parse("<b><yellow>==PVPWORLD=="));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        return scoreboard;
    }

    public static void updateScoreBoard(Player player) {
        File playerExpFile = new File("./playerExp.yml");
        File playerCoinFile = new File("./playerCoin.yml");
        YamlConfiguration playerExpYml = YamlConfiguration.loadConfiguration(playerExpFile);
        YamlConfiguration playerCoinYml = YamlConfiguration.loadConfiguration(playerCoinFile);
        Scoreboard scoreBoard = createScoreBoard();
        Map<String, Integer> TimesMap = new HashMap<>();
        if (scoreBoard == null) {
            player.sendMessage("error");
            return;
        }

        Objective objective = scoreBoard.getObjective("lobby");
        if (objective == null) return;

        Score score12 = objective.getScore("line12");
        score12.setScore(12);
        score12.customName(textComponent.parse("<u><aqua>" + player.getName() + "さん"));

        Score score11 = objective.getScore("line11");
        score11.setScore(11);
        score11.customName(Component.empty());

        Score score10 = objective.getScore("line10");
        score10.setScore(10);
        score10.customName(textComponent.parse("<white>EXP: <green>" + getPlayerExp(player)));

        Score score9 = objective.getScore("line9");
        score9.setScore(9);
        score9.customName(textComponent.parse("<white>Coin: <gold>" + getPlayerCoin(player)));

        Score score8 = objective.getScore("line8");
        score8.setScore(8);
        score8.customName(Component.empty());

        Score score7 = objective.getScore("line7");
        score7.setScore(7);
        score7.customName(textComponent.parse("<white>アスレチックのランキング"));

        Score score6 = objective.getScore("line6");
        score6.setScore(6);
        score6.customName(textComponent.parse("<white>あなたのスコア: " + getPlayerLobbyAthleticTime(player)));

        for (String playerName2 : athleticTimeUtils.playerLobbyAthleticTimeData.getKeys(false)) {
            int time = athleticTimeUtils.playerLobbyAthleticTimeData.getInt(playerName2);
            TimesMap.put(playerName2, time);
        }

        Score score5 = objective.getScore("line5");
        score5.setScore(5);
        score5.customName(textComponent.parse("<white>1番の人のスコア: <gold>" + getSortedInteger(TimesMap)));

        Score score4 = objective.getScore("line4");
        score4.setScore(4);
        score4.customName(textComponent.parse("<white>名前: <gold>" + getSortedKey(TimesMap)));

        Score score3 = objective.getScore("line3");
        score3.setScore(3);
        score3.customName(Component.empty());

        Score score2 = objective.getScore("line2");
        score2.setScore(2);
        score2.customName(textComponent.parse("<white>分からないことがあったら..."));

        Score score1 = objective.getScore("line1");
        score1.setScore(1);
        score1.customName(textComponent.parse("<aqua><bold>/pvpworld help"));

        player.setScoreboard(scoreBoard);
    }

    public static void removeScoreBoard(Player player) {
        ScoreboardManager manager = player.getServer().getScoreboardManager();
        if (manager == null) {
            player.sendMessage(textComponent.parse("<white>error"));
            return;
        }

        Scoreboard scoreboard = manager.getMainScoreboard();
        player.setScoreboard(scoreboard);
    }

    public static Integer getSortedInteger(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());
        return sortedEntries.getFirst().getValue();
    }

    public static String getSortedKey(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());
        String playerUUID = sortedEntries.getFirst().getKey();
        UUID uuid = UUID.fromString(playerUUID);
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}
