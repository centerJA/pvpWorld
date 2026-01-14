package mark.tofu.pvpworld.utils.wellUtils;

import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.yamlProperties.coinUtils;
import mark.tofu.pvpworld.utils.yamlProperties.expUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
        loreList.add("");
        loreList.add(ChatColor.RED + "消費: " + ChatColor.GOLD + "10Gold");
        loreList.add(ChatColor.YELLOW + ">>右クリックして井戸から入手<<");
        Objects.requireNonNull(meta).setLore(loreList);
        meta.setDisplayName(ChatColor.DARK_PURPLE + "井戸");
        item.setItemMeta(meta);
        return item;
    }

    public static void rollItems(Player player, PvpWorld plugin) throws IOException {
        player.closeInventory();
        coinUtils.playerSetCoin(player, -10);
        //soundを追加する
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int value = random.nextInt(10000) + 1;
                if (value <= 5000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "8Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.WHITE + "コモン", "", 20, 40, 20);
                    try {
                        coinUtils.playerSetCoin(player, 8);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 7000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "12Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.GREEN + "アンコモン", "", 20, 40, 20);
                    try {
                        coinUtils.playerSetCoin(player, 12);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 8000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "20Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.AQUA + "レア", "", 20, 40, 20);
                    try {
                        coinUtils.playerSetCoin(player, 20);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 8100) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "100Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.BLUE + "スーパーレア", "", 20, 40, 20);
                    try {
                        coinUtils.playerSetCoin(player, 100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 8150) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "200Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.DARK_PURPLE + "エピック", "", 20, 40, 20);
                    try {
                        coinUtils.playerSetCoin(player, 400);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 8155) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "700Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.DARK_PURPLE + "レジェンダリー", "", 20, 40, 20);
                    try {
                        coinUtils.playerSetCoin(player, 700);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    expUtils.playerSetExp(player, 2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 80L);
    }
}