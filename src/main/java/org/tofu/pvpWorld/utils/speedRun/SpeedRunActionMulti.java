package org.tofu.pvpWorld.utils.speedRun;

import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.itemStackMaker;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.titleMaker;
import org.tofu.pvpWorld.utils.yamlProperties.coinUtils;
import org.tofu.pvpWorld.utils.yamlProperties.expUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SpeedRunActionMulti {
    public static ArrayList<String> multiPlayingList = new ArrayList<>(),
                                    backToLobbyList = new ArrayList<>();
    public static boolean gamePlaying = false,
                          canPressButton = false;

    private static Player centrifugalPlayer;

    private static World world = Bukkit.getWorld("pvpWorld");
    public static Location startLocation = new Location(world, 154.500, 5.000, 113.500, -90, 0),
                           wallLoc1 = new Location(world, 167, 5, 123),
                           wallLoc2 = new Location(world, 167, 14, 103),
                           buttonLoc = new Location(world, 423, 9, 113),
                           targetBlock1 = new Location(world, 412, 10, 107),
                           targetBlock2 = new Location(world, 412, 10, 119),
                           appearBlock = new Location(world, 396, 7, 113);
//    //multi------------------
//    public static void mutiMapSelecting(Player player) {
//        Inventory gameList = Bukkit.createInventory(null, 9, "SpeedRun: マップ選択");
//        gameList.setItem(0, Config.itemMeta("シンプル", Material.PAPER, 1));
//        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
//    }


    public static void multiOnHoldAction(Player player, PvpWorld plugin) throws IOException {
        if (multiPlayingList.contains(player.getName())) {
            player.sendMessage("既に参加しています!");
            return;
        }
        if (Config.overLappingTrigger(player)) {
            Config.overLappingMessage(player);
            return;
        }
        Config.beforeGame(player);

        if (gamePlaying) {
            player.sendMessage("既にゲームが進行中です!");
            player.sendMessage("もう少しお待ちください");
        } else {
            if (multiPlayingList.isEmpty()) {
                player.teleport(startLocation);
                multiPlayingList.add(player.getName());
                player.sendMessage("誰もプレイしていません");
                player.sendMessage("プレイするには最低2人が必要です!");
                Config.clearInventory(player);
                promoteGames();
                player.getInventory().setItem(0, itemStackMaker.createItem(textComponent.parse("ロビーに戻る"), Material.RED_MUSHROOM, 1));
            } else if (multiPlayingList.size() == 1) {
                player.teleport(startLocation);
                noticeToPlayer();
                multiPlayingList.add(player.getName());
                centrifugalPlayer = player;
                player.sendMessage("現在2人が参加しています!");
                player.sendMessage("追加の参加者を募集しています...");
                Config.clearInventory(player);
                player.getInventory().setItem(0, itemStackMaker.createItem(textComponent.parse("ロビーに戻る"), Material.RED_MUSHROOM, 1));
                SpeedRunTimerMulti.startTimer(player, plugin);
            } else {
                player.teleport(startLocation);
                multiPlayingList.add(player.getName());
                player.sendMessage("参加しました");
                Config.clearInventory(player);
                plusExtraTime();
                player.getInventory().setItem(0, itemStackMaker.createItem(textComponent.parse("ロビーに戻る"), Material.RED_MUSHROOM, 1));
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
                SpeedRunTimerMulti.stopTimer(SpeedRunActionMulti.multiPlayingList);
                for (String PlayerName: multiPlayingList) {
                    Player pl = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                    pl.sendMessage(player.getName() + "さんが退出しました");
                    pl.sendMessage("最低人数に達していないため、タイマーをストップします");
                    pl.setLevel(0);
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
        canPressButton = false;
        player.showTitle(titleMaker.title(textComponent.parse("<green>勝利"), textComponent.parse("<yellow>おめでとう!!"), 0, 3000, 0));
        SpeedRunScheduledTimer.stopTimer(centrifugalPlayer);
        for (String PlayerName: multiPlayingList) {
            Player player1 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            player1.setLevel(0);
        }
        backToLobbyList.addAll(multiPlayingList);
        multiPlayingList.remove(player.getName());
        fillBlock(wallLoc1, wallLoc2, Material.GLASS);
        loseAction();
        expUtils.playerSetExp(player, 30);
        coinUtils.playerSetCoin(player, 38);
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
            pl.showTitle(titleMaker.title(textComponent.parse("<red>敗北"), textComponent.parse("<yellow>次も頑張ろう!!"), 0, 3000, 0));
            expUtils.playerSetExp(pl, 10);
            coinUtils.playerSetCoin(pl, 13);
            gamePlaying = false;
        }
    }



    public static void fillBlock(Location loc1, Location loc2, Material material) {
        World world = loc1.getWorld();
        if (world == null || !world.equals(loc2.getWorld())) return;

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }


    public static void startAction(PvpWorld plugin) {
        fillBlock(wallLoc1, wallLoc2, Material.AIR);
        gamePlaying = true;
        canPressButton = true;
        System.out.println(multiPlayingList);
        for (String PlayerName: multiPlayingList) {
            Player pl = Bukkit.getPlayer(PlayerName);
            if (pl != null) {
                pl.showTitle(titleMaker.title(textComponent.parse("<aqua>スタート!"), textComponent.parse(""), 1000, 1000, 1000));
                pl.setLevel(0);
            }
        }
        SpeedRunScheduledTimer.startTimer(centrifugalPlayer, plugin, true);
    }



    public static void checkButton(Block block, Player player, PvpWorld plugin) throws IOException {
        if (block.getLocation().equals(buttonLoc)) {
            if (!canPressButton) return;
            winAction(player, plugin);
        }
    }


    public static void promoteGames() {
        for (String PlayerName: Config.WorldAllPlayerList) {
            Player player = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            player.sendMessage(textComponent.parse("<gold>[SpeedRun Multi]<white>1人が対戦相手を募集中です!"));
        }
    }

    public static void noticeToPlayer() {
        for (String PlayerName: multiPlayingList) {
            Player player = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            player.sendMessage(textComponent.parse("<white>対戦相手が見つかったため、カウントダウンを開始します!"));
        }
    }

    public static void plusExtraTime() {
        if (SpeedRunTimerMulti.playerTimes.get(centrifugalPlayer) > 10) return;
        SpeedRunTimerMulti.addTime(centrifugalPlayer, 10);
        for (String PlayerName: multiPlayingList) {
            Player player = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            int size = multiPlayingList.size();
            player.sendMessage(textComponent.parse("<white>" + size + "人目が参加したため、10秒追加しました!"));
        }
    }

    public static void checkSnowBallInfo(Location location, Player player, PvpWorld plugin) {
        boolean isTarget1 = (location.getBlockX() == targetBlock1.getBlockX() &&
                location.getBlockY() == targetBlock1.getBlockY() &&
                location.getBlockZ() == targetBlock1.getBlockZ());

        boolean isTarget2 = (location.getBlockX() == targetBlock2.getBlockX() &&
                location.getBlockY() == targetBlock2.getBlockY() &&
                location.getBlockZ() == targetBlock2.getBlockZ());

        if (isTarget1 || isTarget2) {
            World world1 = location.getWorld();
            Location currentLocation = new Location(world1, appearBlock.getX(), appearBlock.getY(), appearBlock.getZ());
            currentLocation.getBlock().setType(Material.GOLD_BLOCK);
            player.sendMessage(textComponent.parse("<gold>足場が出現しました!"));
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    currentLocation.getBlock().setType(Material.AIR);
                }
            }, 20L);
        }

    }
}
