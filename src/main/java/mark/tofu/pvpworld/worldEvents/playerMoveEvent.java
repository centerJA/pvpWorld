package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.ffaGanes.SpleefActivities;
import mark.tofu.pvpworld.utils.freePvp.FreePvpUtils;
import mark.tofu.pvpworld.utils.oneVersusOne.OneVersusOneGames;
import mark.tofu.pvpworld.utils.oneVersusOne.SumoActivities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class playerMoveEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public playerMoveEvent(PvpWorld plugin) {
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
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (this.world != world) return;
        if (Config.NoWalkList.contains(player.getName())) {
            e.setCancelled(true);
        }
        if (Config.AdminBuildModeList.contains(player.getName())) return;
        Material type = player.getLocation().getBlock().getType();
        if (type.equals(Material.TRIPWIRE)) {
            if (OneVersusOneGames.player1v1GamesContainsCheck(player)) {
                OneVersusOneGames.overlappingGames(player);
                player.teleport(Config.lobby);
                return;
            }
            FreePvpUtils.joinAction(player, plugin);
        } else if (type.equals(Material.WATER)) {
            if (SumoActivities.sumoQueueingList.contains(player.getName())) {
                SumoActivities.sumoCloseAction(player, plugin);
            } else if (SpleefActivities.spleefQueueingList.contains(player.getName())) {
                SpleefActivities.spleefCloseAction(player, plugin);
            }
        }
    }
}
