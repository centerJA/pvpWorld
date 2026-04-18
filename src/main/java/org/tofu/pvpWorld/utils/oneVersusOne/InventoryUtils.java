package org.tofu.pvpWorld.utils.oneVersusOne;

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
        Inventory gameList = Bukkit.createInventory(null, 54, textComponent.parse("<b><yellow>1v1ゲームス"));
        gameList.setItem(10, sumoSetProperties());
        gameList.setItem(11, topfightSetProperties());
        Objects.requireNonNull(player.getPlayer()).openInventory(gameList);
    }

    public static ItemStack sumoSetProperties() {
        int size = SumoActivities.sumoQueueingList.size();
        ItemStack item = new ItemStack(Material.LEAD, size + 1);
        ItemMeta meta = item.getItemMeta();
        List<Component> loreList = new ArrayList<>();
        loreList.add(textComponent.parse("<green>相手を殴って敵を落とします!"));
        loreList.add(textComponent.parse("<white>ルール:"));
        loreList.add(textComponent.parse("<white>・ブロック使用不可!"));
        loreList.add(textComponent.parse("<white>・落ちたら負け!"));
        if (size == 2) {
            loreList.add(textComponent.parse("<red>誰かがプレイ中!"));
        } else {
            loreList.add(textComponent.parse("<white>待機中: <gold>" + size));
        }
        meta.lore(loreList);
        meta.displayName(textComponent.parse("<yellow>sumo"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack topfightSetProperties() {
        int size = TopfightActivities.topfightQueueingList.size();
        ItemStack item = new ItemStack(Material.IRON_BLOCK, size + 1);
        ItemMeta meta = item.getItemMeta();
        List<Component> loreList = new ArrayList<>();
        loreList.add(textComponent.parse("<green>相手を殺します!"));
        loreList.add(textComponent.parse("<white>ルール:"));
        loreList.add(textComponent.parse("<white>・ブロックは16個!"));
        loreList.add(textComponent.parse("<white>・最高高度はy16!"));
        loreList.add(textComponent.parse("<white>・落ちたら負け!"));
        if (size == 2) {
            loreList.add(textComponent.parse("<red>誰かがプレイ中!"));
        } else {
            loreList.add(textComponent.parse("<white>待機中: <gold>" + size));
        }
        meta.lore(loreList);
        meta.displayName(textComponent.parse("<red>topfight"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }



    public static void replaceInventoryCheck(Player player) {
        Component target = textComponent.parse("<b><yellow>1v1ゲームス");
        for (String PlayerName : Config.WorldAllPlayerList) {
            Player player2 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
            InventoryView inventoryView = player2.getOpenInventory();
            if (inventoryView == null || !inventoryView.title().equals(target)) return;
            player.closeInventory();
            openGameListInventory(player);
        }
    }
}
