package mark.tofu.pvpworld.utils.scoreBoard;

import mark.tofu.pvpworld.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.util.*;

import static mark.tofu.pvpworld.utils.yamlProperties.athleticTimeUtils.getPlayerLobbyAthleticTime;
import static mark.tofu.pvpworld.utils.yamlProperties.coinUtils.getPlayerCoin;
import static mark.tofu.pvpworld.utils.yamlProperties.xpUtils.getPlayerExp;

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
        objective.getScore(ChatColor.UNDERLINE + playerName).setScore(12);

        objective.getScore(ChatColor.WHITE.toString() + ChatColor.RESET).setScore(11);

        String exp = ChatColor.WHITE + "EXP: " + ChatColor.GREEN + getPlayerExp(player);
        objective.getScore(exp).setScore(10);

        String coin = ChatColor.WHITE + "Coin: " + ChatColor.GOLD + getPlayerCoin(player);
        objective.getScore(coin).setScore(9);

        objective.getScore(ChatColor.WHITE.toString() + ChatColor.RESET + ChatColor.BLACK).setScore(8);

        String athletic = ChatColor.WHITE + "アスレチックのランキング";
        objective.getScore(athletic).setScore(7);

        String yourScore = ChatColor.WHITE + "あなたのスコア: " + getPlayerLobbyAthleticTime(player);
        objective.getScore(yourScore).setScore(6);

        for (String playerName2 : Config.playerLobbyAthleticTimeData.getKeys(false)) {
            int time = Config.playerLobbyAthleticTimeData.getInt(playerName2);
            TimesMap.put(playerName2, time);
        }

        String firstScore = ChatColor.WHITE + "1番の人のスコア: " +ChatColor.GOLD +  getSortedInteger(TimesMap);
        objective.getScore(firstScore).setScore(5);

        String firstPlayer = ChatColor.WHITE + "1番の人の名前: " + ChatColor.GOLD + getSortedKey(TimesMap);
        objective.getScore(firstPlayer).setScore(4);

        objective.getScore(ChatColor.WHITE.toString() + ChatColor.RESET + ChatColor.STRIKETHROUGH).setScore(3);

        String contact = ChatColor.WHITE + "分からないことがあったら...";
        objective.getScore(contact).setScore(2);

        String contact2 = ChatColor.AQUA + "" + ChatColor.BOLD + "/pvpworld help";
        objective.getScore(contact2).setScore(1);
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

    public static Integer getSortedInteger(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());
        return sortedEntries.get(0).getValue();
    }

    public static String getSortedKey(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());
        String playerUUID = sortedEntries.get(0).getKey();
        UUID uuid = UUID.fromString(playerUUID);
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}
