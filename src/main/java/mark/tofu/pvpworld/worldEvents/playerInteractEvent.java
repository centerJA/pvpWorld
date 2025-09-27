package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.AthleticUtils;
import mark.tofu.pvpworld.utils.SpeedRunAction;
import mark.tofu.pvpworld.utils.SpeedRunScheduledTimer;
import mark.tofu.pvpworld.utils.SpeedRunTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.Objects;

public class playerInteractEvent implements Listener {
    PvpWorld plugin;

    private World world;

    public playerInteractEvent(PvpWorld plugin) {
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
    public void onPlayerInteractEvent(PlayerInteractEvent e) throws IOException {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if (this.world != world) return;
        if (Config.AdminBuildModeList.contains(player.getName())) return;
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
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Config.AdminBuildModeList.contains(player.getName())) return;
            Block block = e.getClickedBlock();
            if (block == null) return;
            if (block.getType() == Material.OAK_WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                if (sign == null) return;
                String[] lines = new String[4];
                for (int i = 0; i < 4; i++) {
                    lines[i] = sign.getLine(i);
                }

                if (Objects.equals(lines[0], "SpeedRunTest")) {
                    player.sendMessage("testtttttt");
                    SpeedRunAction.openGameListInventory(player);
                }
            } else if (block.getType() == Material.STONE) {

            }
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            Material block = player.getInventory().getItemInMainHand().getType();
            if (block == null) return;
            if (block == Material.RED_MUSHROOM) {
                String playerName = player.getName();
                if (Config.SpeedRunSingleOnHoldList.contains(playerName) || Config.SpeedRunSingleList.contains(playerName)) {
                    player.getInventory().clear();
                    player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM));
                    Config.SpeedRunSingleOnHoldList.remove(playerName);
                    Config.SpeedRunSingleList.remove(playerName);
                    SpeedRunTimer.stopTimer(player);
                    SpeedRunScheduledTimer.stopTimer(player);
                    player.sendMessage(ChatColor.AQUA + "SpeedRunをキャンセルしました");
                }
                player.teleport(Config.lobby);
                player.setExp(0);
            } else if (block == Material.FEATHER) {
                PotionEffect levitation = new PotionEffect(PotionEffectType.LEVITATION, 5, 1);
                player.addPotionEffect(levitation);
            }
        }
    }
}
