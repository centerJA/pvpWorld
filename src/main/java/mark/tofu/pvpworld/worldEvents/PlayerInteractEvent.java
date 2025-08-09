package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.AthleticUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import java.io.IOException;
import java.util.Objects;

public class PlayerInteractEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public PlayerInteractEvent(PvpWorld plugin) {
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
    public void onPlayerInteractEvent(org.bukkit.event.player.PlayerInteractEvent e) throws IOException {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (this.world != world) return;
//        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
//            Block block = e.getClickedBlock();
//            if (block == null) return;
//            if (block.getType() == Material.OAK_WALL_SIGN) {
//                Sign sign = (Sign) block.getState();
//                if (sign == null) return;
//            }
        if (e.getAction().equals(Action.PHYSICAL)) {
            if(Objects.requireNonNull(e.getClickedBlock()).getType() == Material.STONE_PRESSURE_PLATE) {
                if (Math.floor(e.getClickedBlock().getLocation().getX()) == Math.floor(Config.lobbyAthleticFinish.getX()) && Math.floor(e.getClickedBlock().getLocation().getY()) == Math.floor(Config.lobbyAthleticFinish.getY()) && Math.floor(e.getClickedBlock().getY()) == Math.floor(Config.lobbyAthleticFinish.getY())) {
                    if (player.getLevel() == 0) {
                        player.sendMessage(ChatColor.AQUA + "あなたのタイムは現在0です。");
                        player.sendMessage(ChatColor.AQUA + "もう一度アスレチックに挑戦しましょう!");
                        return;
                    }
                    if (Config.lobbyAthleticFinish.getWorld() == null) {
                        player.sendMessage("問題が発生しました");
                    }
                    AthleticUtils.stopAthleticAction(player);
                }

                else if (Math.floor(e.getClickedBlock().getLocation().getX()) == Math.floor(Config.lobbyAthleticStart.getX()) && Math.floor(e.getClickedBlock().getLocation().getY()) == Math.floor(Config.lobbyAthleticStart.getY()) && Math.floor(e.getClickedBlock().getY()) == Math.floor(Config.lobbyAthleticStart.getY())) {
                    if(Config.lobbyAthleticStart.getWorld() == null) {
                        player.sendMessage("問題が発生しました");
                    }
                    AthleticUtils.startAthleticAction(player, plugin);
                }
            }
        }
    }
}
