package mark.tofu.pvpworld;


import mark.tofu.pvpworld.utils.SpeedRunScheduledTimer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Config extends JavaPlugin {
    public static World world = Bukkit.getWorld("pvpWorld");

    public static File playerExpFile, playerLastRoginFile;
    public static FileConfiguration playerExpData, playerLastRogin;


    public static ArrayList<String> WorldAllPlayerList = new ArrayList<>(),
                                    DoNotReceiveDamageList = new ArrayList<>(),
                                    SpeedRunSingleOnHoldList = new ArrayList<>(),
                                    AdminBuildModeList = new ArrayList<>(),
                                    SpeedRunSingleList = new ArrayList<>(),
                                    NoWalkList = new ArrayList<>(),
                                    FreePvpPlayerList = new ArrayList<>();

    public static Location lobby = new Location(world, 0.500, 5.500, -0.500, 90, 0),
                           lobbyAthleticStart = new Location(world, -28, 4, 6),
                           lobbyAthleticFinish = new Location(world, -29, 7, -1),
                           speedRunSingleOnholdRoom = new Location(world, -77.500, 4, -0.500, 90, 0),
                           speedRunSingleMap1SpawnPoint = new Location(world, -13.500, 4, 107.500, 0, 0),
                           speedRunSingleMap1UnderSandPoint = new Location(world, -14, 4, 109),
                           speedRunSingleMap1UpSandPoint = new Location(world, -14, 5, 109),
                           freePvpJoinPoint = new Location(world, 42, 4, -1),
                           freePvpSpawnPoint = new Location(world, 54.500, 14.500, -0.500, -90, 0);

    public static int random1, random2, result;

    public static ItemStack serverSelect, worldSelect, quit;

    public static ItemStack itemMeta(String displayName, Material material, int i) {
        Bukkit.getLogger().info("called itemMeta!");
        ItemStack itemStack = new ItemStack(material, i);
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
        String notice = "Free PVPスペースが解放されました!!";
        String date = "2025/10/10";
        String description = "ロビーの後ろにあるので、足を運んでみてください!";
        return date + ": {" + notice + "}: " + description;
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
        long oneDay = 24L * 60 * 60 * 1000;
        long nowTime = System.currentTimeMillis();
//        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        long lastPlayed = getPlayerLastLogin(player);
        if (lastPlayed == 0) {
            player.sendMessage(ChatColor.AQUA + "初参加です!");
            setPlayerLastRogin(player);
            return true;
        }
        long difference = nowTime - lastPlayed;

        return difference >= oneDay;
    }


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

    }

    public static void playerLastRoginSetup(PvpWorld plugin) {
        playerLastRoginFile = new File(plugin.getDataFolder(), "playerLastRoginTime.yml");
        if (!playerLastRoginFile.exists()) {
            try {
                playerLastRoginFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getPlayerLastLogin(Player player) {
        return playerLastRogin.getInt(player.getName(), 0);
    }

    public static void setPlayerLastRogin(Player player) {
        long nowTime = System.currentTimeMillis();
        playerLastRogin.set(player.getName(), nowTime);
        try {
            playerLastRogin.save(playerLastRoginFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setInt(Player player) {
        Random random = new Random();
        random1 = random.nextInt(10) + 1;
        random2 = random.nextInt(10) + 1;
        result = random2 + random1;
        player.sendMessage(ChatColor.YELLOW + "クイズ!間違えたら脱落、8秒以内に答えられなくても脱落!");
        player.sendMessage(ChatColor.AQUA + String.valueOf(random1) + " + " + String.valueOf(random2) + " = ");

    }
    public static void compair(Player player, String chat) {
        if (String.valueOf(result).equals(chat)) {
            player.sendMessage(ChatColor.GREEN + "合格!");
            NoWalkList.remove(player.getName());
        } else {
            player.sendMessage(ChatColor.RED + "間違えてしまった!");
            player.sendMessage(String.valueOf(result));
            player.sendTitle(ChatColor.RED + "脱落", ChatColor.AQUA + "再挑戦しよう!", 20, 80, 20);
            Config.clearInventory(player);
            player.getInventory().setItem(0, itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
            SpeedRunSingleList.remove(player.getName());
            NoWalkList.remove(player.getName());
            SpeedRunScheduledTimer.stopTimer(player);
            player.setExp(0);
        }
    }

    public static void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(9, serverSelect);
        player.getInventory().setItem(10, worldSelect);
        player.getInventory().setItem(11, quit);
    }

    public static void checkInventoryItem(Player player) {
        serverSelect = player.getInventory().getItem(9);
        worldSelect = player.getInventory().getItem(10);
        quit = player.getInventory().getItem(11);
    }

}
