package org.tofu.pvpWorld.utils.ffaGames;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.tofu.pvpWorld.utils.textComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryUtils {
    public static void openGameListInventory(Player player) {
        Inventory gameList = Bukkit.createInventory(null, 54, textComponent.parse("<b><green>FFAゲームス"));
        gameList.setItem(10, spleefSetProperties());
        player.openInventory(gameList);
    }

    public static ItemStack spleefSetProperties() {
        int size = 1;
        ItemStack item = new ItemStack(Material.DIAMOND_SHOVEL, size);
        ItemMeta meta = item.getItemMeta();
        List<Component> loreList = new ArrayList<>();
        loreList.add(textComponent.parse("<green>相手を下に落とします!"));
        loreList.add(textComponent.parse("<white>ルール:"));
        loreList.add(textComponent.parse("<white>雪は掘れる!"));
        loreList.add(textComponent.parse("<white>掘ると雪玉が手に入る!"));
        loreList.add(textComponent.parse("<white>落ちたら負け!"));
        meta.lore(loreList);
        meta.displayName(textComponent.parse("<green>spleef"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public static void replaceInventoryCheck(Player player) {
        Component target = textComponent.parse("<b><green>spleef");
        for (String PlayerName : Config.WorldAllPlayerList) {
            Player player2 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            InventoryView inventoryView = player2.getOpenInventory();
            if (inventoryView == null || !inventoryView.title().equals(target)) return;
            player.closeInventory();
            openGameListInventory(player);
        }
    }
}
