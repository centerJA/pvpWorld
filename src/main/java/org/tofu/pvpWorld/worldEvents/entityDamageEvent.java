package org.tofu.pvpWorld.worldEvents;

import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.ffaGames.SpleefActivities;
import org.tofu.pvpWorld.utils.oneVersusOne.SumoActivities;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class entityDamageEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public entityDamageEvent(PvpWorld plugin) {
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
    public void onEntityDamageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        World world = entity.getWorld();
        if (this.world != world) return;
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.sendMessage("get damage");

            if (Config.DoNotReceiveDamageList.contains(player.getName())) {
                player.sendMessage("cancel-1");
                e.setCancelled(true);
                player.sendMessage("cancel");
            } else {
                if (SumoActivities.sumoQueueingList.contains(player.getName()) || SpleefActivities.spleefQueueingList.contains(player.getName())) {
                    e.setDamage(0);
                } else if (SpleefActivities.spleefPlayingList.contains(player.getName())) {
                    e.setDamage(0.1);
                }
            }
        }
    }
}
