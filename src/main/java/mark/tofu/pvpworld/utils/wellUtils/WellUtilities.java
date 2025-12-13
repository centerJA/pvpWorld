package mark.tofu.pvpworld.utils.wellUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WellUtilities {
    public static void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "井戸");
        inventory.setItem(31, pickItemProperties());
        Objects.requireNonNull(player.getPlayer()).openInventory(inventory);
    }

    public static ItemStack pickItemProperties() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.WHITE + "10Goldを使用して、レアアイテムや");
        loreList.add(ChatColor.WHITE + "Goldなどを入手できます!!");
        loreList.add(ChatColor.GREEN + "何がでるかはわかりません");
        loreList.add(ChatColor.RED + "消費: " + ChatColor.YELLOW + "10Gold");
        loreList.add(ChatColor.GOLD + ">>右クリックして井戸から入手<<");
        meta.setLore(loreList);
        meta.setDisplayName(ChatColor.DARK_PURPLE + "井戸");
        item.setItemMeta(meta);
        return item;
    }
}
