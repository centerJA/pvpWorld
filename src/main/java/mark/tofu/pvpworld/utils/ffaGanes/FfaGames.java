package mark.tofu.pvpworld.utils.ffaGanes;

import jdk.nashorn.internal.objects.NativeError;
import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.oneVersusOne.StartTimerUtils;
import mark.tofu.pvpworld.utils.oneVersusOne.TimeUpTimer;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.Objects;

public class FfaGames {
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


    public static void noWalkRemoveAction(ArrayList<String> arrayList) {
        for (String PlayerName: arrayList) {
            Config.NoWalkList.remove(PlayerName);
        }
    }

    public static void ffaQueueingActivities(Player player, ArrayList<String> arrayList, PvpWorld plugin, InventoryClickEvent e) {
        player.sendMessage(String.valueOf(arrayList));
        if (arrayList.isEmpty()) {
            arrayList.add(player.getName());
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage("他の人を待っています...");
            player.sendMessage("参加をやめるには、インベントリの中の青色の染料を右クリックしてください");
            player.getInventory().setItem(8, Config.itemMeta("ゲームをやめる", Material.BLUE_DYE, 1));
            ffaSuggestPlayerJoin(arrayList);
        } else if (arrayList.size() == 1) {
            for (String PlayerName: arrayList) {
                if (PlayerName.equals(player.getName())) {
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("既に参加しています!");
                    player.sendMessage("退出するにはインベントリ内の青い染料を右クリックしてください");
                } else {
                    arrayList.add(player.getName());
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("相手が見つかりました!");
                    player.sendMessage("ほかの人を探しています...");
                    player.getInventory().setItem(8, Config.itemMeta("ゲームをやめる", Material.BLUE_DYE, 1));
                    mark.tofu.pvpworld.utils.ffaGanes.StartTimerUtils.startTimer(player, plugin, arrayList);
                }
            }
        } else {
            arrayList.add(player.getName());
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage("参加しました");
        }
    }

    public static void ffaSuggestPlayerJoin(ArrayList<String> arrayList) {
        String base = ChatColor.RED + "[FFA Games]";
        String base2 = ChatColor.WHITE + "で";
        String base3 = ChatColor.WHITE + "人が待機中です!";
        if ( arrayList.equals(SpleefActivities.spleefPlayingList)) {
            for (String PlayerName: Config.WorldAllPlayerList) {
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(base + ChatColor.WHITE + "Spleef" + base2 + arrayList.size() + base3);
            }
        }
    }

    public static void playerQuitByBlueDyeAction(ArrayList<String> arrayList, String playerName, PvpWorld plugin) {
        arrayList.remove(playerName);
        if (arrayList.size() == 1) {
            for (String PlayerName: arrayList) {
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage("最低人数に達していないため、タイマーを止めます");
                if (arrayList.equals(SpleefActivities.spleefPlayingList)) {
                    mark.tofu.pvpworld.utils.ffaGanes.StartTimerUtils.startTimer(Objects.requireNonNull(Bukkit.getPlayer(playerName)), plugin, SpleefActivities.spleefPlayingList);

                }
            }
        } else return;
    }

    public static void playerQuitByLeaveWorldAction(ArrayList<String> arrayList, String playerName, PvpWorld plugin) {
        arrayList.remove(playerName);
        if (arrayList.size() == 1) {
            for (String PlayerName: arrayList) {
                Player player = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                player.sendTitle(ChatColor.GREEN + "勝利", ChatColor.YELLOW + "対戦相手が放棄しました", 0, 60, 0);
                arrayList.remove(PlayerName);
                gameCloseAction(plugin);
            }
        }
    }

    public static void playerListChecker(Player player, ArrayList<String> arrayList) {
        if (arrayList.size() == 1) {
            mark.tofu.pvpworld.utils.ffaGanes.StartTimerUtils.stopTimer(player);
            for (String PlayerName: arrayList) {
                Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage("1人になってしまったため、タイマーがストップしました");
            }
        }
    }
}
