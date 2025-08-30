package mark.tofu.pvpworld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Config {
    public static World world = Bukkit.getWorld("pvpWorld");


    public static ArrayList<String> worldAllPlayerList = new ArrayList<>(),
                                    doNotReceiveDamageList = new ArrayList<>(),
                                    SpeedRunOnHoldList = new ArrayList<>();

    public static Location lobby = new Location(world, 0.500, 5.500, -0.500, 90, 0),
                           lobbyAthleticStart = new Location(world, -28, 4, 6),
                           lobbyAthleticFinish = new Location(world, -29, 7, -1),
                           speedRunOnholdRoom = new Location(world, -78.500, 4, -1.500, 90, 0);

    public static ItemStack itemMeta(String displayName, Material material) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;
        itemMeta.setDisplayName(displayName);
        return itemStack;
    }
}
