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

public class expUtils extends JavaPlugin {

    public static File playerExpFile;
    public static FileConfiguration playerExpData;

    public static List<Map.Entry<String, Integer>> entryList = new ArrayList<>();


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


    public static void sortEntries() {
        for (String uuid : playerExpData.getKeys(false)) {
            int exp = playerExpData.getInt(uuid);
            entryList.add(new AbstractMap.SimpleEntry<>(uuid, exp));
        }
        entryList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    }



    public static String getRanking(int x) {
        int index = x - 1;
        if (index >= 0 && index < entryList.size()) {
            Map.Entry<String, Integer> entry = entryList.get(index);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
            String playerName = offlinePlayer.getName();
            return ChatColor.GOLD + String.valueOf(x) + "位 - " + ChatColor.WHITE + playerName + ": " + ChatColor.GREEN + entry.getValue();
        } else {
            return ChatColor.GOLD + String.valueOf(x) + "位 - " + ChatColor.WHITE + "N/A";
        }
    }
}
