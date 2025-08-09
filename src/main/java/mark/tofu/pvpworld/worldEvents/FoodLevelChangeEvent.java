package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FoodLevelChangeEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public FoodLevelChangeEvent(PvpWorld plugin) {
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
    public void onFoodLevelChangeEvent(org.bukkit.event.entity.FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player player = ((Player) entity).getPlayer();
            if (player == null) return;
            player.setFoodLevel(20);
        }
    }
}
