package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.athletic.AthleticTimer;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunScheduledTimer;
import mark.tofu.pvpworld.utils.speedRun.SpeedRunTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

public class OneVersusOneGames {
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
                    player.getInventory().setItem(0, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
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
            encourageJoinGame(player);
            player.getInventory().setItem(8, Config.itemMeta("ゲームをやめる", Material.RED_DYE, 1));
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

    public static void encourageJoinGame(Player player) {
        String base = "1人が対戦相手を待機中です!";
        if (SumoActivities.sumoQueueingList.contains(player.getName())) {
            for (String PlayerName: Config.WorldAllPlayerList) {
                Player player2 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                player2.sendMessage(ChatColor.YELLOW + "[Sumo] " + ChatColor.WHITE + base);
            }
        }
        else if (TopfightActivities.topfightQueueingList.contains(player.getName())) {
            for (String PlayerName: Config.WorldAllPlayerList) {
                Player player2 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                player2.sendMessage(ChatColor.RED + "[TopFight] " + ChatColor.WHITE + base);
            }
        }
    }
}
