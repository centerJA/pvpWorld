package mark.tofu.pvpworld.utils.ffaGames;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.yamlProperties.coinUtils;
import mark.tofu.pvpworld.utils.yamlProperties.expUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class SpleefActivities {
    public static World world = Bukkit.getWorld("pvpWorld");

    public static ArrayList<String> spleefQueueingList = new ArrayList<>(),
                                    spleefPlayingList = new ArrayList<>();

    public static ArrayList<Location> locationList = new ArrayList<>();

    public static Location spawnPoint = new Location(world, 183.500, 4.500, -24.500);

    public static void spleefStartAction(Player player, PvpWorld plugin) {
        player.sendMessage("4:" + spleefPlayingList);
        for (String PlayerName: spleefQueueingList) {
            Config.clearInventory(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)));
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).teleport(spawnPoint);
            ItemStack itemStack = new ItemStack(Material.DIAMOND_SHOVEL, 1);
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).getInventory().setItem(0, itemStack);
        }
        spleefPlayingList.addAll(spleefQueueingList);
        player.sendMessage("5:" + spleefPlayingList);
        spleefQueueingList.clear();
        TimeUpTimer.startTimer(player, plugin, 600);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (String PlayerName: spleefPlayingList) {
                    Config.DoNotReceiveDamageList.remove(PlayerName);
                }
            }
        }, 100L);
    }

    public static void spleefCloseAction(PvpWorld plugin) throws IOException {
        Config.DoNotReceiveDamageList.addAll(spleefPlayingList);
        Config.TeleportToLobbyList.addAll(spleefPlayingList);
        for (String PlayerName: spleefPlayingList) {
            Player player = Bukkit.getPlayer(PlayerName);
            if (player == null) return;
            player.sendTitle(ChatColor.GREEN + "勝利", ChatColor.YELLOW + "おめでとう!!!", 0, 60, 0);
            expUtils.playerSetExp(player, 10);
            coinUtils.playerSetCoin(player, 10);
        }
        FfaGames.gameCloseAction(plugin);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                resetSnowBlock();
            }
        }, 80L);
    }

    public static void voidAction(Player player, PvpWorld plugin) throws IOException {
        spleefPlayingList.remove(player.getName());
        player.sendTitle(ChatColor.RED + "敗北", ChatColor.YELLOW + "もう一度挑戦しよう!", 0, 60, 0);
        winnerChecker(player, plugin);
        Config.clearInventory(player);
        expUtils.playerSetExp(player, 5);
        coinUtils.playerSetCoin(player, 4);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                player.teleport(Config.lobby);
            }
        }, 60L);
    }

    public static void winnerChecker(Player player, PvpWorld plugin) throws IOException {
        if (spleefPlayingList.size() == 1) {
            spleefCloseAction(plugin);
        }
    }


    public static void snowBallAction(Player player) {
        Random random = new Random();
        int i = random.nextInt(10) + 1;
        if (i <= 4) {
            player.getInventory().addItem(Config.itemMeta("あぶない雪玉", Material.SNOWBALL, 1));
        }
    }

    public static void resetSnowBlock() {
        for (Location loc: locationList) {
            loc.getBlock().setType(Material.SNOW_BLOCK);
        }
    }
}
