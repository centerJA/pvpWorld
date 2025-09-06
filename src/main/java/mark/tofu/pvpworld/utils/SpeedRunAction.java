package mark.tofu.pvpworld.utils;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class SpeedRunAction {
    public static void openGameListInventory(Player player) {
        player.sendMessage("38481368");
        Inventory gameList = Bukkit.createInventory(null, 9, "SpeedRun: モード選択");
        gameList.setItem(0, Config.itemMeta("SpeedRunシングルプレイ", Material.PAPER));
        gameList.setItem(1, Config.itemMeta("SpeedRunマルチプレイ", Material.PAPER));
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
    }

    public static void singleOnHoldAction(Player player, PvpWorld plugin) {
        if (!Config.SpeedRunSingleOnHoldList.isEmpty()) { //誰かがプレイしている最中
            player.sendMessage("既に誰かがプレイしています!");
            player.sendMessage("少々お待ちください");
            return;
        } else { //タイマーを発動させる
            player.teleport(Config.speedRunSingleOnholdRoom);
            Config.SpeedRunSingleOnHoldList.add(player.getName());
            SpeedRunTimer.startTimer(player, plugin);
        }
    }

    public static void multiOnHoldAction(Player player, PvpWorld plugin) {
        player.sendMessage("まだアクセスできません!");
    }
}
