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
            Config.clearInventory(player);
            Config.WorldAllPlayerList.remove(playerName);
            Config.DoNotReceiveDamageList.remove(playerName);
            Config.SpeedRunSingleOnHoldList.remove(playerName);
            Config.NoWalkList.remove(playerName);
            Config.SpeedRunSingleList.remove(playerName);
        } else { //自分のサーバーに来た時
          if (!Config.WorldAllPlayerList.contains(playerName)) {
              Config.WorldAllPlayerList.add(playerName);
          }
          if (!Config.DoNotReceiveDamageList.contains(playerName)) {
              Config.DoNotReceiveDamageList.add(playerName);
          }
            if (playerName.equals("InfInc") || playerName.equals("markcs11") || playerName.equals("10000m")) {
                player.sendMessage(String.valueOf(Config.WorldAllPlayerList));
            }
            player.teleport(Config.lobby);
            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(20);
            player.setHealth(20);
            Config.checkInventoryItem(player);
            player.setLevel(0);
            player.sendMessage(ChatColor.GOLD + Config.worldUpdateNotice());
            player.sendTitle(player.getName() + ChatColor.AQUA + "さん", ChatColor.AQUA + "こんにちは！", 20, 40, 20);
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
                }
            }, 10L);
            if (Config.testPlayerLastLoginTime(player)) {
                Config.playerSetExp(player, 5);
                Config.playerSetCoin(player, 3);
                player.sendMessage(ChatColor.AQUA + "最後にログイン時のexpを受け取ってから1日以上経過したので、5expと3coin獲得しました!");
                player.sendMessage(ChatColor.AQUA + "現在のあなたのexp: " + Config.getPlayerExp(player) + "exp");
                player.sendMessage(ChatColor.AQUA + "現在のあなたのcoin; " + Config.getPlayerCoin(player) + "coin");
                Config.setPlayerLastLogin(player);
            } else {
                player.sendMessage("最後にログイン時のexpを受け取ってから1日以上経っていないので、ログイン時のexpとcoinは獲得できません!");
            }
        }
    }
}
