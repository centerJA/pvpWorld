package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Objects;

public class  OneVersusOneGames {
    public static void gameCloseAction(PvpWorld plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (String PlayerName: Config.TeleportToLobbyList) {
                    Player player = Bukkit.getPlayer(PlayerName);
                    if (player == null) return;
                    player.getInventory().clear();
                    StartTimerUtils.stopTimer(player);
                    TimeUpTimer.stopTimer(player);
                    player.teleport(Config.lobby);
                }
                for (String PlayerName: Config.TeleportToLobbyList) {
                    Config.TeleportToLobbyList.remove(PlayerName);
                }
            }
        }, 60L);
    }

    public static void drawAction(ArrayList<String> arrayList, PvpWorld plugin) {
        for (String PlayerName: arrayList) {
            Player player = Bukkit.getPlayer(PlayerName);
            if (player == null) return;
            player.sendTitle(ChatColor.YELLOW + "引き分け", ChatColor.YELLOW + "勝利までもう少し!!", 0, 60, 0);
            Config.TeleportToLobbyList.addAll(arrayList);
            gameCloseAction(plugin);
        }
    }

    public static void overlappingGames(Player player) {
        player.sendMessage("あなたは既に1v1ゲームスに参加しています!");
        player.sendMessage("退出してからゲームに参加してください");
    }

    public static boolean player1v1GamesContainsCheck(Player player) {
        if (SumoActivities.sumoQueueingList.contains(player.getName())) {
            return true;
        }
        else return false;
    }

    public static void noWalkRemoveAction(ArrayList<String> arrayList) {
        for (String PlayerName: arrayList) {
            Config.NoWalkList.remove(PlayerName);
        }
    }

    public static void timeUpAction(Player player, PvpWorld plugin) {
        String playerName = player.getName();
        if (SumoActivities.sumoQueueingList.contains(playerName)) {
            drawAction(SumoActivities.sumoQueueingList, plugin);
        }
        else if (TopfightActivities.topfightQueueingList.contains(playerName)) {
            drawAction(TopfightActivities.topfightQueueingList, plugin);
        }
    }

    public static void queueingActivities(Player player, InventoryClickEvent e, PvpWorld plugin, ArrayList<String> arrayList) {
        player.sendMessage(String.valueOf(arrayList));
        if (arrayList.isEmpty()) {
            arrayList.add(player.getName());
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage("他の人を待っています...");
            player.sendMessage("参加をやめるには、インベントリの中の赤色の染料を右クリックしてください");
            player.getInventory().setItem(8, Config.itemMeta("ゲームをやめる", Material.RED_DYE, 1));
            oneVersusOneSggestPlayerJoin(arrayList);
            ScoreBoardUtils.set1v1ScoreBoard(player, false);
        } else if (arrayList.size() == 1) {
            for (String PlayerName: arrayList) {
                if (PlayerName.equals(player.getName())) {
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("既に参加しています!");
                    player.sendMessage("退出するにはインベントリ内の赤い染料を右クリックしてください");
                } else {
                    arrayList.add(player.getName());
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("相手が見つかりました!");
                    dividePlayer(arrayList, player, plugin);
                    ScoreBoardUtils.set1v1ScoreBoard(player, true);
                }
            }
        } else {
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage("既に誰かがプレイ中です");
        }
    }

    public static void dividePlayer(ArrayList<String> arrayList, Player player, PvpWorld plugin) {
        if (arrayList.equals(SumoActivities.sumoQueueingList)) {
            SumoActivities.sumoStartAction(player, plugin);
        }
        else if (arrayList.equals(TopfightActivities.topfightQueueingList)) {
            TopfightActivities.topfightStartAction(player, plugin);
        }
        else {
            player.sendMessage("エラー");
        }
    }


    public static void oneVersusOneSggestPlayerJoin(ArrayList<String> arrayList) {
        String base = ChatColor.YELLOW + "[1v1 Games]";
        String base2 = ChatColor.WHITE + "で1人が対戦者を探しています!";
        if (arrayList.equals(SumoActivities.sumoQueueingList)) {
            for (String PlayerName: Config.WorldAllPlayerList) {
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(base + ChatColor.WHITE + "sumo" + base2);
            }
        } else if (arrayList.equals(TopfightActivities.topfightQueueingList)) {
            for (String PlayerName: Config.WorldAllPlayerList) {
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(base + ChatColor.WHITE + "topfight" + base2);
            }
        }
    }

    public static void playerQuitAction(ArrayList<String> arrayList, String playerName, PvpWorld plugin) {
        arrayList.remove(playerName);
        ScoreBoardUtils.set1v1ScoreBoard(Objects.requireNonNull(Bukkit.getPlayer(playerName)), true);
        for (String PlayerName: arrayList) {
            if (PlayerName == null) return;
            arrayList.remove(Objects.requireNonNull(Bukkit.getPlayer(PlayerName)));
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendTitle(ChatColor.GREEN + "勝利!", ChatColor.YELLOW + "相手が戦いを放棄しました", 0, 60, 0);
            gameCloseAction(plugin);
        }
    }
}
