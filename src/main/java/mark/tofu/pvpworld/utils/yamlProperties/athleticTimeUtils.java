package mark.tofu.pvpworld.utils.yamlProperties;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class athleticTimeUtils extends JavaPlugin {
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
                ScoreBoardUtils.updateScoreBoard(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (getPlayerLobbyAthleticTime(player) > playerScore) {
                playerLobbyAthleticTimeData.set(String.valueOf(player.getUniqueId()), playerScore);
                try {
                    playerLobbyAthleticTimeData.save(playerLobbyAthleticTimeFile);
                    ScoreBoardUtils.updateScoreBoard(player);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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



    public static String getRanking(int x) {
        int index = x - 1;
        if (index >= 0 && index < entryList.size()) {
            Map.Entry<String, Integer> entry = entryList.get(index);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
            String playerName = offlinePlayer.getName();
            return ChatColor.GOLD + String.valueOf(x) + "位 - " + ChatColor.WHITE + playerName + ": " + ChatColor.RED + entry.getValue();
        } else {
            return ChatColor.GOLD + String.valueOf(x) + "位 - " + ChatColor.WHITE + "N/A";
        }
    }
}
