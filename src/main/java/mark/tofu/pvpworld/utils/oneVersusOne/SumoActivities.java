package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class SumoActivities {
    public static World world = Bukkit.getWorld("pvpWorld");

    public static ArrayList<String> sumoQueueingList = new ArrayList<>();

    public static Location player1Location = new Location(world, 50.500, 4.500, -108.500, 180, 0),
                           player2Location = new Location(world, 50.500, 4.500, -120.500,0, 0);

    public static void sumoStartAction(Player player, PvpWorld plugin) {
        for (String PlayerName: sumoQueueingList) {
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).getInventory().setItem(8, null);
        }
        Objects.requireNonNull(Bukkit.getPlayer(sumoQueueingList.get(0))).teleport(player1Location);
        Objects.requireNonNull(Bukkit.getPlayer(sumoQueueingList.get(1))).teleport(player2Location);
        TimeUpTimer.startTimer(player, plugin, 300);
        Config.NoWalkList.addAll(sumoQueueingList);
        StartTimerUtils.startTimer(player, plugin, sumoQueueingList);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() { //スタート
            @Override
            public void run() {
                OneVersusOneGames.noWalkRemoveAction(sumoQueueingList);
                for (String PlayerName: sumoQueueingList) {
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(String.valueOf(Config.DoNotReceiveDamageList));
                    Config.DoNotReceiveDamageList.remove(PlayerName);
                    Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(String.valueOf(Config.DoNotReceiveDamageList));
                }
            }
        }, 100L);
    }


    public static void sumoCloseAction(Player player, PvpWorld plugin) {
        Config.DoNotReceiveDamageList.addAll(SumoActivities.sumoQueueingList);
        Config.TeleportToLobbyList.addAll(SumoActivities.sumoQueueingList);
        SumoActivities.sumoQueueingList.remove(player.getName());
        player.sendTitle(ChatColor.RED + "敗北", ChatColor.YELLOW + "もう一度挑戦しよう", 0, 60, 0);
        Player winner = Bukkit.getPlayer(SumoActivities.sumoQueueingList.get(0));
        if (winner == null) return;
        SumoActivities.sumoQueueingList.remove(winner.getName());
        winner.sendTitle(ChatColor.GREEN + "勝利", ChatColor.YELLOW + "すごい!!", 0, 60, 0);
        OneVersusOneGames.gameCloseAction(plugin);
    }

    public static void sumoQueueingActivities(Player player, InventoryClickEvent e, PvpWorld plugin) {
        player.sendMessage(String.valueOf(SumoActivities.sumoQueueingList));
        if (SumoActivities.sumoQueueingList.isEmpty()) {
            SumoActivities.sumoQueueingList.add(player.getName());
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage("他の人を待っています...");
            player.sendMessage("参加をやめるには、インベントリの中の赤色の染料を右クリックしてください");
            player.getInventory().setItem(8, Config.itemMeta("ゲームをやめる", Material.RED_DYE, 1));
        } else if (SumoActivities.sumoQueueingList.size() == 1) {
            for (String PlayerName : SumoActivities.sumoQueueingList) {
                if (PlayerName.equals(player.getName())) {
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("既に参加しています!");
                    player.sendMessage("退出するにはインベントリ内の赤い染料を右クリックしてください");
                } else {
                    SumoActivities.sumoQueueingList.add(player.getName());
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("相手が見つかりました!");
                    SumoActivities.sumoStartAction(player, plugin);
                }
            }
        } else {
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage("既に誰かがプレイ中です");
        }
    }
}
