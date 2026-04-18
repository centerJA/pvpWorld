package org.tofu.pvpWorld.utils.yamlProperties;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.scoreBoard.ScoreBoardUtils;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils; // ←追加: TextDisplayの更新用
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tofu.pvpWorld.utils.textComponent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class expUtils {

    public static File playerExpFile;
    public static FileConfiguration playerExpData;

    public static List<Map.Entry<String, Integer>> entryList = new ArrayList<>();

    public static void playerExpSetup(PvpWorld plugin) {
        playerExpFile = new File(plugin.getDataFolder(), "playerExp.yml");
        if (!playerExpFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                playerExpFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerExpData = YamlConfiguration.loadConfiguration(playerExpFile);
        sortEntries();

        System.out.println("setup good");
    }

    public static int getPlayerExp(Player player) {
        if (playerExpData.contains(String.valueOf(player.getUniqueId()))) {
            return playerExpData.getInt(String.valueOf(player.getUniqueId()), 0);
        } else {
            return 0;
        }
    }

    public static void playerSetExp(Player player, int exp) {
        int finalScore = getPlayerExp(player) + exp;
        playerExpData.set(String.valueOf(player.getUniqueId()), finalScore);

        try {
            playerExpData.save(playerExpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        sortEntries();
        TextDisplayUtils.latestRanking();

        player.sendMessage(textComponent.parse("<green>+" + exp + "Exp"));
        ScoreBoardUtils.updateScoreBoard(player);
    }

    public static void sortEntries() {
        entryList.clear();
        for (String uuid : playerExpData.getKeys(false)) {
            int exp = playerExpData.getInt(uuid);
            entryList.add(new AbstractMap.SimpleEntry<>(uuid, exp));
        }
        entryList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    }

    public static Component getRanking(int x) {
        int index = x - 1;
        if (index >= 0 && index < entryList.size()) {
            Map.Entry<String, Integer> entry = entryList.get(index);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
            String playerName = offlinePlayer.getName();
            return textComponent.parse("<gold>" + x + "位 - <white>" + playerName + ": <green>" + entry.getValue());
        } else {
            return textComponent.parse("<gold>" + x + "位 - <white>N/A");
        }
    }
}