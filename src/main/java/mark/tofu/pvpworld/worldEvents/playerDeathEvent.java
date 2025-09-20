package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class playerDeathEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public playerDeathEvent(PvpWorld plugin) {
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
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        Player player = e.getEntity();
        String playerName = player.getName();
        World world = player.getWorld();
        if (this.world != world) return;
        e.getDrops().clear();
        player.teleport(Config.lobby);
        if (Config.DoNotReceiveDamageList.contains(playerName)) {
            e.getDrops().clear();
        } else return;
    }
}
