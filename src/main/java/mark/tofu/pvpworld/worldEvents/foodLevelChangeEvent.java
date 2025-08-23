package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class foodLevelChangeEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public foodLevelChangeEvent(PvpWorld plugin) {
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
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();
        Player player = (Player) entity;
        if (player == null) {
            Bukkit.getLogger().info("エラーが発生しました");
            return;
        }
        World world = player.getWorld();
        if (this.world != world) return;
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                e.setCancelled(true);
            }
        },1L);
    }
}
