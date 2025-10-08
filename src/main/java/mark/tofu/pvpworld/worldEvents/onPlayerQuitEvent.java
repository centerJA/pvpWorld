package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onPlayerQuitEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public onPlayerQuitEvent(PvpWorld plugin) {
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
    public void onOnPlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        if (this.world != world) return;
        Config.WorldAllPlayerList.remove(playerName);
        Config.DoNotReceiveDamageList.remove(playerName);
        Config.SpeedRunSingleOnHoldList.remove(playerName);
        Config.SpeedRunSingleList.remove(playerName);
        Config.FreePvpPlayerList.remove(playerName);
    }
}
