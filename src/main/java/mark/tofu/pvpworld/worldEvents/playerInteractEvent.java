package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.*;
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
            } else if (e.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                if (Math.floor(e.getClickedBlock().getLocation().getX()) == Math.floor(Config.freePvpJoinPoint.getX()) && Math.floor(e.getClickedBlock().getLocation().getY()) == Math.floor(Config.freePvpJoinPoint.getY()) && (double) e.getClickedBlock().getZ() == Math.floor(Config.freePvpJoinPoint.getZ())) {
                    if (!Config.FreePvpPlayerList.contains(player.getName())) {
                        Config.FreePvpPlayerList.add(player.getName());
                        Config.clearInventory(player);
                        player.getInventory().setItem(0, Config.itemMeta("聖なる剣", Material.IRON_SWORD, 1));
                        player.getInventory().setItem(1, Config.itemMeta("釣り竿", Material.FISHING_ROD, 1));
                        player.getInventory().setItem(2, Config.itemMeta("弓", Material.BOW, 1));
                        player.getInventory().setItem(8, Config.itemMeta("矢", Material.ARROW, 8));
                        player.getInventory().setItem(39, Config.itemMeta("ヘルメット", Material.IRON_HELMET, 1));
                        player.getInventory().setItem(38, Config.itemMeta("チェストプレート", Material.IRON_CHESTPLATE, 1));
                        player.getInventory().setItem(37, Config.itemMeta("レギンス", Material.IRON_LEGGINGS, 1));
                        player.getInventory().setItem(36, Config.itemMeta("ブーツ", Material.IRON_BOOTS, 1));
                        player.getInventory().setItem(12, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
                        Config.DoNotReceiveDamageList.remove(player.getName());
                        player.sendMessage("ロビーに戻るにはインベントリ内のアイテムを使用してください");
                        player.teleport(Config.freePvpSpawnPoint);
                        for (String PlayerName: Config.WorldAllPlayerList) {
                            Player player2 = Bukkit.getPlayer(PlayerName);
                            Objects.requireNonNull(player2).sendMessage(ChatColor.GOLD + player.getName() + "さんがFree PVPスペースに参加しました");
                        }
                        if (Config.FreePvpPlayerList.isEmpty()) {
                            player.sendMessage("現在誰もいません");
                        }
                    } else {
                        player.sendMessage("エラーが発生しました");
                    }

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

                if (Objects.equals(lines[0], "SpeedRunTest")) { //SpeedRunのメニューを表示させる
                    SpeedRunAction.openGameListInventory(player);
                } else if (Objects.equals(lines[0], "ルール説明")) {
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
                        player.sendMessage(ChatColor.AQUA + "-----Free PVP-----");
                        player.sendMessage("このゲームは、自由参加型の" + ChatColor.GOLD + "FFA" + ChatColor.WHITE + "PVPゲームです!");
                        player.sendMessage("FFAとは、味方がいない、全員敵のゲームのことです。");
                        player.sendMessage("鉄剣、弓、矢8本、釣り竿、鉄フル装備が支給されます。");
                        player.sendMessage("敵を倒すと金リンゴがもらえ、継続的に試合を続けられます!");
                        player.sendMessage("退出する際は、インベントリ内にある赤いキノコをホットバーに移動させて、右クリックしてください。");
                        player.sendMessage(ChatColor.AQUA + "---------------------");
                    }
                }
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
            }
        }
    }
}
