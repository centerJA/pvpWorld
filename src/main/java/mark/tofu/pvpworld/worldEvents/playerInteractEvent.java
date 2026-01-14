package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.athletic.AthleticTimer;
import mark.tofu.pvpworld.utils.athletic.AthleticUtils;
import mark.tofu.pvpworld.utils.ffaGames.FfaGames;
import mark.tofu.pvpworld.utils.ffaGames.InventoryUtils;
import mark.tofu.pvpworld.utils.ffaGames.SpleefActivities;
import mark.tofu.pvpworld.utils.freePvp.FreePvpUtils;
import mark.tofu.pvpworld.utils.oneVersusOne.*;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunAction;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunScheduledTimer;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunTimer;
import mark.tofu.pvpworld.utils.wellUtils.WellUtilities;
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
import java.util.Random;

import static mark.tofu.pvpworld.utils.oneVersusOne.InventoryUtils.openGameListInventory;

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
                        player.sendMessage(ChatColor.AQUA + "あなたのタイムは現在0です。");
                        player.sendMessage(ChatColor.AQUA + "もう一度アスレチックに挑戦しましょう!");
                        return;
                    }
                    if (Config.lobbyAthleticFinish.getWorld() == null) {
                        player.sendMessage("問題が発生しました");
                    }
                    AthleticUtils.stopAthleticAction(player);
                }

                else if (Math.floor(e.getClickedBlock().getLocation().getX()) == Math.floor(Config.lobbyAthleticStart.getX()) && Math.floor(e.getClickedBlock().getLocation().getY()) == Math.floor(Config.lobbyAthleticStart.getY()) && Math.floor(e.getClickedBlock().getZ()) == Math.floor(Config.lobbyAthleticStart.getZ())) {
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
            if (block.getType() == Material.OAK_SIGN) {
                Sign sign = (Sign) block.getState();
                if (sign == null) return;
                String[] lines = new String[4];
                for (int i = 0; i < 4; i++) {
                    lines[i] = sign.getLine(i);
                }

                if (Objects.equals(lines[0], "ルール説明")) {
                    if (Objects.equals(lines[1], "SpeedRun")) {
                        if (Objects.equals(lines[2], "シングルプレイ")) { //SpeedRun Single内の待機所のルール説明
                            player.sendMessage(ChatColor.AQUA + "-----SpeedRunシングルプレイ-----");
                            player.sendMessage("このゲームは、アスレチックを走り抜けてゴールにあるボタンを押す速さを争うゲームです!");
                            player.sendMessage("でも、ただアスレチックをするだけではありません!");
                            player.sendMessage(ChatColor.YELLOW + "10秒に1回ランダムでイベントが発生します!!");
                            player.sendMessage(ChatColor.GREEN + "歩く速さが速く" + ChatColor.WHITE + "なったり、" + ChatColor.RED + "周りが見えなく" + ChatColor.WHITE + "なったり...");
                            player.sendMessage("リーダーボードも作る予定です!");
                            player.sendMessage(ChatColor.AQUA + "---------------------------");
                        }
                    } else if (Objects.equals(lines[1], "FreePVP")) {
                        FreePvpUtils.ruleExplain(player);
                    }
                }
            }
            if (block.getType() == Material.OAK_WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                if (sign == null) return;
                String[] lines = new String[4];
                for (int i = 0; i < 4; i++) {
                    lines[i] = sign.getLine(i);
                }

                if (Config.overLappingTrigger(player)) {
                    Config.overLappingMessage(player);
                    return;
                }

                if (e.getItem() != null && e.getItem().getType().name().endsWith("_DYE")) {
                    e.setCancelled(true);
                    player.sendMessage("染料以外のアイテムや、素手で看板をクリックしてください!");
                }

                if (Objects.equals(lines[0], "SpeedRunTest")) { //SpeedRunのメニューを表示させる
                    SpeedRunAction.openGameListInventory(player);
                } else if (Objects.equals(lines[0], "1v1test")) {
                    openGameListInventory(player);
                } else if (Objects.equals(lines[0], "FFA Games test")) {
                    player.sendMessage("test1u124u12894");
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
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            Material block = player.getInventory().getItemInMainHand().getType();
            if (block == null) return;
            if (block == Material.RED_MUSHROOM) {
                String playerName = player.getName();
                if (Config.SpeedRunSingleOnHoldList.contains(playerName) || Config.SpeedRunSingleList.contains(playerName)) {
                    Config.SpeedRunSingleOnHoldList.remove(playerName);
                    Config.SpeedRunSingleList.remove(playerName);
                    SpeedRunTimer.stopTimer(player);
                    SpeedRunScheduledTimer.stopTimer(player);
                    player.sendMessage(ChatColor.AQUA + "SpeedRunをキャンセルしました");
                }
                StartTimerUtils.stopTimer(player);
                TimeUpTimer.stopTimer(player);
                Config.clearInventory(player);
                player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
                player.teleport(Config.lobby);
                player.setLevel(0);
                AthleticTimer.stopTimer(player);
                if (Config.FreePvpPlayerList.contains(playerName)) {
                    Config.FreePvpPlayerList.remove(playerName);
                    Config.DoNotReceiveDamageList.add(playerName);
                    player.sendMessage("Free PVPを退出しました");
                }
            } else if (block == Material.FEATHER) {
                PotionEffect levitation = new PotionEffect(PotionEffectType.LEVITATION, 100, 1);
                player.addPotionEffect(levitation);
                if (player.getItemInHand().getType().equals(Material.FEATHER)) {
                    player.setItemInHand(null);
                    player.sendMessage("使用しました!");
                }
            } else if (block == Material.GOLD_BLOCK) {
                Random random = new Random();
                int randomInt = random.nextInt(2) + 1;
                if (randomInt == 1) { //良い
                    player.sendTitle(ChatColor.GREEN + "当たり!", "", 20, 40, 20);
                    player.getInventory().addItem(Config.itemMeta("スピード", Material.GOLD_BLOCK, 1));
                    player.sendMessage("右クリックで5秒間のスピードの効果を得られます!");
                    if (player.getItemInHand().getType().equals(Material.GOLD_BLOCK)) {
                        player.setItemInHand(null);
                    }
                } else {
                    player.sendTitle(ChatColor.RED + "はずれ", "", 20, 40, 20);
                    PotionEffect confusion = new PotionEffect(PotionEffectType.CONFUSION, 100, 1);
                    player.addPotionEffect(confusion);
                    player.sendMessage(ChatColor.RED + "5秒間視界が歪むようになってしまった!");
                    if (player.getItemInHand().getType().equals(Material.GOLD_BLOCK)) {
                        player.setItemInHand(null);
                    }
                }
            } else if (block == Material.NETHER_STAR) {
                PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 100, 1);
                player.addPotionEffect(speed);
                if (player.getItemInHand().getType().equals(Material.NETHER_STAR)) {
                    player.setItemInHand(null);
                    player.sendMessage("使用しました!");
                }
            } else if (block == Material.RED_DYE) {
                if (SumoActivities.sumoQueueingList.contains(player.getName())) {
                    SumoActivities.sumoQueueingList.remove(player.getName());
                } else if (TopfightActivities.topfightQueueingList.contains(player.getName())) {
                    TopfightActivities.topfightQueueingList.remove(player.getName());
                }
                if (player.getItemInHand().getType().equals(Material.RED_DYE)) {
                    player.setItemInHand(null);
                    player.sendMessage("退出しました");
                }
            } else if (block == Material.BLUE_DYE) {
                if (player.getItemInHand().getType().equals(Material.BLUE_DYE)) {
                    if (SpleefActivities.spleefPlayingList.contains(player.getName())) {
                        FfaGames.playerQuitByBlueDyeAction(SpleefActivities.spleefPlayingList, player.getName(), plugin);
                        FfaGames.playerListChecker(player, SpleefActivities.spleefPlayingList);
                    }
                    player.setItemInHand(null);
                    player.sendMessage("退出しました");
                }
            }
        }
    }
}
