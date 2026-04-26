package org.tofu.pvpWorld.utils.speedRun;

import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.itemStackMaker;
import org.tofu.pvpWorld.utils.lobbyAthletic.AthleticTimer;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.tofu.pvpWorld.utils.titleMaker;

import java.util.Objects;
import java.util.Random;


public class SpeedRunAction {
    public static void openGameListInventory(Player player) {
        Inventory gameList = Bukkit.createInventory(null, 9, Component.text("SpeedRun: モード選択"));
        gameList.setItem(0, itemStackMaker.createItem(textComponent.parse("SpeedRunシングルプレイ"), Material.PAPER, 1));
        gameList.setItem(1, itemStackMaker.createItem(textComponent.parse("SpeedRunマルチプレイ"), Material.PAPER, 1));
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
        player.sendMessage("test0000");
    }

    //single------------

    public static void singleOnHoldAction(Player player, PvpWorld plugin) {
        AthleticTimer.stopTimer(player);
        if (!Config.SpeedRunSingleOnHoldList.isEmpty()) { //誰かがプレイしている最中
            player.sendMessage(textComponent.parse("<aqua>既に誰かがプレイしています!</aqua>"));
            player.sendMessage(textComponent.parse("<aqua>少々お待ちください</aqua>"));
            return;
        } else { //タイマーを発動させる
            player.sendMessage(textComponent.parse("<aqua>誰もプレイしていなかったので、開始します</aqua>"));
            player.teleport(Config.speedRunSingleOnholdRoom);
            player.getInventory().setItem(0, itemStackMaker.createItem(textComponent.parse("ロビーに戻る"), Material.RED_MUSHROOM, 1));
            Config.SpeedRunSingleOnHoldList.add(player.getName());
            player.sendMessage(String.valueOf(Config.SpeedRunSingleOnHoldList));
            SpeedRunTimer.startTimer(player, plugin);
            TextDisplayUtils.renameSpeedRun(Config.SpeedRunSingleList.size() + Config.SpeedRunSingleOnHoldList.size());
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
                SpeedRunScheduledTimer.startTimer(player, plugin, false);
                Config.SpeedRunSingleList.add(player.getName());
                Config.speedRunSingleMap1UnderSandPoint.getBlock().setType(Material.AIR);
                Config.speedRunSingleMap1UpSandPoint.getBlock().setType(Material.AIR);
                player.sendMessage(textComponent.parse("<aqua>スタート!!!</aqua>"));
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
            player.playSound(playerLocation, Sound.BLOCK_COBWEB_PLACE, 1, 1);
            player.sendMessage(textComponent.parse("<red>神からの天罰</red>"));
            player.sendMessage(textComponent.parse("<red>蜘蛛の巣に引っかかってしまった!</red>"));
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    playerLocation.getBlock().setType(material);
                }
            }, 60L);
        } else if (ran == 2) { //プレイヤーにスピードを3秒間与える
            PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 2);
            player.addPotionEffect(speed);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            player.sendMessage(textComponent.parse("<green>天使からのささやかな贈り物</green>"));
            player.sendMessage(textComponent.parse("<green>3秒間歩くスピードが速くなった!</green>"));
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.getActivePotionEffects().clear();
                }
            }, 60L);
        } else if (ran == 3) { //プレイヤーにジャンプを3秒間与える
            PotionEffect jump = new PotionEffect(PotionEffectType.JUMP_BOOST, 60, 2);
            player.addPotionEffect(jump);
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1, 1);
            player.sendMessage(textComponent.parse("<green>天使からのささやかな贈り物</green>"));
            player.sendMessage(textComponent.parse("<green>3秒間ジャンプしたときの高さが高くなった!</green>"));
        } else if (ran == 4) { //3秒間プレイヤーを動かなくする
            Config.NoWalkList.add(player.getName());
            player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT,1 ,1);
            player.sendMessage(textComponent.parse("<red>宇宙人の攻撃</red>"));
            player.sendMessage(textComponent.parse("<red>3秒間動けなくなってしまった!</red>"));
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Config.NoWalkList.remove(player.getName());
                }
            }, 60L);
        } else if (ran == 5) { //5秒間浮遊できるアイテムを渡す
            player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1.6F);
            player.getInventory().addItem(itemStackMaker.createItem(textComponent.parse("浮遊する"), Material.FEATHER, 1));
            player.sendMessage(textComponent.parse("<green>空からの贈り物</green>"));
            player.sendMessage(textComponent.parse("<green>5秒間浮遊できるアイテムをゲットした!</green>"));
        } else if (ran == 6) { //5秒間盲目になる
            PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 100, 1);
            player.addPotionEffect(blindness);
            player.playSound(player.getLocation(), Sound.ENTITY_MULE_DEATH, 1, 1);
            player.sendMessage(textComponent.parse("<red>地球の怒り</red>"));
            player.sendMessage(textComponent.parse("<red>5秒間盲目になってしまった!</red>"));
        } else if (ran == 7) { //問題を出して間違えたら鈍足
            player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_AMBIENT, 1, 1);
            player.sendMessage(textComponent.parse("<gold>神からの挑戦状</gold>"));
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
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
            player.sendMessage(textComponent.parse("<yellow>知らない人からのちょっかい</yellow>"));
            player.sendMessage(textComponent.parse("<yellow>ノックバックを受けてしまった!</yellow>"));
        } else if (ran == 9) { //1/2ラッキーブロックをあげる
            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SWAG,1 ,1);
            player.getInventory().addItem(itemStackMaker.createItem(textComponent.parse("ラッキーブロック"), Material.GOLD_BLOCK, 1));
            player.sendMessage(textComponent.parse("<yellow>運試し</yellow>"));
            player.sendMessage(textComponent.parse("<yellow>1/2ラッキーブロックを入手した!</yellow>"));
            player.sendMessage(textComponent.parse("右クリックすると半分の確率で良いものを得られ、半分の確率で悪い効果を受けます"));
        } else if (ran == 10) { //
            player.sendMessage("エラー");
        }
    }

    public static void ruleDescription(Player player) {
        player.sendMessage(textComponent.parse("<aqua>-----SpeedRunシングルプレイ-----"));
        player.sendMessage(Component.text("このゲームは、アスレチックを走り抜けてゴールにあるボタンを押す速さを争うゲームです!"));
        player.sendMessage(Component.text("でも、ただアスレチックをするだけではありません!"));
        player.sendMessage(textComponent.parse("<yellow>10秒に1回ランダムでイベントが発生します!!"));
        player.sendMessage(textComponent.parse("<green>歩く速さが速く<white>なったり、<red>周りが見えなく<white>なったり..."));
        player.sendMessage(Component.text("リーダーボードも作る予定です!"));
        player.sendMessage(textComponent.parse("<aqua>---------------------------"));
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
            player.showTitle(titleMaker.title(textComponent.parse("<gold>当たり!</gold>"), Component.empty(), 1000, 2000, 1000));
            player.getInventory().addItem(itemStackMaker.createItem(textComponent.parse("スピード"), Material.NETHER_STAR, 1));
            player.sendMessage("右クリックで5秒間のスピードの効果を得られます!");
            if (player.getItemInHand().getType().equals(Material.GOLD_BLOCK)) {
                player.setItemInHand(null);
            }
        } else {
            player.showTitle(titleMaker.title(textComponent.parse("はずれ"), Component.empty(), 1000, 2000, 1000));
            PotionEffect confusion = new PotionEffect(PotionEffectType.DARKNESS, 100, 10);
            player.addPotionEffect(confusion);
            player.sendMessage(textComponent.parse("<red>5秒間視界が歪むようになってしまった!</red>"));
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

    public static void mutiMapSelecting(Player player) {
        Inventory gameList = Bukkit.createInventory(null, 9, textComponent.parse("<red>speedRun multi</red>"));
        gameList.setItem(0, itemStackMaker.createItem(textComponent.parse("SpeedRunシングルプレイ"), Material.PAPER, 1));
        gameList.setItem(1, itemStackMaker.createItem(textComponent.parse("SpeedRunマルチプレイ"), Material.PAPER, 1));
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
    }
}
