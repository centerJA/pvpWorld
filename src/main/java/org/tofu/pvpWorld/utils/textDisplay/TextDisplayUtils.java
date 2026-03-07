package org.tofu.pvpWorld.utils.textDisplay;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.utils.athletic.AthleticProperties;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.yamlProperties.athleticTimeUtils;
import org.tofu.pvpWorld.utils.yamlProperties.coinUtils;
import org.tofu.pvpWorld.utils.yamlProperties.expUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class TextDisplayUtils {
    public static Location SpeedRun, OneVersusOne, FfaGames, expRanking, coinRanking, athleticRanking;

    public static Component SpeedRunSize, OneVersusOneSize, FfaGamesSize;

    public static List<Component> exp, coin, athletic;

    private static World world;

    public static void locationSetUp() {
        world = Bukkit.getWorld("pvpWorld");
        SpeedRun = new Location(world, -18.500, 7.000, -0.500);
        OneVersusOne = new Location(world, 0.500, 7.000, -19.500);
        FfaGames = new Location(world, 0.500, 7.000, 18.500);
        SpeedRunSize = textComponent.parse("<gold>0<white>人がプレイ中!");
        OneVersusOneSize = textComponent.parse("<gold>0<white>人がプレイ中!");
        FfaGamesSize = textComponent.parse("<gold>0<white>人がプレイ中!");
        expRanking = new Location(world, -5.500, 6.000, 44.000);
        coinRanking = new Location(world, 6.500, 6.000, 44.000);
        athleticRanking = new Location(world, -5.500, 6.000, 55.000);

        System.out.println("[PVPWORLD]loc setup finished");
        latestRanking();
    }

    public static void latestRanking() {
        exp = new ArrayList<>();
        exp.add(textComponent.parse("<green>EXP<red>ランキング"));
        exp.add(expUtils.getRanking(1));
        exp.add(expUtils.getRanking(2));
        exp.add(expUtils.getRanking(3));
        exp.add(expUtils.getRanking(4));
        exp.add(expUtils.getRanking(5));
        exp.add(expUtils.getRanking(6));
        exp.add(expUtils.getRanking(7));
        exp.add(expUtils.getRanking(8));
        exp.add(expUtils.getRanking(9));
        exp.add(expUtils.getRanking(10));
        exp.add(textComponent.parse("<green>クリックして更新"));

        coin = new ArrayList<>();
        coin.add(textComponent.parse("<gold>COIN<red>ランキング"));
        coin.add(coinUtils.getRanking(1));
        coin.add(coinUtils.getRanking(2));
        coin.add(coinUtils.getRanking(3));
        coin.add(coinUtils.getRanking(4));
        coin.add(coinUtils.getRanking(5));
        coin.add(coinUtils.getRanking(6));
        coin.add(coinUtils.getRanking(7));
        coin.add(coinUtils.getRanking(8));
        coin.add(coinUtils.getRanking(9));
        coin.add(coinUtils.getRanking(10));
        coin.add(textComponent.parse("<green>クリックして更新"));

        athletic = new ArrayList<>();
        athletic.add(textComponent.parse("<yellow>Athletic<red>ランキング"));
        athletic.add(athleticTimeUtils.getRanking(1));
        athletic.add(athleticTimeUtils.getRanking(2));
        athletic.add(athleticTimeUtils.getRanking(3));
        athletic.add(athleticTimeUtils.getRanking(4));
        athletic.add(athleticTimeUtils.getRanking(5));
        athletic.add(athleticTimeUtils.getRanking(6));
        athletic.add(athleticTimeUtils.getRanking(7));
        athletic.add(athleticTimeUtils.getRanking(8));
        athletic.add(athleticTimeUtils.getRanking(9));
        athletic.add(athleticTimeUtils.getRanking(10));
        athletic.add(textComponent.parse("<green>クリックして更新"));

        System.out.println("[PVPWORLD]ranking setup finished");
        showAllText();
    }

    public static void renameSpeedRun(int size) {
        SpeedRunSize = textComponent.parse("<gold>" + size + "<white>人がプレイ中!");
        showAllText();
    }

    public static void renameOneVersusOneSize(int size) {
        OneVersusOneSize = textComponent.parse("<gold>" + size + "<white>人がプレイ中!");
        showAllText();
    }

    public static void renameFfaGamesSize(int size) {
        FfaGamesSize = textComponent.parse("<gold>" + size + "<white>人がプレイ中!");
        showAllText();
    }

    public static void showAllText() {
        removeAllText();
        ArmorStand armorStand = SpeedRun.getWorld().spawn(SpeedRun, ArmorStand.class);
        armorStandSettings(armorStand, SpeedRunSize, true);

        ArmorStand armorStand1 = OneVersusOne.getWorld().spawn(OneVersusOne, ArmorStand.class);
        armorStandSettings(armorStand1, OneVersusOneSize, true);

        ArmorStand armorStand2 = FfaGames.getWorld().spawn(FfaGames, ArmorStand.class);
        armorStandSettings(armorStand2, FfaGamesSize, true);

        Location exploc = expRanking.clone();
        Location coinloc = coinRanking.clone();
        Location athleticloc = athleticRanking.clone();
        double lineSpacing = 0.25;

        for (Component lines: exp) {
            ArmorStand as = exploc.getWorld().spawn(exploc, ArmorStand.class);
            armorStandSettings(as, lines, false);
            exploc.add(0, -lineSpacing, 0);
        }

        for (Component lines: coin) {
            ArmorStand as = coinloc.getWorld().spawn(coinloc, ArmorStand.class);
            armorStandSettings(as, lines, false);
            coinloc.add(0, -lineSpacing, 0);
        }

        for (Component lines: athletic) {
            ArmorStand as = athleticloc.getWorld().spawn(athleticloc, ArmorStand.class);
            armorStandSettings(as, lines, false);
            athleticloc.add(0, -lineSpacing, 0);
        }

        AthleticProperties.showAllText();
        System.out.println("showText");
    }

    public static void removeAllText() {
        for (Entity entity: world.getEntities()) {
            if (entity instanceof ArmorStand) {
                entity.remove();
            }
        }
    }

    public static void armorStandSettings(ArmorStand as, Component text, boolean marker) {
        as.setBasePlate(false);
        as.setCustomNameVisible(true);
        as.customName(text);
        as.setArms(false);
        as.setVisible(false);
        as.setInvulnerable(true);
        as.setCanPickupItems(false);
        as.setGravity(false);
        as.setMarker(marker);
    }
}