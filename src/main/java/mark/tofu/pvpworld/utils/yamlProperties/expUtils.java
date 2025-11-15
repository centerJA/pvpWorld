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

public class expUtils extends JavaPlugin {

    public static File playerExpFile;
    public static FileConfiguration playerExpData;


    public static void playerExpSetup(PvpWorld plugin) {
        playerExpFile = new File(plugin.getDataFolder(), "playerExp.yml");
        if (!playerExpFile.exists()) {
            try {
                playerExpFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerExpData = YamlConfiguration.loadConfiguration(playerExpFile);
        System.out.println("setup good");
    }

    public static int getPlayerExp(Player player) {
        if (playerExpData.contains(String.valueOf(player.getUniqueId()))) {
            System.out.println("setup go12434od");
            return playerExpData.getInt(String.valueOf(player.getUniqueId()), 0);
        } else {
            return 0;
        }
    }

    public static void playerSetExp(Player player, int exp) throws IOException {
        int finalScore = getPlayerExp(player) + exp;
        playerExpData.set(String.valueOf(player.getUniqueId()), finalScore);
        try {
            playerExpData.save(playerExpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.GREEN + "+" + exp + "Exp");
        ScoreBoardUtils.updateScoreBoard(player);
    }
}
