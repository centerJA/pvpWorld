package org.tofu.pvpWorld.utils.yamlProperties;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tofu.pvpWorld.PvpWorld;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

public class systemConfig {
    public static File systemConfigFile;

    public static FileConfiguration systemConfig;

    public static void systemConfigSetUp(PvpWorld plugin) {
        systemConfigFile = new File(plugin.getDataFolder(), "systemConfig.yml");
        systemConfig = YamlConfiguration.loadConfiguration(systemConfigFile);
        checkAndCreateKey("reward.athletic.40s");
        checkAndCreateKey("reward.athletic.35s");
        checkAndCreateKey("reward.athletic.30s");

        saveConfig();
    }


    public static String getValueWithoutPlayerInfo(String request) {
        if (request.equals("athleticTimeReset")) {
            long nowTime = System.currentTimeMillis();
            long before = systemConfig.getLong("systemConfig.athleticTimeReset");
            long difference = nowTime - before;
            long twoWeek = 24L * 60 * 60 * 1000 * 14;
            if (twoWeek > difference) {
                return "false";
            } else {
                return "true";
            }
        } else return null;
    }

    public static boolean getValueWithPlayerInfoBOOLEAN(String request, Player player) {
        if (request.equals("reward.athletic.40s")) {
            List<String> currentList = systemConfig.getStringList(request);
            return currentList.contains(player.getUniqueId().toString());
        } else if (request.equals("reward.athletic.35s")) {
            List<String> currentList = systemConfig.getStringList(request);
            return currentList.contains(player.getUniqueId().toString());
        } else if (request.equals("reward.athletic.30s")) {
            List<String> currentList = systemConfig.getStringList(request);
            return currentList.contains(player.getUniqueId().toString());
        } else return false;
    }

    public static void setValueWithPlayerInfoLIST(String request, Player player) {
        String playerUuid = player.getUniqueId().toString();
        List<String> currentList = systemConfig.getStringList(request);

        if (!currentList.contains(playerUuid)) {
            currentList.add(playerUuid);
            systemConfig.set(request, currentList);
            saveConfig();
        }
    }

    public static void setSystemConfigAthleticTime(long time) {
        systemConfig.set("athleticTimeReset", time);
        saveConfig();
    }


    public static void saveConfig() {
        try {
            systemConfig.save(systemConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void checkAndCreateKey(String path) {
        if (!systemConfig.contains(path)) {
            systemConfig.set(path, new ArrayList<String>());

            saveConfig();
        }
    }
}
