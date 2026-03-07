package org.tofu.pvpWorld.worldEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.scoreBoard.ScoreBoardUtils;
import org.tofu.pvpWorld.utils.textComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.tofu.pvpWorld.utils.yamlProperties.playerAdminList;

import java.io.IOException;
import java.time.Duration;

import static org.tofu.pvpWorld.utils.yamlProperties.coinUtils.getPlayerCoin;
import static org.tofu.pvpWorld.utils.yamlProperties.coinUtils.playerSetCoin;
import static org.tofu.pvpWorld.utils.yamlProperties.expUtils.getPlayerExp;
import static org.tofu.pvpWorld.utils.yamlProperties.expUtils.playerSetExp;

public class playerChangeWorldEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public playerChangeWorldEvent(PvpWorld plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("pvpWorld plugin loaded");
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

        if (this.world != world) {
            Config.clearInventory(player);
            ScoreBoardUtils.removeScoreBoard(player);
            Config.WorldAllPlayerList.remove(playerName);
            Config.DoNotReceiveDamageList.remove(playerName);
            Config.SpeedRunSingleOnHoldList.remove(playerName);
            Config.NoWalkList.remove(playerName);
            Config.SpeedRunSingleList.remove(playerName);
        } else {
            if (!Config.WorldAllPlayerList.contains(playerName)) {
                Config.WorldAllPlayerList.add(playerName);
            }
            if (!Config.DoNotReceiveDamageList.contains(playerName)) {
                Config.DoNotReceiveDamageList.add(playerName);
            }
            if (playerAdminList.playerHasAdmin(player)) {
                player.sendMessage(Component.text(String.valueOf(Config.WorldAllPlayerList)));
            }

            player.teleport(Config.lobby);
            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(20);
            player.setHealth(20);
            Config.checkInventoryItem(player);
            player.setLevel(0);

            player.sendMessage(textComponent.parse("<gold>" + Config.worldUpdateNotice()));

            Title.Times times = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1));
            Title title = Title.title(textComponent.parse(player.getName() + "<aqua>さん"), textComponent.parse("<aqua>こんにちは！"), times);
            player.showTitle(title);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
                    ScoreBoardUtils.updateScoreBoard(player);
                }
            }, 10L);

            if (Config.testPlayerLastLoginTime(player)) {
                playerSetExp(player, 10);
                playerSetCoin(player, 7);
                player.sendMessage(textComponent.parse("<aqua>最後にログイン時のexpを受け取ってから1日以上経過したので、5expと3coinを獲得しました!"));
                player.sendMessage(textComponent.parse("<aqua>現在のあなたのexp: " + getPlayerExp(player) + "exp"));
                player.sendMessage(textComponent.parse("<aqua>現在のあなたのcoin: " + getPlayerCoin(player) + "coin"));
                Config.setPlayerLastLogin(player);
            } else {
                player.sendMessage(textComponent.parse("最後にログイン時のexpとcoinを受け取ってから1日以上経っていないので、ログイン時のexpとcoinは獲得できません!"));
            }
        }
    }
}