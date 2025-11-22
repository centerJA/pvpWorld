package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
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

import static mark.tofu.pvpworld.utils.oneVersusOne.OneVersusOneGames.sumoQueueingList;
import static mark.tofu.pvpworld.utils.oneVersusOne.OneVersusOneGames.sumoStartAction;

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
        } else if (itemStack.getType() == Material.LEAD) {
            if (displayName.equals("sumo")) {
                if (sumoQueueingList.isEmpty()) {
                    sumoQueueingList.add(player.getName());
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("他の人を待っています...");
                    player.sendMessage("参加をやめるには、インベントリの中の赤色の染料を右クリックしてください");
                    player.getInventory().setItem(8, Config.itemMeta("ゲームをやめる", Material.RED_DYE, 1));
                } else if (sumoQueueingList.size() == 1) {
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("相手が見つかりました!");
                    sumoStartAction(player, plugin);
                } else {
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("既に誰かがプレイ中です");
                }
            }
        }
    }
}
