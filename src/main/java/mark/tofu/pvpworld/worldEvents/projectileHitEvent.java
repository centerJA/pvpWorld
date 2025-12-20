package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.ffaGames.SpleefActivities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

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
    public void ProjectileHitEvent(ProjectileHitEvent e) {
        World world = e.getEntity().getWorld();
        if (this.world != world) return;
        if (!(e.getEntity() instanceof Snowball)) return;
        if (!(e.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) e.getEntity().getShooter();
        if (Config.AdminBuildModeList.contains(player.getName())) return;
        if (SpleefActivities.spleefPlayingList.contains(player.getName())) {
            if (e.getHitEntity() != null && e.getHitEntity() instanceof Player) {
            } else if (e.getHitBlock() != null && e.getHitEntity() == null) {
                Material material = e.getHitBlock().getType();
                if (material == Material.SNOW_BLOCK) {
                    e.getHitBlock().setType(Material.AIR);
                    SpleefActivities.locationList.add(e.getHitBlock().getLocation());
                }
            }
        }
    }
}
