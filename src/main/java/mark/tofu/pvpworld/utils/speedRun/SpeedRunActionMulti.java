package mark.tofu.pvpworld.utils.speedRun;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.yamlProperties.coinUtils;
import mark.tofu.pvpworld.utils.yamlProperties.expUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SpeedRunActionMulti {
    public static ArrayList<String> multiPlayingList = new ArrayList<>(),
                                    backToLobbyList = new ArrayList<>();
    public static boolean gamePlaying = false;

    private static World world = Bukkit.getWorld("pvpWorld");
    public static Location startLocation = new Location(world, 154.500, 5.000, 113.500, -90, 0),
                           wallLoc1 = new Location(world, 167, 5, 123),
                           wallLoc2 = new Location(world, 167, 14, 103),
                           buttonLoc = new Location(world, 182, 5, 113);

//    //multi------------------
//    public static void mutiMapSelecting(Player player) {
//        Inventory gameList = Bukkit.createInventory(null, 9, "SpeedRun: マップ選択");
//        gameList.setItem(0, Config.itemMeta("シンプル", Material.PAPER, 1));
//        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
//    }


    public static void multiOnHoldAction(Player player, PvpWorld plugin) {
        if (multiPlayingList.contains(player.getName())) {
            player.sendMessage("既に参加しています!");
            return;
        }
        multiPlayingList.add(player.getName());
        if (gamePlaying) {
            player.sendMessage("既にゲームが進行中です!");
            player.sendMessage("もう少しお待ちください");
        } else {
            if (multiPlayingList.size() == 1) {
                player.teleport(startLocation);
                player.sendMessage("誰もプレイしていません");
                player.sendMessage("プレイするには最低2人が必要です!");
                //red mushroom
            } if (multiPlayingList.size() == 2) {
                player.teleport(startLocation);
                //もともと居た人にお知らせする
                player.sendMessage("現在2人が参加しています!");
                player.sendMessage("追加の参加者を募集しています...");
                SpeedRunTimerMulti.startTimer(player, plugin);
                //encourage
            } else {
                player.teleport(startLocation);
                player.sendMessage("参加しました");
            }
        }
    }


    public static void playerLeaveAction(Player player) throws IOException {
        multiPlayingList.remove(player.getName());
        player.sendMessage("SpeedRunMultiを退出しました");
        if (gamePlaying) {
            if (multiPlayingList.size() == 1) {
                for (String PlayerName: multiPlayingList) {
                    Player pl = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                    PvpWorld plugin = new PvpWorld();
                    winAction(pl, plugin);
                }

            } else {
                for (String PlayerName: multiPlayingList) {
                    Player pl = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                    pl.sendMessage(player.getName() + "さんが退出しました");
                }
            }
        } else {
            if (multiPlayingList.size() == 1) {
                for (String PlayerName: multiPlayingList) {
                    Player pl = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                    pl.sendMessage(player.getName() + "さんが退出しました");
                    pl.sendMessage("最低人数に達していないため、タイマーをストップします");
                }
            } else {
                for (String PlayerName: multiPlayingList) {
                    Player pl = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                    pl.sendMessage(player.getName() + "さんが退出しました");
                }
            }
        }
    }


    public static void winAction(Player player, PvpWorld plugin) throws IOException {
        player.sendTitle(ChatColor.GREEN + " 勝利", ChatColor.YELLOW + "おめでとう!!", 0, 60, 0);
        backToLobbyList.addAll(multiPlayingList);
        multiPlayingList.remove(player.getName());
        fillBlock(wallLoc1, wallLoc2, Material.GLASS);
        loseAction();
        expUtils.playerSetExp(player, 20);
        coinUtils.playerSetCoin(player, 25);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if (backToLobbyList == null) return;
                for (String PlayerName: backToLobbyList) {
                    Player pl = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                    pl.teleport(Config.lobby);
                    backToLobbyList.remove(pl.getName());
                }
            }
        }, 60L);
    }

    public static void loseAction() throws IOException {
        for (String PlayerName: multiPlayingList) {
            Player pl = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            pl.sendTitle(ChatColor.RED + " 敗北", ChatColor.YELLOW + "次は頑張ろう!!", 0, 60, 0);
            expUtils.playerSetExp(pl, 15);
            coinUtils.playerSetCoin(pl, 13);
        }
    }



    public static void fillBlock(Location loc1, Location loc2, Material material) {
        World world = loc1.getWorld();
        if (world == null || !world.equals(loc2.getWorld())) return;

        // どちらの座標が大きくても正しくループできるよう、最小値と最大値を特定
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        // 3重ループで範囲内の全座標を走査
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // ブロックを設置
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }


    public static void startAction() {
        //ScheduledTimer
        fillBlock(wallLoc1, wallLoc2, Material.AIR);
        for (String PlayerName: multiPlayingList) {
            Player pl = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            pl.sendTitle(ChatColor.AQUA + "スタート!", "", 20, 20, 20);
        }
    }

    public static void checkButton(Block block, Player player, PvpWorld plugin) throws IOException {
        if (block.getLocation().equals(buttonLoc)) {
            winAction(player, plugin);
        }
    }
}
