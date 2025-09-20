package mark.tofu.pvpworld;


import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Config extends JavaPlugin {
    public static World world = Bukkit.getWorld("pvpWorld");

    public static File playerExpFile;
    public static FileConfiguration playerExpData;


    public static ArrayList<String> WorldAllPlayerList = new ArrayList<>(),
                                    DoNotReceiveDamageList = new ArrayList<>(),
                                    SpeedRunSingleOnHoldList = new ArrayList<>(),
                                    AdminBuildModeList = new ArrayList<>(),
                                    SpeedRunSingleWaitList = new ArrayList<>(),
                                    SpeedRunSingleList = new ArrayList<>();

    public static Location lobby = new Location(world, 0.500, 5.500, -0.500, 90, 0),
                           lobbyAthleticStart = new Location(world, -28, 4, 6),
                           lobbyAthleticFinish = new Location(world, -29, 7, -1),
                           speedRunSingleOnholdRoom = new Location(world, -78.500, 4, -1.500, 90, 0),
                           speedRunSingleMap1SpawnPoint = new Location(world, 14.500, 4, 107, 0, 0);


    public static ItemStack itemMeta(String displayName, Material material) {
        Bukkit.getLogger().info("called itemMeta!");
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            Bukkit.getLogger().info("itemMeta is null");
            return null;
        }
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static String worldUpdateNotice() {
        String notice = "お知らせを表示するコマンドが作成されました!";
        String date = "2025/09/14";
        return date + ": " + notice;
    }

    public static void playerSetLoginExp(Player player) throws IOException {
        int finalScore = getPlayerExp(player) + 5;
        playerExpData.set(player.getName(), finalScore);
        try {
            playerExpData.save(playerExpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage("5expが追加されました");
        player.sendMessage(String.valueOf(getPlayerExp(player)) + "exp");
    }

    public static int getPlayerExp(Player player) {
        return playerExpData.getInt(player.getName(), 0);
    }

    public static boolean testPlayerLastLoginTime(Player player) {
        String playerName = player.getName();
        long oneDay = 24L * 60 * 60 * 1000;
        long nowTime = System.currentTimeMillis();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        long lastPlayed = player.getLastPlayed();
        if (lastPlayed == 0) {
            player.sendMessage(ChatColor.AQUA + "初参加です!");
            return true;
        }
        long difference = nowTime - lastPlayed;

        return difference >= oneDay;
    }


    public static void setup(PvpWorld plugin) {
        playerExpFile = new File(plugin.getDataFolder(), "playerExp.yml");
        if (!playerExpFile.exists()) {
            try {
                playerExpFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerExpData = YamlConfiguration.loadConfiguration(playerExpFile);

    }
}
