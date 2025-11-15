package mark.tofu.pvpworld.utils.yamlProperties;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class athleticTimeUtils extends JavaPlugin {
    public static File playerLobbyAthleticTimeFile;
    public static FileConfiguration playerLobbyAthleticTimeData;

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
}
