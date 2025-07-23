package mark.tofu.pvpworld.worldOptions;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.ArrayList;

public class PlayerChangeWorldEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public static ArrayList<String> worldAllPlayerList;
    public PlayerChangeWorldEvent(PvpWorld plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getLogger().info("pvpWorld plugin loaded");
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                world = Bukkit.getWorld("pvpWorld");
            }
        }, 10L);
        worldAllPlayerList = new ArrayList<>();
    }

    @EventHandler
    public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        if (this.world != world) {
            if(worldAllPlayerList.contains(playerName)) {
                worldAllPlayerList.remove(playerName);
            }
        } else {
            if (!worldAllPlayerList.contains(playerName)) {
                worldAllPlayerList.add(playerName);
            }
            player.sendMessage(String.valueOf(worldAllPlayerList));
        }
    }
}
