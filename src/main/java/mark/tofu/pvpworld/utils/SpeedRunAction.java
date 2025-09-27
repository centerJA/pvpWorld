package mark.tofu.pvpworld.utils;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Random;

public class SpeedRunAction {
    public static void openGameListInventory(Player player) {
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
            player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM));
            Config.SpeedRunSingleOnHoldList.add(player.getName());
            player.sendMessage(String.valueOf(Config.SpeedRunSingleOnHoldList));
            SpeedRunTimer.startTimer(player, plugin);
        }
    }

    public static void startSingleMode(Player player, PvpWorld plugin) {
        Random random = new Random();
        Config.speedRunSingleMap1UnderSandPoint.getBlock().setType(Material.SAND);
        Config.speedRunSingleMap1UpSandPoint.getBlock().setType(Material.SAND);
        player.teleport(Config.speedRunSingleMap1SpawnPoint);
        long delay = 20L + random.nextInt(80);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                SpeedRunScheduledTimer.startTimer(player, plugin);
                Config.SpeedRunSingleList.add(player.getName());
                Config.speedRunSingleMap1UnderSandPoint.getBlock().setType(Material.AIR);
                Config.speedRunSingleMap1UpSandPoint.getBlock().setType(Material.AIR);
                player.sendMessage(ChatColor.AQUA + "スタート!!!");
            }
        }, delay);
    }

    public static void randomEvent(Player player, PvpWorld plugin) {
        Random random = new Random();
        int ran = 1 + random.nextInt(10);
        if (ran == 1) { //プレイヤーの居場所の下に蜘蛛の巣を3秒間設置する
            Location playerLocation = player.getLocation();
            Block originalBlock = playerLocation.getBlock();
            playerLocation.getBlock().setType(Material.COBWEB);
            player.sendMessage(ChatColor.RED + "蜘蛛の巣に引っかかってしまった!");
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    playerLocation.getBlock().setType(originalBlock.getType());
                }
            }, 60L);
        } else if (ran == 2) { //プレイヤーにスピードを3秒間与える
            PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 2);
            player.addPotionEffect(speed);
            player.sendMessage(ChatColor.GREEN + "3秒間歩くスピードが速くなった!");
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.getActivePotionEffects().clear();
                }
            }, 60L);
        } else if (ran == 3) { //プレイヤーにジャンプを3秒間与える
            PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 60, 2);
            player.addPotionEffect(jump);
            player.sendMessage(ChatColor.GREEN + "3秒間ジャンプする高さが高くなった!");
//            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
//                @Override
//                public void run() {
//                    player.getActivePotionEffects().clear();
//                }
//            }, 60L);
        } else if (ran == 4) { //3秒間プレイヤーを動かなくする
            Config.NoWalkList.add(player.getName());
            player.sendMessage(ChatColor.RED + "3秒間動けなくなってしまった!");
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Config.NoWalkList.remove(player.getName());
                }
            }, 60L);
        } else if (ran == 5) { //5秒間浮遊できるアイテムを渡す
            player.getInventory().addItem(Config.itemMeta("浮遊する", Material.FEATHER));
            player.sendMessage(ChatColor.GREEN + "5秒間浮遊できるアイテムをゲットした!");
        } else if (ran == 6) { //5秒間盲目になる
            PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 100, 1);
            player.addPotionEffect(blindness);
        } else if (ran == 7) { //問題を出して、間違えたらリタイア、8秒をすぎてもリタイア
            Config.NoWalkList.add(player.getName());
            Config.setInt(player);

        } else {
            player.sendMessage("エラーが発生しました");
        }
    }

    //multi------------------
    public static void multiOnHoldAction(Player player, PvpWorld plugin) {
        player.sendMessage("まだアクセスできません!");
    }
}
