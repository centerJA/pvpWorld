package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class blockPlaceEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public blockPlaceEvent(PvpWorld plugin) {
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
    public void onBlockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (this.world != world) return;
        String playerName = player.getName();
        if (Config.WorldAllPlayerList.contains(playerName)) {
            if (!Config.AdminBuildModeList.contains(playerName)) {
                e.setCancelled(true);
                player.sendMessage("ブロックは置けません!");
            }

        } else {
            player.sendMessage("エラーが発生しました");
        }

    }
}
