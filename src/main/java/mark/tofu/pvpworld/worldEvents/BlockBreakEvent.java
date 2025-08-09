package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockBreakEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public BlockBreakEvent(PvpWorld plugin) {
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
    public void onBlockBreakEvent(org.bukkit.event.block.BlockBreakEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        Material material = e.getBlock().getType();
        if (this.world != world) return;
        if (material == null) return;
        if (playerName.equals("markcs11") || playerName.equals("InfInc")) {
            return;
        } else {
            e.setCancelled(true);
            player.sendMessage("地形は破壊できません!");
        }
    }
}
