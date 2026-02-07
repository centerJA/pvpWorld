package mark.tofu.pvpworld.utils.athletic;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.textDisplay.TextDisplayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AthleticProperties {
    public static Location boxDescription,
                           boxRankingLoc;

    public static List<String> boxDesc = new ArrayList<>(),
                               boxRanking;

    public static FileConfiguration atheticBoxData;

    public static File athleticBoxFile;

    public static List<Map.Entry<String, Integer>> entryList = new ArrayList<>();

    public static void setup() {
        boxDescription = new Location(Config.world, 79.500, 6.000, 145.500);
        boxRankingLoc = new Location(Config.world, 75.500, 6.000, 145.500);
        boxDesc.add("箱の中でアスレチックをします!");
        boxDesc.add("難易度: " + ChatColor.GREEN + "低");

        updateRanking();
    }

    public static void updateRanking() {
        boxRanking = new ArrayList<>();
        boxRanking.add(ChatColor.YELLOW + "Box");

        showAllText();
    }

    public static void showAllText() {
        double lineSpacing = 0.25;
        Location boxDescLocc = boxDescription.clone();
        Location boxRankLocc = boxRankingLoc.clone();
        for (String lines: boxDesc) {
            ArmorStand as = boxDescLocc.getWorld().spawn(boxDescLocc, ArmorStand.class);
            armorStandSettings(as, lines);
            boxDescLocc.add(0, -lineSpacing, 0);
        }

        for (String lines: boxRanking) {
            ArmorStand as = boxRankLocc.getWorld().spawn(boxRankLocc, ArmorStand.class);
            armorStandSettings(as, lines);
            boxRankLocc.add(0, -lineSpacing, 0);
        }
    }


    public static void athleticYamlSetup(PvpWorld plugin) {
        athleticBoxFile = new File(plugin.getDataFolder(), "athletic.yml");
        if (!athleticBoxFile.exists()) {
            try {
                athleticBoxFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        atheticBoxData = YamlConfiguration.loadConfiguration(athleticBoxFile);
    }

    public static int getAthleticTime(FileConfiguration fileConfiguration, Player player, String fileName) {
        String path = fileName + player.getUniqueId().toString();
        if (fileConfiguration.contains(path)) {
            return fileConfiguration.getInt(path);
        } else {
            return 10000;
        }
    }

    public static boolean setAthleticTime(FileConfiguration fileConfiguration, int time, Player player) {
        if (fileConfiguration.equals(atheticBoxData)) {
            if (getAthleticTime(atheticBoxData, player, "Box.") >= time) {
                atheticBoxData.set("Box." + String.valueOf(player.getUniqueId()), time);
                try {
                    atheticBoxData.save(athleticBoxFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } else return false;
        } else return false;
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
        as.setMarker(false);
    }

    public static void removeAllText() {
        for (Entity entity: Config.world.getEntities()) {
            if (entity instanceof ArmorStand) {
                entity.remove();
            }
        }
    }
}
