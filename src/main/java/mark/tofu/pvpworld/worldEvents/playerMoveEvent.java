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
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

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
        } else if (player.getLocation().getBlock().getType().equals(Material.TRIPWIRE)) {
            if (!Config.FreePvpPlayerList.contains(player.getName())) {
                Config.FreePvpPlayerList.add(player.getName());
                Config.clearInventory(player);
                player.getInventory().setItem(0, Config.itemMeta("聖なる剣", Material.IRON_SWORD, 1));
                player.getInventory().setItem(1, Config.itemMeta("弓", Material.BOW, 1));
                player.getInventory().setItem(8, Config.itemMeta("矢", Material.ARROW, 8));
                player.getInventory().setItem(38, Config.itemMeta("チェストプレート", Material.CHAINMAIL_CHESTPLATE, 1));
                player.getInventory().setItem(37, Config.itemMeta("レギンス", Material.CHAINMAIL_LEGGINGS, 1));
                player.getInventory().setItem(36, Config.itemMeta("ブーツ", Material.CHAINMAIL_BOOTS, 1));
                player.getInventory().setItem(12, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
                player.sendMessage("ロビーに戻るにはインベントリ内のアイテムを使用してください");
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Config.DoNotReceiveDamageList.remove(player.getName());
                    }
                }, 60L);
                for (String PlayerName: Config.WorldAllPlayerList) {
                    Player player2 = Bukkit.getPlayer(PlayerName);
                    Objects.requireNonNull(player2).sendMessage(ChatColor.GOLD + player.getName() + "さんがFree PVPスペースに参加しました");
                }
                if (Config.FreePvpPlayerList.isEmpty()) {
                    player.sendMessage("現在誰もいません");
                }
            }
        }
    }
}
