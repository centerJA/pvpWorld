package mark.tofu.pvpworld.utils.speedRun;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Objects;

public class SpeedRunActionMulti {
    public static ArrayList<String> multiPlayingList = new ArrayList<>();
    public static boolean gamePlaying = false;

    private static World world = Bukkit.getWorld("world");
    public static Location startLocation = new Location(world, 154.500, 5.000, 113.500, -90, 0);
//    //multi------------------
//    public static void mutiMapSelecting(Player player) {
//        Inventory gameList = Bukkit.createInventory(null, 9, "SpeedRun: マップ選択");
//        gameList.setItem(0, Config.itemMeta("シンプル", Material.PAPER, 1));
//        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
//    }


    public static void multiOnHoldAction(Player player, PvpWorld plugin) {
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


    public static void playerLeaveAction(Player player) {
        multiPlayingList.remove(player.getName());
        player.sendMessage("SpeedRunMultiを退出しました");
        if (gamePlaying) {
            if (multiPlayingList.size() == 1) {
                //win action
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
}
