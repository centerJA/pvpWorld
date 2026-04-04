package org.tofu.pvpWorld.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class itemStackMaker {

    public static ItemStack createItem(Component displayName, Material material, int i) {
        ItemStack item = new ItemStack(material, i);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(displayName);
            item.setItemMeta(meta);
        }

        return item;
    }
}