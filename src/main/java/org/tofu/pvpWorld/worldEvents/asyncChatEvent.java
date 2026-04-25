package org.tofu.pvpWorld.worldEvents;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;

public class asyncChatEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public asyncChatEvent(PvpWorld plugin) {
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
    public void onAsyncChatEvent(AsyncChatEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (this.world != world) return;
        String comment = PlainTextComponentSerializer.plainText().serialize(e.message()).trim();
        if (Config.NoWalkList.contains(player.getName())) {
            Config.compair(player, comment);
            e.setCancelled(true);
        }
    }
}
