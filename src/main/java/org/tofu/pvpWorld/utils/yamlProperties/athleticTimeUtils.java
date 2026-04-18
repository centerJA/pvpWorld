package org.tofu.pvpWorld.utils.yamlProperties;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class athleticTimeUtils {
    public static File playerLobbyAthleticTimeFile;
    public static FileConfiguration playerLobbyAthleticTimeData;

    public static List<Map.Entry<String, Integer>> entryList = new ArrayList<>();

    public static void lobbyAthleticSetUp(PvpWorld plugin) {
        playerLobbyAthleticTimeFile = new File(plugin.getDataFolder(), "playerAthleticTime.yml");
        if (!playerLobbyAthleticTimeFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                playerLobbyAthleticTimeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerLobbyAthleticTimeData = YamlConfiguration.loadConfiguration(playerLobbyAthleticTimeFile);
        sortEntries();
    }

    public static int getPlayerLobbyAthleticTime(Player player) {
        String uuid = player.getUniqueId().toString();
        if (playerLobbyAthleticTimeData.contains(uuid)) {
            return playerLobbyAthleticTimeData.getInt(uuid);
        } else {
            return 10000;
        }
    }

    public static void setPlayerLobbyAthleticTime(Player player, int playerScore, boolean hasForce) {
        String uuid = player.getUniqueId().toString();

        if (hasForce || getPlayerLobbyAthleticTime(player) > playerScore) {
            if (!hasForce) player.sendMessage("upload");

            playerLobbyAthleticTimeData.set(uuid, playerScore);

            try {
                playerLobbyAthleticTimeData.save(playerLobbyAthleticTimeFile);
                if (!hasForce) player.sendMessage("upload finish");
            } catch (IOException e) {
                e.printStackTrace();
            }

            sortEntries();
            TextDisplayUtils.latestRanking();
            ScoreBoardUtils.updateScoreBoard(player);

            if (!hasForce) player.sendMessage("updated");
        }
    }

    public static void sortEntries() {
        entryList.clear();
        for (String uuid : playerLobbyAthleticTimeData.getKeys(false)) {
            int time = playerLobbyAthleticTimeData.getInt(uuid);
            entryList.add(new AbstractMap.SimpleEntry<>(uuid, time));
        }
        entryList.sort(Map.Entry.comparingByValue());
    }

    public static Component getRanking(int x) {
        int index = x - 1;
        if (index >= 0 && index < entryList.size()) {
            Map.Entry<String, Integer> entry = entryList.get(index);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
            String playerName = offlinePlayer.getName();
            return textComponent.parse("<gold>" + x + "位 - <white>" + playerName + ": <red>" + entry.getValue());
        } else {
            return textComponent.parse("<gold>" + x + "位 - <white>N/A");
        }
    }
}