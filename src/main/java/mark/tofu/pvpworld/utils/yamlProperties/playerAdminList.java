package mark.tofu.pvpworld.utils.yamlProperties;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class playerAdminList extends JavaPlugin {

    public static File playerAdminListFile;
    public static FileConfiguration playerAdminListData;

    public static void playerAdminListSetup(PvpWorld plugin) {
        playerAdminListFile = new File(plugin.getDataFolder(), "playerAdminList.yml");
        if (!playerAdminListFile.exists()) {
            try {
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
        } else {
            player.sendMessage(player.getName() + "はすでに権限を持っています");
        }
        try {
            playerAdminListData.save(playerAdminListFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(player.getName() + ": 正常に追加されました");
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
        } else {
            player.sendMessage(player.getName() + "は権限を持っていません");
        }
        try {
            playerAdminListData.save(playerAdminListFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(player.getName() + ": 正常に削除しました");
    }

    public static void printAllAdminPlayers(Player player) {
        List<String> uuidList = playerAdminListData.getStringList("uuids");
        if (uuidList.isEmpty()) {
            player.sendMessage("誰もいません");
        } else {
            player.sendMessage("一覧");
            for (String uuid: uuidList) {
                try {
                    UUID uuidFull = UUID.fromString(uuid);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuidFull);
                    String playerName = offlinePlayer.getName();
                    if (playerName == null) return;
                    player.sendMessage(playerName);

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
