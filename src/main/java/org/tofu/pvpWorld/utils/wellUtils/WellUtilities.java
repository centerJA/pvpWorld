package org.tofu.pvpWorld.utils.wellUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.yamlProperties.coinUtils;
import org.tofu.pvpWorld.utils.yamlProperties.expUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WellUtilities {
    public static void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, textComponent.parse("<bold><dark_purple>井戸"));
        inventory.setItem(29, pickItemProperties());
        inventory.setItem(33, pickItemPropertiesBIG());
        Objects.requireNonNull(player.getPlayer()).openInventory(inventory);
    }

    public static ItemStack pickItemProperties() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta meta = item.getItemMeta();
        List<Component> loreList = new ArrayList<>();
        loreList.add(textComponent.parse("<white>10Goldを使用して、レアアイテムや"));
        loreList.add(textComponent.parse("<white>Goldなどを入手できます!!"));
        loreList.add(textComponent.parse("<green>何がでるかはわかりません"));
        loreList.add(Component.empty());
        loreList.add(textComponent.parse("<red>消費: <gold>10Gold"));
        loreList.add(textComponent.parse("<yellow>>>右クリックして井戸から入手<<"));
        Objects.requireNonNull(meta).lore(loreList);
        meta.displayName(textComponent.parse("<red>小さな井戸"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack pickItemPropertiesBIG() {
        ItemStack item = new ItemStack(Material.GOLD_BLOCK, 1);
        ItemMeta meta = item.getItemMeta();
        List<Component> loreList = new ArrayList<>();
        loreList.add(textComponent.parse("<white>100Goldを使用して、レアアイテムや"));
        loreList.add(textComponent.parse("<white>Goldなどを入手できます!!"));
        loreList.add(textComponent.parse("<green>何がでるかはわかりません"));
        loreList.add(textComponent.parse("<yellow>⚠注意:基本は損します"));
        loreList.add(textComponent.parse("><yellow>一攫千金です"));
        loreList.add(Component.empty());
        loreList.add(textComponent.parse("<red>消費: <gold>100Gold"));
        loreList.add(textComponent.parse("<yellow>>>右クリックして井戸から入手<<"));
        Objects.requireNonNull(meta).lore(loreList);
        meta.displayName(textComponent.parse("<dark_purple>大きな井戸"));
        item.setItemMeta(meta);
        return item;
    }

    public static void rollItems(Player player, PvpWorld plugin) throws IOException {
        player.closeInventory();
        if (coinUtils.getPlayerCoin(player) < 10) {
            player.sendMessage(textComponent.parse("<red>あなたは10coinを所持していません!"));
            return;
        }
        coinUtils.playerSetCoin(player, -10);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int value = random.nextInt(10000) + 1;
                Title.Times titleTimes = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1));

                if (value <= 5000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>8Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<white>コモン"), Component.empty(), titleTimes));
                    coinUtils.playerSetCoin(player, 8);
                } else if (value <= 7000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>12Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<green>アンコモン"), Component.empty(), titleTimes));
                    coinUtils.playerSetCoin(player, 12);
                } else if (value <= 8000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>20Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<aqua>レア"), Component.empty(), titleTimes));
                    coinUtils.playerSetCoin(player, 20);
                } else if (value <= 8100) {
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>100Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<blue>ユニーク"), Component.empty(), titleTimes));
                    sendRareMessage(player, "<blue>[ユニーク]", "100");
                    coinUtils.playerSetCoin(player, 100);
                } else if (value <= 8150) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>200Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<dark_purple>エピック"), Component.empty(), titleTimes));
                    sendRareMessage(player, "<dark_purple>[エピック]", "200");
                    coinUtils.playerSetCoin(player, 400);
                } else if (value <= 8155) {
                    player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1F, 1);
                    player.sendMessage(textComponent.parse("<gold>700Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<gold>レジェンダリー"), Component.empty(), titleTimes));
                    sendRareMessage(player, "<gold>[レジェンダリー]", "700");
                    coinUtils.playerSetCoin(player, 700);
                } else if (value <= 8156) {
                    player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1F, 0.5F);
                    player.sendMessage(textComponent.parse("<gold>1000Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<black>インセイン"), Component.empty(), titleTimes));
                    sendRareMessage(player, "<black>[インセイン]", "1000");
                    coinUtils.playerSetCoin(player, 800);
                }
                expUtils.playerSetExp(player, 2);
            }
        }, 80L);
    }

    public static void rollItemsBIG(Player player, PvpWorld plugin) throws IOException {
        player.closeInventory();
        if (coinUtils.getPlayerCoin(player) < 100) {
            player.sendMessage(textComponent.parse("<red>あなたは100coinを所持していません!"));
            return;
        }
        coinUtils.playerSetCoin(player, -100);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int value = random.nextInt(10000) + 1;
                Title.Times titleTimes = Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1));

                if (value <= 5000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>50Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<red>損"), Component.empty(), titleTimes));
                    coinUtils.playerSetCoin(player, 50);
                } else if (value <= 7000) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>200Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<yellow>利益!"), Component.empty(), titleTimes));
                    coinUtils.playerSetCoin(player, 100);
                } else if (value <= 7100) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>1000Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<blue>利益!!"), Component.empty(), titleTimes));
                    sendRareMessage(player, "<blue>[利益!!]", "1000");
                    coinUtils.playerSetCoin(player, 200);
                } else if (value <= 7105) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>4000Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<gold>極上"), Component.empty(), titleTimes));
                    sendRareMessage(player, "<gold>[極上]", "4000");
                    coinUtils.playerSetCoin(player, 700);
                } else if (value <= 7106) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    player.sendMessage(textComponent.parse("<gold>8000Gold<white>を入手しました!!"));
                    player.showTitle(Title.title(textComponent.parse("<black>伝説"), Component.empty(), titleTimes));
                    sendRareMessage(player, "<black>[伝説]", "89000");
                    coinUtils.playerSetCoin(player, 1000);
                }
                expUtils.playerSetExp(player, 2);
            }
        }, 80L);
    }

    public static void sendRareMessage(Player player, String rare, String gold) {
        for (String PlayerName: Config.WorldAllPlayerList) {
            Objects.requireNonNull(Bukkit.getPlayer(PlayerName)).sendMessage(textComponent.parse("<gold>" + player.getName() + "<white>さんが井戸で" + rare + "<gold>" + gold + "gold<white>を入手しました!!"));
        }
    }
}