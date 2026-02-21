package mark.tofu.pvpworld.utils.wellUtils;

import mark.tofu.pvpworld.Config;
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
        inventory.setItem(29, pickItemProperties());
        inventory.setItem(33, pickItemPropertiesBIG());
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
        meta.setDisplayName(ChatColor.RED + "小さな井戸");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack pickItemPropertiesBIG() {
        ItemStack item = new ItemStack(Material.GOLD_BLOCK, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.WHITE + "100Goldを使用して、レアアイテムや");
        loreList.add(ChatColor.WHITE + "Goldなどを入手できます!!");
        loreList.add(ChatColor.GREEN + "何がでるかはわかりません");
        loreList.add(ChatColor.YELLOW + "⚠注意:基本は損します");
        loreList.add(ChatColor.YELLOW + "一攫千金です");
        loreList.add("");
        loreList.add(ChatColor.RED + "消費: " + ChatColor.GOLD + "100Gold");
        loreList.add(ChatColor.YELLOW + ">>右クリックして井戸から入手<<");
        Objects.requireNonNull(meta).setLore(loreList);
        meta.setDisplayName(ChatColor.DARK_PURPLE + "大きな井戸");
        item.setItemMeta(meta);
        return item;
    }


    public static void rollItems(Player player, PvpWorld plugin) throws IOException {
        player.closeInventory();
        if (coinUtils.getPlayerCoin(player) < 10) {
            player.sendMessage(ChatColor.RED + "あなたは10coinを所持していません!");
            return;
        }
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
                    player.sendTitle(ChatColor.BLUE + "ユニーク", "", 20, 40, 20);
                    sendRareMessage(player, ChatColor.BLUE + "[ユニーク]", "100");
                    try {
                        coinUtils.playerSetCoin(player, 100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 8150) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "200Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.DARK_PURPLE + "エピック", "", 20, 40, 20);
                    sendRareMessage(player, ChatColor.DARK_PURPLE + "[エピック]", "200");
                    try {
                        coinUtils.playerSetCoin(player, 400);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 8155) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "700Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.GOLD + "レジェンダリー", "", 20, 40, 20);
                    sendRareMessage(player, ChatColor.GOLD + "[レジェンダリー]", "700");
                    try {
                        coinUtils.playerSetCoin(player, 700);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 8156) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "1000Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.BLACK + "インセイン", "", 20, 40, 20);
                    sendRareMessage(player, ChatColor.BLACK + "[インセイン]", "1000");
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


    public static void rollItemsBIG(Player player, PvpWorld plugin) throws IOException {
        player.closeInventory();
        if (coinUtils.getPlayerCoin(player) < 100) {
            player.sendMessage(ChatColor.RED + "あなたは100coinを所持していません!");
            return;
        }
        coinUtils.playerSetCoin(player, -100);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int value = random.nextInt(10000) + 1;
                if (value <= 5000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "50Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.RED + "損", "", 20, 40, 20);
                    try {
                        coinUtils.playerSetCoin(player, 50);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 7000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "200Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.YELLOW + "利益!", "", 20, 40, 20);
                    try {
                        coinUtils.playerSetCoin(player, 100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }  else if (value <= 7100) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "1000Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.BLUE + "利益!!", "", 20, 40, 20);
                    sendRareMessage(player, ChatColor.BLUE + "[利益!!]", "1000");
                    try {
                        coinUtils.playerSetCoin(player, 100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 7105) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "4000Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.GOLD + "極上", "", 20, 40, 20);
                    sendRareMessage(player, ChatColor.GOLD + "[極上]", "4000");
                    try {
                        coinUtils.playerSetCoin(player, 700);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (value <= 7106) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(ChatColor.GOLD + "8000Gold" + ChatColor.WHITE + "を入手しました!!");
                    player.sendTitle(ChatColor.BLACK + "伝説", "", 20, 40, 20);
                    sendRareMessage(player, ChatColor.BLACK + "[伝説]", "89000");
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



    public static void sendRareMessage(Player player, String rare, String gold) {
        for (String PlayerName: Config.WorldAllPlayerList) {
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(ChatColor.GOLD + player.getName() + ChatColor.WHITE + "さんが井戸で" + rare + ChatColor.GOLD + gold + "gold" + ChatColor.WHITE + "を入手しました!!");
        }
    }
}