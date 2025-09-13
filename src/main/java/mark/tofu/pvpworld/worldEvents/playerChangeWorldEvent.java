package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.io.IOException;

public class playerChangeWorldEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public playerChangeWorldEvent(PvpWorld plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getLogger().info("pvpWorld plugin loaded");
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                world = Bukkit.getWorld("pvpWorld");
            }
        }, 10L);
    }

    @EventHandler
    public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent e) throws IOException {
        Player player = e.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        if (this.world != world) {//他のワールドに移動した時
            Config.worldAllPlayerList.remove(playerName);
            Config.doNotReceiveDamageList.remove(playerName);
            Config.SpeedRunSingleOnHoldList.remove(playerName);
        } else { //自分のサーバーに来た時
          if (!Config.worldAllPlayerList.contains(playerName)) {
              Config.worldAllPlayerList.add(playerName);
          }
          if (!Config.doNotReceiveDamageList.contains(playerName)) {
              Config.doNotReceiveDamageList.add(playerName);
          }
            if (playerName.equals("InfInc") || playerName.equals("markcs11")) {
                player.sendMessage(String.valueOf(Config.worldAllPlayerList));
            }
            player.teleport(Config.lobby);
            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(20);
            player.setHealth(20);
            player.sendTitle(
                    player.getName() + ChatColor.AQUA + "さん",
                    ChatColor.AQUA + "こんにちは！",
                    20, 40, 20
            );
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.QUARTZ_BLOCK));
                }
            }, 10L);
            Bukkit.getLogger().info("finished!!");
        }
    }
}
