package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityDamageEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public EntityDamageEvent(PvpWorld plugin) {
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
    public void onEntityDamageEvent(org.bukkit.event.entity.EntityDamageEvent e) {
        Entity entity = e.getEntity();
        World world = entity.getWorld();
        if (this.world != world) return;
        for (String PlayerName: Config.doNotReciveDamageList) {
            e.setCancelled(true);
        }
    }
}
