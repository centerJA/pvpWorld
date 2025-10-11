package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

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
        } else if (Config.FreePvpPlayerList.contains(playerName)) {
            Player killedPlayer = e.getEntity().getKiller();
            if (killedPlayer == null) {
                player.sendMessage("死んでしまった!!");
                Config.clearInventory(player);
            } else {
                for (String PlayerName: Config.FreePvpPlayerList) {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(ChatColor.GOLD + PlayerName + ChatColor.WHITE + "は" + killedPlayer.getName() + "に殺されてしまった!!");
                }
                killedPlayer.getInventory().addItem(Config.itemMeta("金リンゴ", Material.GOLDEN_APPLE, 1));
                killedPlayer.sendMessage("金リンゴを入手しました");
            }
        }
    }
}
