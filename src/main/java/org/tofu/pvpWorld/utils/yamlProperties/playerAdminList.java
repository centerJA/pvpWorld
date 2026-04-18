package org.tofu.pvpWorld.utils.yamlProperties;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tofu.pvpWorld.utils.textComponent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class playerAdminList {

    public static File playerAdminListFile;
    public static FileConfiguration playerAdminListData;

    public static void playerAdminListSetup(PvpWorld plugin) {
        playerAdminListFile = new File(plugin.getDataFolder(), "playerAdminList.yml");
        if (!playerAdminListFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                playerAdminListFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerAdminListData = YamlConfiguration.loadConfiguration(playerAdminListFile);
    }

    public static void playerApplyAdmin(Player player) {
        String playerUUID = player.getUniqueId().toString();
        List<String> uuidList = playerAdminListData.getStringList("uuids");

        if (!uuidList.contains(playerUUID)) {
            uuidList.add(playerUUID);
            playerAdminListData.set("uuids", uuidList);
            try {
                playerAdminListData.save(playerAdminListFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendMessage(textComponent.parse(player.getName() + ": 正常に追加されました"));
        } else {
            player.sendMessage(textComponent.parse(player.getName() + "はすでに権限を持っています"));
        }
    }

    public static boolean playerHasAdmin(Player player) {
        String playerUUID = player.getUniqueId().toString();
        List<String> uuidList = playerAdminListData.getStringList("uuids");
        return uuidList.contains(playerUUID);
    }

    public static void playerRemoveAdmin(Player player) {
        String playerUUID = player.getUniqueId().toString();
        List<String> uuidList = playerAdminListData.getStringList("uuids");

        if (uuidList.contains(playerUUID)) {
            uuidList.remove(playerUUID);
            playerAdminListData.set("uuids", uuidList);
            try {
                playerAdminListData.save(playerAdminListFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendMessage(textComponent.parse(player.getName() + ": 正常に削除しました"));
        } else {
            player.sendMessage(textComponent.parse(player.getName() + "は権限を持っていません"));
        }
    }

    public static void printAllAdminPlayers(Player player) {
        List<String> uuidList = playerAdminListData.getStringList("uuids");

        if (uuidList.isEmpty()) {
            player.sendMessage(textComponent.parse("誰もいません"));
        } else {
            player.sendMessage(textComponent.parse("一覧"));
            for (String uuid: uuidList) {
                try {
                    UUID uuidFull = UUID.fromString(uuid);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuidFull);
                    String playerName = offlinePlayer.getName();

                    if (playerName == null) continue; // return; だと途中で処理が止まってしまうため continue に修正

                    player.sendMessage(textComponent.parse(playerName));

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}