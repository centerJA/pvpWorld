package org.tofu.pvpWorld.worldEvents;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.units.qual.A;
import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.ffaGames.SpleefActivities;
import org.tofu.pvpWorld.utils.freePvp.FreePvpUtils;
import org.tofu.pvpWorld.utils.oneVersusOne.SumoActivities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunActionMulti;
import org.tofu.pvpWorld.utils.textComponent;

import java.io.IOException;
import java.util.ArrayList;

public class playerMoveEvent implements Listener {
    PvpWorld plugin;

    private World world;

    private ArrayList<String> freePvpException = new ArrayList<>();

    public playerMoveEvent(PvpWorld plugin) {
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
    public void onPlayerMoveEvent(PlayerMoveEvent e) throws IOException {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (this.world != world) return;
        if (Config.NoWalkList.contains(player.getName())) {
            e.setCancelled(true);
        }
        if (Config.AdminBuildModeList.contains(player.getName())) return;
        Material type = player.getLocation().getBlock().getType();
        if (type.equals(Material.TRIPWIRE)) {
            if (SpeedRunActionMulti.multiPlayingList.contains(player.getName())) return;
            if (Config.overLappingTrigger(player)) {
                if (freePvpException.contains(player.getName())) return;
                Config.overLappingMessage(player);
                player.teleport(Config.lobby);
            } else {
                FreePvpUtils.joinAction(player, plugin);
                freePvpException.add(player.getName());
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        freePvpException.remove(player.getName());
                    }
                }, 20L);
            }
        } else if (type.equals(Material.WATER)) {
            if (SumoActivities.sumoQueueingList.contains(player.getName())) {
                SumoActivities.sumoCloseAction(player, plugin);
            } else if (SpleefActivities.spleefPlayingList.contains(player.getName())) {
                SpleefActivities.voidAction(player, plugin);
            }
        }
    }
}
