package mark.tofu.pvpworld.utils.textDisplay;

import mark.tofu.pvpworld.utils.yamlProperties.athleticTimeUtils;
import mark.tofu.pvpworld.utils.yamlProperties.coinUtils;
import mark.tofu.pvpworld.utils.yamlProperties.expUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class TextDisplayUtils {
    public static Location SpeedRun, OneVersusOne, FfaGames, expRanking, coinRanking, athleticRanking;

    public static String SpeedRunSize, OneVersusOneSize, FfaGamesSize;

    public static List<String> exp, coin, athletic;

    private static World world;

    public static void locationSetUp() {
        world = Bukkit.getWorld("pvpWorld");
        SpeedRun = new Location(world, -18.500, 7.000, -0.500);
        OneVersusOne = new Location(world, 0.500, 7.000, -19.500);
        FfaGames = new Location(world, 0.500, 7.000, 18.500);
        SpeedRunSize = ChatColor.GOLD + "0" + ChatColor.WHITE + "人がプレイ中!";
        OneVersusOneSize = ChatColor.GOLD + "0" + ChatColor.WHITE + "人がプレイ中!";
        FfaGamesSize = ChatColor.GOLD + "0" + ChatColor.WHITE + "人がプレイ中!";
        expRanking = new Location(world, -5.500, 8.000, 44.000);
        coinRanking = new Location(world, 6.500, 8.000, 44.000);
        athleticRanking = new Location(world, -5.500, 8.000, 55.000);
        exp = new ArrayList<>();
        exp.add(ChatColor.GREEN + "EXP" + ChatColor.RED + "ランキング");
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
        exp.add(ChatColor.GREEN + "クリックして更新");

        coin = new ArrayList<>();
        coin.add(ChatColor.GOLD + "COIN" + ChatColor.RED + "ランキング");
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
        coin.add(ChatColor.GREEN + "クリックして更新");

        athletic = new ArrayList<>();
        athletic.add(ChatColor.YELLOW + "Athletic" + ChatColor.RED + "ランキング");
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
        athletic.add(ChatColor.GREEN + "クリックして更新");

        System.out.println("setup finished");
        showAllText();
    }

    public static void renameSpeedRun(int size) {
        SpeedRunSize = ChatColor.GOLD + String.valueOf(size) + ChatColor.WHITE + "人がプレイ中!";
        showAllText();
    }

    public static void renameOneVersusOneSize(int size) {
        OneVersusOneSize = ChatColor.GOLD + String.valueOf(size) + ChatColor.WHITE + "人がプレイ中!";
        showAllText();
    }

    public static void renameFfaGamesSize(int size) {
        FfaGamesSize = ChatColor.GOLD + String.valueOf(size) + ChatColor.WHITE + "人がプレイ中!";
        showAllText();
    }



    public static void showAllText() {
        removeAllText();
        ArmorStand armorStand = SpeedRun.getWorld().spawn(SpeedRun, ArmorStand.class);
        armorStandSettings(armorStand, SpeedRunSize);

        ArmorStand armorStand1 = OneVersusOne.getWorld().spawn(OneVersusOne, ArmorStand.class);
        armorStandSettings(armorStand1, OneVersusOneSize);

        ArmorStand armorStand2 = FfaGames.getWorld().spawn(FfaGames, ArmorStand.class);
        armorStandSettings(armorStand2, FfaGamesSize);

        Location exploc = expRanking.clone();
        Location coinloc = coinRanking.clone();
        Location athleticloc = athleticRanking.clone();
        double lineSpacing = 0.25;

        for (String lines: exp) {
            ArmorStand as = exploc.getWorld().spawn(exploc, ArmorStand.class);
            armorStandSettings(as, lines);
            exploc.add(0, -lineSpacing, 0);
        }

        for (String lines: coin) {
            ArmorStand as = coinloc.getWorld().spawn(coinloc, ArmorStand.class);
            armorStandSettings(as, lines);
            coinloc.add(0, -lineSpacing, 0);
        }

        for (String lines: athletic) {
            ArmorStand as = athleticloc.getWorld().spawn(athleticloc, ArmorStand.class);
            armorStandSettings(as, lines);
            athleticloc.add(0, -lineSpacing, 0);
        }

        System.out.println("showText");
    }


    public static void removeAllText() {
        for (Entity entity: world.getEntities()) {
            if (entity instanceof ArmorStand) {
                entity.remove();
            }
        }
    }

    public static void armorStandSettings(ArmorStand as, String text) {
        as.setBasePlate(false);
        as.setCustomNameVisible(true);
        as.setCustomName(text);
        as.setArms(false);
        as.setVisible(false);
        as.setInvulnerable(true);
        as.setCanPickupItems(false);
        as.setGravity(false);
        as.setMarker(true);
        System.out.println("armorSettings finished");
    }


}
