package mark.tofu.pvpworld.utils.scoreBoard;

import mark.tofu.pvpworld.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.swing.*;
import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreBoardUtils {
    public static Scoreboard createScoreBoard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return null;
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("lobby", "dummy", ChatColor.YELLOW + "" + ChatColor.BOLD + "==PVPWORLD==");
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

        String playerName = ChatColor.AQUA + player.getName() + "さん";
        objective.getScore(ChatColor.UNDERLINE + playerName).setScore(9);

        objective.getScore(ChatColor.WHITE.toString() + ChatColor.RESET).setScore(8);

        String exp = ChatColor.WHITE + "EXP: " + ChatColor.GREEN + Config.getPlayerExp(player);
        objective.getScore(exp).setScore(7);

        String coin = ChatColor.WHITE + "Coin: " + ChatColor.GOLD + Config.getPlayerCoin(player);
        objective.getScore(coin).setScore(6);

        objective.getScore(ChatColor.WHITE.toString() + ChatColor.RESET).setScore(5);

        String athletic = ChatColor.WHITE + "アスレチックのランキング";
        objective.getScore(athletic).setScore(4);

        String yourScore = ChatColor.WHITE + "あなたのスコア: " + Config.getPlayerLobbyAthleticTime(player);
        objective.getScore(yourScore).setScore(3);

        for (String playerName2 : Config.playerLobbyAthleticTimeData.getKeys(false)) {
            int time = Config.playerLobbyAthleticTimeData.getInt(playerName2);
            TimesMap.put(playerName2, time);
        }

        String firstScore = ChatColor.WHITE + "1番の人のスコア: " + getSortedEntries(TimesMap);
        objective.getScore(firstScore).setScore(2);

        player.setScoreboard(scoreBoard);
    }

    public static void removeScoreBoard(Player player) {
        ScoreboardManager manager = player.getServer().getScoreboardManager();
        if (manager == null) {
            player.sendMessage("error");
            return;
        }

        Scoreboard scoreboard = manager.getMainScoreboard();
        player.setScoreboard(scoreboard);
    }

    public static Map.Entry<String, Integer> getSortedEntries(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());
        return sortedEntries.get(0);
    }
}
