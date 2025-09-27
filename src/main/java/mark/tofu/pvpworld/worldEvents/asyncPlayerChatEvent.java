package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;


public class asyncPlayerChatEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public static Random random;

    public static int random1, random2, result;

    public asyncPlayerChatEvent(PvpWorld plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                world = Bukkit.getWorld("pvpWorld");
            }
        }, 10L);
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        String comment = e.toString();
        if (this.world != world) return;
        if (Config.NoWalkList.contains(player.getName())) {
            Config.compair(player, comment);
        }
    }
}
