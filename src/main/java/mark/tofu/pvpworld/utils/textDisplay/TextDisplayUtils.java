package mark.tofu.pvpworld.utils.textDisplay;

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
    public static Location SpeedRun, OneVersusOne, FfaGames, expRanking, coinRanking;

    public static String SpeedRunSize, OneVersusOneSize, FfaGamesSize;

    public static List<String> exp, coin;

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
        exp = new ArrayList<>();
        exp.add(ChatColor.GREEN + "EXP" + ChatColor.GOLD + "ランキング");
        exp.add(ChatColor.RED + "近日公開");
        coin = new ArrayList<>();
        coin.add(ChatColor.GOLD + "COIN" + ChatColor.GOLD + "ランキング");
        coin.add(ChatColor.RED + "近日公開");
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

        ArmorStand armorStand3 = expRanking.getWorld().spawn(expRanking, ArmorStand.class);
        Location exploc = expRanking.clone();
        Location coinloc = coinRanking.clone();
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
