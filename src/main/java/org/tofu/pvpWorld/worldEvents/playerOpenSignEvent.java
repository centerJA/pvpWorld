package org.tofu.pvpWorld.worldEvents;


import io.papermc.paper.event.player.PlayerOpenSignEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.tofu.pvpWorld.PvpWorld;

public class playerOpenSignEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public playerOpenSignEvent(PvpWorld plugin) {
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
    public void onPlayerOpenSignEvent(PlayerOpenSignEvent e){
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (this.world != world) return;
        e.setCancelled(true);
    }

}
