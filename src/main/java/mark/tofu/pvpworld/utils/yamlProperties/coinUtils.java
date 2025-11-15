package mark.tofu.pvpworld.utils.yamlProperties;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class   coinUtils extends JavaPlugin {
    public static File playerCoinDataFile;
    public static FileConfiguration playerCoinData;

    public static void playerCoinSetUp(PvpWorld plugin) {
        playerCoinDataFile = new File(plugin.getDataFolder(), "playerCoin.yml");
        if (!playerCoinDataFile.exists()) {
            try {
                playerCoinDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerCoinData = YamlConfiguration.loadConfiguration(playerCoinDataFile);
    }

    public static int getPlayerCoin(Player player) {
        if (playerCoinData.contains(String.valueOf(player.getUniqueId()))) {
            return playerCoinData.getInt(String.valueOf(player.getUniqueId()), 0);
        } else {
            return 0;
        }
    }

    public static void playerSetCoin(Player player, int coin) throws IOException {
        int finalScore = getPlayerCoin(player) + coin;
        playerCoinData.set(String.valueOf(player.getUniqueId()), finalScore);
        try {
            playerCoinData.save(playerCoinDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.GOLD + "+" + coin + "Coin");
        ScoreBoardUtils.updateScoreBoard(player);
    }
}
