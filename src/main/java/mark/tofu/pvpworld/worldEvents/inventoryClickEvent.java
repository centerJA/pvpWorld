package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class inventoryClickEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public inventoryClickEvent(PvpWorld plugin) {
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
    public void onInventoryClickEvent(InventoryClickEvent e) {
        HumanEntity entity = e.getWhoClicked();
        World world = e.getWhoClicked().getWorld();
        ItemStack itemStack = e.getCurrentItem();
        if (this.world != world) return;
        if (itemStack == null) return;
        String displayName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        if (itemStack.getType() == Material.PAPER) {
            if (displayName.equals("SpeedRunシングルプレイ")) {
                SpeedRunAction.singleOnHoldAction(player, plugin);
            } else if (displayName.equals("SpeedRunマルチプレイ")) {
                player.sendMessage("まだアクセスできません");
                e.setCancelled(true);
                //SpeedRunAction.multiOnHoldAction(player, plugin);
            }
        }
    }
}
