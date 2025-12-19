package mark.tofu.pvpworld.utils.ffaGanes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryUtils {
    public static void openGameListInventory(Player player) {
        Inventory gameList = Bukkit.createInventory(null, 54, ChatColor.BOLD + "" + ChatColor.GREEN + "FFA Games");
        gameList.setItem(10, spleefSetProperties());
        player.openInventory(gameList);
    }

    public static ItemStack spleefSetProperties() {
        int size = 1;
        ItemStack item = new ItemStack(Material.DIAMOND_SHOVEL, size);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.GREEN + "相手を下に落とします!");
        loreList.add(ChatColor.WHITE + "ルール:");
        loreList.add(ChatColor.WHITE + "雪は掘れる!");
        loreList.add(ChatColor.WHITE + "掘ると雪玉が手に入る!");
        loreList.add(ChatColor.WHITE + "落ちたら負け!");
        loreList.add("");
        Objects.requireNonNull(meta).setLore(loreList);
        meta.setDisplayName(ChatColor.GREEN + "spleef");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
}
