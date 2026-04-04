package org.tofu.pvpWorld;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.tofu.pvpWorld.utils.itemStackMaker;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.ffaGames.SpleefActivities;
import org.tofu.pvpWorld.utils.lobbyAthletic.AthleticTimer;
import org.tofu.pvpWorld.utils.oneVersusOne.StartTimerUtils;
import org.tofu.pvpWorld.utils.oneVersusOne.SumoActivities;
import org.tofu.pvpWorld.utils.oneVersusOne.TimeUpTimer;
import org.tofu.pvpWorld.utils.oneVersusOne.TopfightActivities;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunActionMulti;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunScheduledTimer;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunTimer;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

public class Config extends JavaPlugin {
    public static World world = Bukkit.getWorld("pvpWorld");

    public static File playerLastLoginFile, systemConfigFile;
    public static FileConfiguration  playerLastLogin, systemConfig;

    public static ArrayList<String> WorldAllPlayerList = new ArrayList<>(),
            DoNotReceiveDamageList = new ArrayList<>(),
            SpeedRunSingleOnHoldList = new ArrayList<>(),
            AdminBuildModeList = new ArrayList<>(),
            SpeedRunSingleList = new ArrayList<>(),
            NoWalkList = new ArrayList<>(),
            FreePvpPlayerList = new ArrayList<>(),
            TeleportToLobbyList = new ArrayList<>();

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



    public static String worldUpdateNotice() {
        String notice = "Free PVPスペースが解放されました!!";
        String date = "2025/10/10";
        String description = "ロビーの後ろにあるので、足を運んでみてください!";
        return date + ": {" + notice + "}: " + description;
    }

    public static boolean testPlayerLastLoginTime(Player player) {
        long oneDay = 24L * 60 * 60 * 1000;
        long nowTime = System.currentTimeMillis();
        long lastPlayed = getPlayerLastLogin(player);
        if (lastPlayed == 0) {
            player.sendMessage(textComponent.parse("<aqua>初参加です!"));
            setPlayerLastLogin(player);
            return true;
        }
        long difference = nowTime - lastPlayed;
        return difference >= oneDay;
    }

    public static void playerLastLoginSetup(PvpWorld plugin) {
        playerLastLoginFile = new File(plugin.getDataFolder(), "playerLastLoginTime.yml");
        if (!playerLastLoginFile.exists()) {
            try {
                playerLastLoginFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerLastLogin = YamlConfiguration.loadConfiguration(playerLastLoginFile);
    }

    public static long getPlayerLastLogin(Player player) {
        if (playerLastLogin.contains(String.valueOf(player.getUniqueId()))) {
            return playerLastLogin.getLong(String.valueOf(player.getUniqueId()));
        } else {
            return 0;
        }
    }

    public static void setPlayerLastLogin(Player player) {
        long nowTime = System.currentTimeMillis();
        playerLastLogin.set(String.valueOf(player.getUniqueId()), nowTime);
        try {
            playerLastLogin.save(playerLastLoginFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setInt(Player player) {
        Random random = new Random();
        random1 = random.nextInt(10) + 1;
        random2 = random.nextInt(10) + 1;
        result = random2 + random1;
        player.sendMessage(textComponent.parse("<yellow>クイズ!間違えたら脱落、8秒以内に答えられなくても脱落!"));
        player.sendMessage(textComponent.parse("<aqua>" + random1 + " + " + random2 + " = "));
    }

    public static void compair(Player player, String chat) {
        if (String.valueOf(result).equals(chat)) {
            player.sendMessage(textComponent.parse("<green>合格!"));
            NoWalkList.remove(player.getName());
        } else {
            player.sendMessage(textComponent.parse("<red>間違えてしまった!"));
            player.sendMessage(Component.text(String.valueOf(result)));
            Title.Times times = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(4), Duration.ofSeconds(1));
            player.showTitle(Title.title(textComponent.parse("<red>脱落"), textComponent.parse("<aqua>再挑戦しよう!"), times));
            Config.clearInventory(player);
            player.getInventory().setItem(0, itemStackMaker.createItem(textComponent.parse("<white>ロビーに戻る"), Material.RED_MUSHROOM, 1));
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

    public static void systemConfigSetUp(PvpWorld plugin) {
        systemConfigFile = new File(plugin.getDataFolder(), "systemConfig.yml");
        if (!systemConfigFile.exists()) {
            try {
                systemConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        systemConfig = YamlConfiguration.loadConfiguration(systemConfigFile);
    }

    public static String systemConfigGetItem(String request) {
        if (request.equals("athleticTime")) {
            long nowTime = System.currentTimeMillis();
            long before = systemConfig.getLong("systemConfig.athleticTime");
            long difference = nowTime - before;
            long twoWeek = 24L * 60 * 60 * 1000 * 14;
            if (twoWeek > difference) {
                return "false";
            } else {
                return "true";
            }
        }
        else return null;
    }

    public static void setSystemConfigAthleticTime(long time) {
        systemConfig.set("Config.athleticTime", time);
        try {
            systemConfig.save(systemConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean overLappingTrigger(Player player) {
        String playerName = player.getName();
        int i = 0;
        if (AthleticTimer.playerTimes.get(player) != null) i = i + 1;
        if (SpleefActivities.spleefQueueingList.contains(playerName)) i = i + 1;
        if (SumoActivities.sumoQueueingList.contains(playerName)) i = i + 1;
        if (TopfightActivities.topfightQueueingList.contains(playerName)) i = i + 1;
        if (SpeedRunSingleOnHoldList.contains(playerName)) i = i + 1;
        if (FreePvpPlayerList.contains(playerName)) i = i + 1;
        if (SpeedRunActionMulti.multiPlayingList.contains(playerName)) i = i + 1;

        return i != 0;
    }

    public static void overLappingMessage(Player player) {
        player.sendMessage(Component.text("あなたは既に他のゲームに参加しています!"));
        player.sendMessage(Component.text("退出してからゲームに参加してください"));
        player.teleport(Config.lobby);
    }

    public static void beforeGame(Player player) throws IOException {
        String playerName = player.getName();
        if (Config.SpeedRunSingleOnHoldList.contains(playerName) || Config.SpeedRunSingleList.contains(playerName)) {
            Config.SpeedRunSingleOnHoldList.remove(playerName);
            Config.SpeedRunSingleList.remove(playerName);
            SpeedRunTimer.stopTimer(player);
            SpeedRunScheduledTimer.stopTimer(player);
            player.sendMessage(Component.text("SpeedRunをキャンセルしました"));
            TextDisplayUtils.renameSpeedRun(Config.SpeedRunSingleList.size() + Config.SpeedRunSingleList.size());
        }
        StartTimerUtils.stopTimer(player);
        TimeUpTimer.stopTimer(player);
        Config.clearInventory(player);
        player.getInventory().setItem(0, itemStackMaker.createItem(textComponent.parse("<white>ロビーに戻る"), Material.RED_MUSHROOM, 1));
        player.teleport(Config.lobby);
        player.setLevel(0);
        AthleticTimer.stopTimer(player);
        if (Config.FreePvpPlayerList.contains(playerName)) {
            Config.FreePvpPlayerList.remove(playerName);
            Config.DoNotReceiveDamageList.add(playerName);
            player.sendMessage(Component.text("Free PVPを退出しました"));
        }
        if (SpeedRunActionMulti.multiPlayingList.contains(playerName)) {
            SpeedRunActionMulti.playerLeaveAction(player);
        }
    }
}