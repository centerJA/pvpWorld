package org.tofu.pvpWorld.worldEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.ffaGames.FfaGames;
import org.tofu.pvpWorld.utils.ffaGames.InventoryUtils;
import org.tofu.pvpWorld.utils.ffaGames.SpleefActivities;
import org.tofu.pvpWorld.utils.freePvp.FreePvpUtils;
import org.tofu.pvpWorld.utils.itemStackMaker;
import org.tofu.pvpWorld.utils.lobbyAthletic.AthleticUtils;
import org.tofu.pvpWorld.utils.oneVersusOne.*;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunAction;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunActionMulti;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils;
import org.tofu.pvpWorld.utils.wellUtils.WellUtilities;
import org.bukkit.Bukkit;
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
import java.time.Duration;
import java.util.Objects;
import java.util.Random;

import static org.tofu.pvpWorld.utils.oneVersusOne.InventoryUtils.openGameListInventory;

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
                if (Math.floor(e.getClickedBlock().getLocation().getX()) == Math.floor(Config.lobbyAthleticFinish.getX()) && Math.floor(e.getClickedBlock().getLocation().getY()) == Math.floor(Config.lobbyAthleticFinish.getY()) && Math.floor(e.getClickedBlock().getZ()) == Math.floor(Config.lobbyAthleticFinish.getZ())) {
                    if (player.getLevel() == 0) {
                        player.sendMessage(textComponent.parse("<aqua>あなたのタイムは現在0です。"));
                        player.sendMessage(textComponent.parse("<aqua>もう一度アスレチックに挑戦しましょう!"));
                        return;
                    }
                    if (Config.lobbyAthleticFinish.getWorld() == null) {
                        player.sendMessage(Component.text("問題が発生しました"));
                    }
                    AthleticUtils.stopAthleticAction(player);
                }

                else if (Math.floor(e.getClickedBlock().getLocation().getX()) == Math.floor(Config.lobbyAthleticStart.getX()) && Math.floor(e.getClickedBlock().getLocation().getY()) == Math.floor(Config.lobbyAthleticStart.getY()) && Math.floor(e.getClickedBlock().getZ()) == Math.floor(Config.lobbyAthleticStart.getZ())) {
                    if(Config.lobbyAthleticStart.getWorld() == null) {
                        player.sendMessage(Component.text("問題が発生しました"));
                    }
                    AthleticUtils.startAthleticAction(player, plugin);
                }
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Config.AdminBuildModeList.contains(player.getName())) return;
            Block block = e.getClickedBlock();
            if (block == null) return;

            if (block.getType() == Material.OAK_SIGN) {
                Sign sign = (Sign) block.getState();
                if (sign == null) return;
                String[] lines = new String[4];
                if (e.getItem() != null) {
                    Material material = e.getItem().getType();
                    boolean isDye = material.name().endsWith("_DYE");
                    boolean isInk = (material == Material.INK_SAC || material == Material.GLOW_INK_SAC);
                    if (isInk || isDye) e.setCancelled(true);
                }
                for (int i = 0; i < 4; i++) {
                    lines[i] = PlainTextComponentSerializer.plainText().serialize(sign.line(i));
                }

                if (Objects.equals(lines[0], "ルール説明")) {
                    if (Objects.equals(lines[1], "SpeedRun")) {
                        if (Objects.equals(lines[2], "シングルプレイ")) {
                            SpeedRunAction.ruleDescription(player);
                        }
                    } else if (Objects.equals(lines[1], "FreePVP")) {
                        FreePvpUtils.ruleExplain(player);
                    }
                }
            } else if (block.getType() == Material.OAK_WALL_SIGN) {
                player.sendMessage(textComponent.parse("お"));
                Sign sign = (Sign) block.getState();
                if (sign == null) return;
                if (e.getItem() != null) {
                    Material material = e.getItem().getType();
                    boolean isDye = material.name().endsWith("_DYE");
                    boolean isInk = (material == Material.INK_SAC || material == Material.GLOW_INK_SAC);
                    if (isInk || isDye) e.setCancelled(true);
                    player.sendMessage(textComponent.parse("染料以外のアイテムか素手でクリックしてください!"));
                }
                String[] lines = new String[4];
                for (int i = 0; i < 4; i++) {
                    lines[i] = PlainTextComponentSerializer.plainText().serialize(sign.line(i));
                }

                if (Config.overLappingTrigger(player)) {
                    Config.overLappingMessage(player);
                    return;
                }
                if (Objects.equals(lines[0], "SpeedRunTest")) {
                    player.sendMessage(Component.text("eeeeeeeeee"));
                    SpeedRunAction.openGameListInventory(player);
                } else if (Objects.equals(lines[0], "1v1test")) {
                    openGameListInventory(player);
                    player.sendMessage(Component.text("bbbbbbjnbbjjb"));
                } else if (Objects.equals(lines[0], "FFA Games test")) {
                    player.sendMessage(Component.text("test1u124u12894"));
                    InventoryUtils.openGameListInventory(player);
                } else if (Objects.equals(lines[0], "右クリックして")) {
                    if (Objects.equals(lines[1], "あなたのスコアを")) {
                        if (Objects.equals(lines[2], "リセットします")) {
                            AthleticUtils.sendClearAthleticTimeRequest(player);
                        }
                    }
                }
            } else if (block.getType() == Material.END_PORTAL_FRAME) {
                WellUtilities.openInventory(player);
            } else if (block.getType() == Material.OAK_BUTTON) {
                SpeedRunActionMulti.checkButton(block, player, plugin);
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            Material block = player.getInventory().getItemInMainHand().getType();
            if (block == null) return;

            if (block == Material.RED_MUSHROOM) {
                Config.beforeGame(player);
                player.teleport(Config.lobby);
            } else if (block == Material.FEATHER) {
                SpeedRunAction.clickedFeather(player);
            } else if (block == Material.GOLD_BLOCK) {
                SpeedRunAction.clickedGoldBlock(player);
            } else if (block == Material.NETHER_STAR) {
                SpeedRunAction.clickedNetherStar(player);
            } else if (block == Material.RED_DYE) {
                OneVersusOneGames.clickedRed_Dye(player);
            } else if (block == Material.BLUE_DYE) {
                FfaGames.clickedBlue_Dye(player);
            }
        }
    }
}