package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.ffaGames.SpleefActivities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class blockBreakEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public blockBreakEvent(PvpWorld plugin) {
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
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        Material material = e.getBlock().getType();
        if (this.world != world) return;
        if (material == null) return;
        if (Config.AdminBuildModeList.contains(playerName)) return;
        if (SpleefActivities.spleefPlayingList.contains(playerName)) {
            ItemStack itemStack = new ItemStack(Material.DIAMOND_SHOVEL, 1);
            if (player.getItemInHand().equals(itemStack)) {
                e.getBlock().setType(Material.AIR);
                SpleefActivities.snowBallAction(player);
                Location location = e.getBlock().getLocation();
                SpleefActivities.locationList.add(location);
            }
        } else {
            e.setCancelled(true);
            player.sendMessage("地形は破壊できません!");
        }
    }
}
