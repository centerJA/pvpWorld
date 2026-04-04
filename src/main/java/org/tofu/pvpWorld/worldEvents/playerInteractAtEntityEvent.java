package org.tofu.pvpWorld.worldEvents;

import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class playerInteractAtEntityEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public playerInteractAtEntityEvent(PvpWorld plugin) {
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
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand as) {
            Location location = as.getLocation();
            if (location.equals(TextDisplayUtils.expRanking) || location.equals(TextDisplayUtils.coinRanking) || location.equals(TextDisplayUtils.athleticRanking)) {
                Player player = e.getPlayer();
                TextDisplayUtils.latestRanking();
                player.sendMessage(textComponent.parse("<green>スコアボードを更新しました"));
            }
        }
    }
}
