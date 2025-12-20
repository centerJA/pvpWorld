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
        System.out.println("3333");
        if (this.world != world) return;
        System.out.println("198282822828");
        if (!(e.getEntity() instanceof Snowball)) return;
        System.out.println("0000000000");
        if (!(e.getEntity().getShooter() instanceof Player)) return;
        System.out.println("???");
        Player player = (Player) e.getEntity().getShooter();
        player.sendMessage("1111111111");
        if (Config.AdminBuildModeList.contains(player.getName())) return;
        player.sendMessage("aaaaaaaaaaa");
        if (SpleefActivities.spleefPlayingList.contains(player.getName())) {
            player.sendMessage("aqrhri2hqrl");
            if (e.getHitEntity() != null && e.getHitEntity() instanceof Player) {
                player.sendMessage("hittt");
            } else if (e.getHitBlock() != null && e.getHitEntity() == null) {
                player.sendMessage("28292");
                Material material = e.getHitBlock().getType();
                if (material == Material.SNOW_BLOCK) {
                    e.getHitBlock().setType(Material.AIR);
                    SpleefActivities.locationList.add(e.getHitBlock().getLocation());
                }
            }
        }
    }
}
