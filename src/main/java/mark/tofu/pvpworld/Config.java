package mark.tofu.pvpworld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class Config {
    public static World world = Bukkit.getWorld("pvpWorld");

    public static ArrayList<String> worldAllPlayerList = new ArrayList<>(),
                                    doNotReciveDamageList = new ArrayList<>();

    public static Location lobby = new Location(world, 0.500, 5.500, -0.500, 90, 0),
                           lobbyAthleticStart = new Location(world, -28, 4, 6),
                           lobbyAthleticFinish = new Location(world, -29, 7, -1);


}
