package mark.tofu.pvpworld.utils.speedRun;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.athletic.AthleticTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class SpeedRunAction {
    public static void openGameListInventory(Player player) {
        Inventory gameList = Bukkit.createInventory(null, 9, "SpeedRun: モード選択");
        gameList.setItem(0, Config.itemMeta("SpeedRunシングルプレイ", Material.PAPER, 1));
        gameList.setItem(1, Config.itemMeta("SpeedRunマルチプレイ", Material.PAPER, 1));
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
    }

    //single------------

    public static void singleOnHoldAction(Player player, PvpWorld plugin) {
        AthleticTimer.stopTimer(player);
        if (!Config.SpeedRunSingleOnHoldList.isEmpty()) { //誰かがプレイしている最中
            player.sendMessage(ChatColor.AQUA + "既に誰かがプレイしています!");
            player.sendMessage(ChatColor.AQUA + "少々お待ちください");
            return;
        } else { //タイマーを発動させる
            player.sendMessage(ChatColor.AQUA + "誰もプレイしていなかったので、開始します");
            player.teleport(Config.speedRunSingleOnholdRoom);
            player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
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
        int ran = 1 + random.nextInt(10); //1~10
        if (ran == 1) { //プレイヤーの居場所の下に蜘蛛の巣を3秒間設置する
            Location playerLocation = player.getLocation();
            Block originalBlock = playerLocation.getBlock();
            Material material = originalBlock.getType();
            playerLocation.getBlock().setType(Material.COBWEB);
            player.sendMessage(ChatColor.RED + "神からの天罰");
            player.sendMessage(ChatColor.RED + "蜘蛛の巣に引っかかってしまった!");
            player.sendMessage(String.valueOf(material));
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    playerLocation.getBlock().setType(material);
                    player.sendMessage("aawwwww");
                }
            }, 60L);
        } else if (ran == 2) { //プレイヤーにスピードを3秒間与える
            PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 2);
            player.addPotionEffect(speed);
            player.sendMessage(ChatColor.GREEN + "天使からのささやかな贈り物");
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
            player.sendMessage(ChatColor.GREEN + "天使からのささやかな贈り物");
            player.sendMessage(ChatColor.GREEN + "3秒間ジャンプする高さが高くなった!");
        } else if (ran == 4) { //3秒間プレイヤーを動かなくする
            Config.NoWalkList.add(player.getName());
            player.sendMessage(ChatColor.RED + "宇宙人の攻撃");
            player.sendMessage(ChatColor.RED + "3秒間動けなくなってしまった!");
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Config.NoWalkList.remove(player.getName());
                }
            }, 60L);
        } else if (ran == 5) { //5秒間浮遊できるアイテムを渡す
            player.getInventory().addItem(Config.itemMeta("浮遊する", Material.FEATHER, 1));
            player.sendMessage(ChatColor.GREEN + "空からの贈り物");
            player.sendMessage(ChatColor.GREEN + "5秒間浮遊できるアイテムをゲットした!");
        } else if (ran == 6) { //5秒間盲目になる
            PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 100, 1);
            player.addPotionEffect(blindness);
            player.sendMessage(ChatColor.RED + "地球の怒り");
            player.sendMessage(ChatColor.RED + "5秒間盲目になってしまった!");
        } else if (ran == 7) { //問題を出して、間違えたらリタイア、8秒をすぎてもリタイア
            player.sendMessage(ChatColor.GOLD + "神からの挑戦状");
            Config.NoWalkList.add(player.getName());
            Config.setInt(player);
        } else if (ran == 8) { //プレイヤーにランダムな方向にノックバックをかける
            Random random2 = new Random();
            double x = random2.nextDouble() * 2 - 1; // -1.0 ～ 1.0
            double y = 0.5 + random2.nextDouble() * 0.5; // 0.5 ～ 1.0 (上向きを保証)
            double z = random2.nextDouble() * 2 - 1;// -1.0 ～ 1.0
            Vector direction = new Vector(x, y, z);
            direction.normalize().multiply(1);
            player.setVelocity(direction);
            player.sendMessage(ChatColor.YELLOW + "知らない人からのちょっかい");
            player.sendMessage(ChatColor.YELLOW + "ノックバックを受けてしまった!");
        } else if (ran == 9) { //1/2ラッキーブロックをあげる
            player.getInventory().addItem(Config.itemMeta("ラックーブロック", Material.GOLD_BLOCK, 1));
            player.sendMessage(ChatColor.YELLOW + "運試し");
            player.sendMessage(ChatColor.YELLOW + "1/2ラッキーブロックを入手した!");
            player.sendMessage("右クリックすると半分の確率で良いものを得られ、半分の確率で悪い効果を受けます");
        } else if (ran == 10) { //
            player.sendMessage("エラー");
        }
    }

    //multi------------------
    public static void multiOnHoldAction(Player player, PvpWorld plugin) {
        player.sendMessage("まだアクセスできません!");
    }


    //click------------------
    public static void clickedFeather(Player player) {
        PotionEffect levitation = new PotionEffect(PotionEffectType.LEVITATION, 100, 1);
        player.addPotionEffect(levitation);
        if (player.getItemInHand().getType().equals(Material.FEATHER)) {
            player.setItemInHand(null);
            player.sendMessage("使用しました!");
        }
    }

    public static void clickedGoldBlock(Player player) {
        Random random = new Random();
        int randomInt = random.nextInt(2) + 1;
        if (randomInt == 1) { //良い
            player.sendTitle(ChatColor.GREEN + "当たり!", "", 20, 40, 20);
            player.getInventory().addItem(Config.itemMeta("スピード", Material.GOLD_BLOCK, 1));
            player.sendMessage("右クリックで5秒間のスピードの効果を得られます!");
            if (player.getItemInHand().getType().equals(Material.GOLD_BLOCK)) {
                player.setItemInHand(null);
            }
        } else {
            player.sendTitle(ChatColor.RED + "はずれ", "", 20, 40, 20);
            PotionEffect confusion = new PotionEffect(PotionEffectType.CONFUSION, 100, 1);
            player.addPotionEffect(confusion);
            player.sendMessage(ChatColor.RED + "5秒間視界が歪むようになってしまった!");
            if (player.getItemInHand().getType().equals(Material.GOLD_BLOCK)) {
                player.setItemInHand(null);
            }
        }
    }

    public static void clickedNetherStar(Player player) {
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 100, 1);
        player.addPotionEffect(speed);
        if (player.getItemInHand().getType().equals(Material.NETHER_STAR)) {
            player.setItemInHand(null);
            player.sendMessage("使用しました!");
        }
    }
}
