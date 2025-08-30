package mark.tofu.pvpworld.utils;

import mark.tofu.pvpworld.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpeedRunAction {
    public static void openGameListInventory(Player player) {
        Inventory gameList = Bukkit.createInventory(null, 9, "SpeedRun: モード選択");
        gameList.setItem(0, Config.itemMeta("SpeedRunシングルプレイ", Material.PAPER));
        gameList.setItem(1, Config.itemMeta("SpeedRunマルチプレイ", Material.PAPER));
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
    }

    public static void onHoldAction(Player player) {
        player.teleport(Config.speedRunOnholdRoom);
        Config.SpeedRunOnHoldList.add(player.getName());
        if (Config.SpeedRunOnHoldList.size() == 1) {

        }
    }
}
