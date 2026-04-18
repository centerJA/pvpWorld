package org.tofu.pvpWorld.utils.yamlProperties;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.scoreBoard.ScoreBoardUtils;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tofu.pvpWorld.utils.textComponent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class coinUtils {
    public static File playerCoinDataFile;
    public static FileConfiguration playerCoinData;

    public static List<Map.Entry<String, Integer>> entryList = new ArrayList<>();

    public static void playerCoinSetUp(PvpWorld plugin) {
        playerCoinDataFile = new File(plugin.getDataFolder(), "playerCoin.yml");
        if (!playerCoinDataFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                playerCoinDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerCoinData = YamlConfiguration.loadConfiguration(playerCoinDataFile);
        sortEntries();
    }

    public static int getPlayerCoin(Player player) {
        if (playerCoinData.contains(String.valueOf(player.getUniqueId()))) {
            return playerCoinData.getInt(String.valueOf(player.getUniqueId()), 0);
        } else {
            return 0;
        }
    }

    public static void playerSetCoin(Player player, int coin) {
        int finalScore = getPlayerCoin(player) + coin;
        playerCoinData.set(String.valueOf(player.getUniqueId()), finalScore);
        try {
            playerCoinData.save(playerCoinDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sortEntries();
        TextDisplayUtils.latestRanking();

        player.sendMessage(textComponent.parse("<gold>+" + coin + "Coin"));
        ScoreBoardUtils.updateScoreBoard(player);
    }

    public static void sortEntries() {
        entryList.clear();
        for (String uuid : playerCoinData.getKeys(false)) {
            int coin = playerCoinData.getInt(uuid);
            entryList.add(new AbstractMap.SimpleEntry<>(uuid, coin));
        }
        entryList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    }

    public static Component getRanking(int x) {
        int index = x - 1;
        if (index >= 0 && index < entryList.size()) {
            Map.Entry<String, Integer> entry = entryList.get(index);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
            String playerName = offlinePlayer.getName();
            return textComponent.parse("<gold>" + x + "位 - <white>" + playerName + ": <gold>" + entry.getValue());
        } else {
            return textComponent.parse("<gold>" + x + "位 - <white>N/A");
        }
    }
}