package org.tofu.pvpWorld.worldEvents;

import org.bukkit.Tag;
import org.bukkit.entity.Arrow;
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
        System.out.println("?");
        if (this.world != hitWorld) return;
        System.out.println("aaaaaa");
        if (!(e.getEntity().getShooter() instanceof Player player)) return;
        System.out.println("01001010");
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
            }
        } else if (e.getEntity() instanceof Arrow) {
            System.out.println("detected");
            if (SpeedRunActionMulti.multiPlayingList.contains(player.getName())) {
                System.out.println("contains");
                Block hitBlock = e.getHitBlock();
                if (hitBlock == null) System.out.println("nullll");
                else {
                    System.out.println("not null");
                    System.out.println(hitBlock.getType());
                }
                if (hitBlock != null && hitBlock.getType().equals(Material.WHITE_WOOL)) {
                    System.out.println("okay");
                    e.getEntity().remove();
                    SpeedRunActionMulti.checkArrowInfo(e.getHitBlock().getLocation(), player, plugin);
                }
            }
        }
    }
}