package org.tofu.pvpWorld.worldEvents;

import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.ffaGames.SpleefActivities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunActionMulti;

public class projectileHitEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public projectileHitEvent(PvpWorld plugin) {
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
    public void onProjectileHit(ProjectileHitEvent e) {
        World hitWorld = e.getEntity().getWorld();
        if (this.world != hitWorld) return;

        if (!(e.getEntity().getShooter() instanceof Player player)) return;

        if (e.getEntity() instanceof Snowball) {
            if (Config.AdminBuildModeList.contains(player.getName())) return;
            if (SpleefActivities.spleefPlayingList.contains(player.getName())) {
                if (e.getHitEntity() != null && e.getHitEntity() instanceof Player) {
                    // 何もしない
                } else if (e.getHitBlock() != null && e.getHitEntity() == null) {
                    Material material = e.getHitBlock().getType();
                    if (material == Material.SNOW_BLOCK) {
                        e.getHitBlock().setType(Material.AIR);
                        SpleefActivities.locationList.add(e.getHitBlock().getLocation());
                    }
                }
            } else if (SpeedRunActionMulti.multiPlayingList.contains(player.getName())) {
                Block hitBlock = e.getHitBlock();
                if (hitBlock != null && hitBlock.getType() == Material.TARGET) {
                    e.getEntity().remove();
                    SpeedRunActionMulti.checkSnowBallInfo(e.getHitBlock().getLocation(), player, plugin);
                }
            }
            return;
        }
    }
}