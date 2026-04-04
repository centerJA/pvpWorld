package org.tofu.pvpWorld.utils.yamlProperties;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
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
                playerLobbyAthleticTimeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerLobbyAthleticTimeData = YamlConfiguration.loadConfiguration(playerLobbyAthleticTimeFile);
        sortEntries();
    }

    public static int getPlayerLobbyAthleticTime(Player player) {
        if (playerLobbyAthleticTimeData.contains(String.valueOf(player.getUniqueId()))) {
            return playerLobbyAthleticTimeData.getInt(String.valueOf(player.getUniqueId()));
        } else {
            return 10000;
        }
    }

    public static void setPlayerLobbyAthleticTime(Player player, int playerScore, boolean hasForce) {
        if (hasForce) {
            playerLobbyAthleticTimeData.set(String.valueOf(player.getUniqueId()), playerScore);
            try {
                playerLobbyAthleticTimeData.save(playerLobbyAthleticTimeFile);
                sortEntries();
                ScoreBoardUtils.updateScoreBoard(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
            TextDisplayUtils.latestRanking();
        } else {
            if (getPlayerLobbyAthleticTime(player) > playerScore) {
                playerLobbyAthleticTimeData.set(String.valueOf(player.getUniqueId()), playerScore);
                player.sendMessage("upload");
                try {
                    playerLobbyAthleticTimeData.save(playerLobbyAthleticTimeFile);
                    player.sendMessage("upload finish");
                    sortEntries();
                    ScoreBoardUtils.updateScoreBoard(player);
                    player.sendMessage("updated");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TextDisplayUtils.latestRanking();
            }
        }
    }


    public static void sortEntries() {
        for (String uuid : playerLobbyAthleticTimeData.getKeys(false)) {
            int time = playerLobbyAthleticTimeData.getInt(uuid);
            entryList.add(new AbstractMap.SimpleEntry<>(uuid, time));
        }
        entryList.sort((a, b) -> a.getValue().compareTo(b.getValue()));
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
