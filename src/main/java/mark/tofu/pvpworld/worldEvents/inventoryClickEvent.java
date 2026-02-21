package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.ffaGames.FfaGames;
import mark.tofu.pvpworld.utils.ffaGames.SpleefActivities;
import mark.tofu.pvpworld.utils.oneVersusOne.OneVersusOneGames;
import mark.tofu.pvpworld.utils.oneVersusOne.SumoActivities;
import mark.tofu.pvpworld.utils.oneVersusOne.TopfightActivities;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunAction;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunActionMulti;
import mark.tofu.pvpworld.utils.wellUtils.WellUtilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
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
    public void onInventoryClickEvent(InventoryClickEvent e) throws IOException {
        HumanEntity entity = e.getWhoClicked();
        World world = e.getWhoClicked().getWorld();
        ItemStack itemStack = e.getCurrentItem();
        if (this.world != world) return;
        if (itemStack == null) return;
        String displayName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        if (itemStack.getType() == Material.PAPER) {
            if (Config.overLappingTrigger(player)) {
                Config.overLappingMessage(player);
                e.setCancelled(true);
                player.closeInventory();
                return;
            }
            if (displayName.equals("SpeedRunシングルプレイ")) {
                SpeedRunAction.singleOnHoldAction(player, plugin);
            } else if (displayName.equals("SpeedRunマルチプレイ")) {
                SpeedRunActionMulti.multiOnHoldAction(player, plugin);
            }
            //test
        } else if (itemStack.getType() == Material.LEAD) {
            if (!displayName.equals(ChatColor.YELLOW + "sumo")) return;
            Config.beforeGame(player);
            OneVersusOneGames.queueingActivities(player, e, plugin, SumoActivities.sumoQueueingList);
        } else if (itemStack.getType() == Material.IRON_BLOCK) {
            if (!displayName.equals(ChatColor.RED + "topfight")) return;
            Config.beforeGame(player);
            OneVersusOneGames.queueingActivities(player, e, plugin, TopfightActivities.topfightQueueingList);
        } else if (itemStack.getType() == Material.DIAMOND_SHOVEL) {
            if (!displayName.equals(ChatColor.GREEN + "spleef")) return;
            Config.beforeGame(player);
            FfaGames.ffaQueueingActivities(player, SpleefActivities.spleefQueueingList, plugin, e);
        } else if (itemStack.getType() == Material.GOLD_INGOT) {
            if (displayName.equals(ChatColor.RED + "小さな井戸")) {
                e.setCancelled(true);
                Config.beforeGame(player);
                WellUtilities.rollItems(player, plugin);
            }
        } else if (itemStack.getType() == Material.GOLD_BLOCK) {
            if (displayName.equals(ChatColor.DARK_PURPLE + "大きな井戸")) {
                e.setCancelled(true);
                Config.beforeGame(player);
                WellUtilities.rollItemsBIG(player, plugin);
            }
        }
    }
}
