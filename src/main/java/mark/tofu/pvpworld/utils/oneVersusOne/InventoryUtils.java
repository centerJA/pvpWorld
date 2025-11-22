package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class InventoryUtils {
    public static void openGameListInventory(Player player) {
        Inventory gameList = Bukkit.createInventory(null, 54, ChatColor.BOLD + "" + ChatColor.YELLOW + "1v1ゲーム");
        gameList.setItem(0, Config.itemMeta("sumo", Material.LEAD, 1));
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
    }
}
