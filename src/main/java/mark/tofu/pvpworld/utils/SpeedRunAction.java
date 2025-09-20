package mark.tofu.pvpworld.utils;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;

public class SpeedRunAction {
    public static void openGameListInventory(Player player) {
        player.sendMessage("38481368");
        Inventory gameList = Bukkit.createInventory(null, 9, "SpeedRun: モード選択");
        gameList.setItem(0, Config.itemMeta("SpeedRunシングルプレイ", Material.PAPER));
        gameList.setItem(1, Config.itemMeta("SpeedRunマルチプレイ", Material.PAPER));
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
    }

    //single------------

    public static void singleOnHoldAction(Player player, PvpWorld plugin) {
        if (!Config.SpeedRunSingleOnHoldList.isEmpty()) { //誰かがプレイしている最中
            player.sendMessage(ChatColor.AQUA + "既に誰かがプレイしています!");
            player.sendMessage(ChatColor.AQUA + "少々お待ちください");
            return;
        } else { //タイマーを発動させる
            player.sendMessage(ChatColor.AQUA + "誰もプレイしていなかったので、開始します");
            player.teleport(Config.speedRunSingleOnholdRoom);
            player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.QUARTZ_BLOCK));
            Config.SpeedRunSingleOnHoldList.add(player.getName());
            player.sendMessage(String.valueOf(Config.SpeedRunSingleOnHoldList));
            SpeedRunTimer.startTimer(player, plugin);
        }
    }

    public static void startSingleMode(Player player, PvpWorld plugin) {
        Random random = new Random();
        player.teleport(Config.speedRunSingleMap1SpawnPoint);
        Config.SpeedRunSingleWaitList.add(player.getName());
        int randomint = random.nextInt(40) + 20;
        String delay = randomint + "L";
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                SpeedRunScheduledTimer.startTimer(player, plugin);
                Config.SpeedRunSingleWaitList.remove(player.getName());
                Config.SpeedRunSingleList.add(player.getName());
            }
        }, randomint);
    }

    //multi------------------
    public static void multiOnHoldAction(Player player, PvpWorld plugin) {
        player.sendMessage("まだアクセスできません!");
    }
}
