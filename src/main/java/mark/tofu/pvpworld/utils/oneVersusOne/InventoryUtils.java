package mark.tofu.pvpworld.utils.oneVersusOne;

import mark.tofu.pvpworld.Config;
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

public class InventoryUtils {
    public static void openGameListInventory(Player player) {
        Inventory gameList = Bukkit.createInventory(null, 54, ChatColor.BOLD + "" + ChatColor.YELLOW + "1v1ゲームス");
        gameList.setItem(10, sumoSetProperties());
        gameList.setItem(11, topfightSetProperties());
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
    }

    public static ItemStack sumoSetProperties() {
        int size = SumoActivities.sumoQueueingList.size();
        ItemStack item = new ItemStack(Material.LEAD, size + 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.GREEN + "相手を殴って敵を落とします!");
        loreList.add(ChatColor.WHITE + "ルール:");
        loreList.add(ChatColor.WHITE + "・ブロック使用不可!");
        loreList.add(ChatColor.WHITE + "・落ちたら負け!");
        if (size == 2) {
            loreList.add(ChatColor.RED + "誰かがプレイ中!");
        } else {
            loreList.add(ChatColor.WHITE + "待機中: " + ChatColor.GOLD + size);
        }
        meta.setLore(loreList);
        meta.setDisplayName(ChatColor.YELLOW + "sumo");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack topfightSetProperties() {
        int size = TopfightActivities.topfightQueueingList.size();
        ItemStack item = new ItemStack(Material.IRON_BLOCK, size + 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.GREEN + "相手を殺します!");
        loreList.add(ChatColor.WHITE + "ルール:");
        loreList.add(ChatColor.WHITE + "・ブロックは16個!");
        loreList.add(ChatColor.WHITE + "・最高高度はy16!");
        loreList.add(ChatColor.WHITE + "・落ちたら負け!");
        if (size == 2) {
            loreList.add(ChatColor.RED + "誰かがプレイ中!");
        } else {
            loreList.add(ChatColor.WHITE + "待機中: " + ChatColor.GOLD + size);
        }
        meta.setLore(loreList);
        meta.setDisplayName(ChatColor.RED + "topfight");
        item.setItemMeta(meta);
        return item;
    }
}
