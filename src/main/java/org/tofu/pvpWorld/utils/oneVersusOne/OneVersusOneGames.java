package org.tofu.pvpWorld.utils.oneVersusOne;

import net.kyori.adventure.text.Component;
import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.itemStackMaker;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.tofu.pvpWorld.utils.titleMaker;

import java.util.ArrayList;
import java.util.Objects;

public class OneVersusOneGames {
    public static void gameCloseAction(PvpWorld plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (String PlayerName: Config.TeleportToLobbyList) {
                    Player player = Bukkit.getPlayer(PlayerName);
                    if (player == null) return;
                    player.getInventory().clear();
                    StartTimerUtils.stopTimer(player);
                    TimeUpTimer.stopTimer(player);
                    player.teleport(Config.lobby);
                    player.getInventory().setItem(0, itemStackMaker.createItem(textComponent.parse("<white>ロビーに戻る"), Material.RED_MUSHROOM, 1));
                }
                for (String PlayerName: Config.TeleportToLobbyList) {
                    Config.TeleportToLobbyList.remove(PlayerName);
                }
                TextDisplayUtils.renameOneVersusOneSize(OneVersusOneAllPlayer());
            }
        }, 60L);
    }

    public static void drawAction(ArrayList<String> arrayList, PvpWorld plugin) {
        for (String PlayerName: arrayList) {
            Player player = Bukkit.getPlayer(PlayerName);
            if (player == null) return;
            player.showTitle(titleMaker.title(textComponent.parse("<yellow>引き分け"), textComponent.parse("<yellow>勝利までもう少し!!"), 0, 3000, 0));
            Config.TeleportToLobbyList.addAll(arrayList);
            gameCloseAction(plugin);
        }
    }

    public static void noWalkRemoveAction(ArrayList<String> arrayList) {
        for (String PlayerName: arrayList) {
            Config.NoWalkList.remove(PlayerName);
        }
    }

    public static void timeUpAction(Player player, PvpWorld plugin) {
        String playerName = player.getName();
        if (SumoActivities.sumoQueueingList.contains(playerName)) {
            drawAction(SumoActivities.sumoQueueingList, plugin);
        }
        else if (TopfightActivities.topfightQueueingList.contains(playerName)) {
            drawAction(TopfightActivities.topfightQueueingList, plugin);
        }
    }

    public static void queueingActivities(Player player, InventoryClickEvent e, PvpWorld plugin, ArrayList<String> arrayList) {
        player.sendMessage(String.valueOf(arrayList));
        InventoryUtils.replaceInventoryCheck(player);
        if (arrayList.isEmpty()) {
            arrayList.add(player.getName());
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage(textComponent.parse("<white>他の人を待っています..."));
            player.sendMessage(textComponent.parse("<white>参加をやめるには、インベントリの中の赤色の染料を右クリックしてください"));
            encourageJoinGame(player);
            TextDisplayUtils.renameOneVersusOneSize(OneVersusOneAllPlayer());
            player.getInventory().setItem(8, itemStackMaker.createItem(textComponent.parse("<white>ゲームをやめる"), Material.RED_DYE, 1));
        } else if (arrayList.size() == 1) {
            for (String PlayerName: arrayList) {
                if (PlayerName.equals(player.getName())) {
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage(textComponent.parse("<white>既に参加しています!"));
                    player.sendMessage(textComponent.parse("<white>退出するにはインベントリ内の赤い染料を右クリックしてください"));
                } else {
                    arrayList.add(player.getName());
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage(textComponent.parse("<white>相手が見つかりました!"));
                    TextDisplayUtils.renameOneVersusOneSize(OneVersusOneAllPlayer());
                    dividePlayer(arrayList, player, plugin);
                }
            }
        } else {
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage(textComponent.parse("<white>既に誰かがプレイ中です"));
        }
    }

    public static void dividePlayer(ArrayList<String> arrayList, Player player, PvpWorld plugin) {
        if (arrayList.equals(SumoActivities.sumoQueueingList)) {
            SumoActivities.sumoStartAction(player, plugin);
        }
        else if (arrayList.equals(TopfightActivities.topfightQueueingList)) {
            TopfightActivities.topfightStartAction(player, plugin);
        }
        else {
            player.sendMessage(textComponent.parse("<white>エラー"));
        }
    }

    public static void encourageJoinGame(Player player) {
        String base = "1人が対戦相手を待機中です!";
        if (SumoActivities.sumoQueueingList.contains(player.getName())) {
            for (String PlayerName: Config.WorldAllPlayerList) {
                Player player2 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                player2.sendMessage(textComponent.parse("<yellow>[Sump] <white>" + base));
            }
        }
        else if (TopfightActivities.topfightQueueingList.contains(player.getName())) {
            for (String PlayerName: Config.WorldAllPlayerList) {
                Player player2 = Objects.requireNonNull(Bukkit.getPlayer(PlayerName));
                player2.sendMessage("<red>[TopFight] <white>" + base);
            }
        }
    }


    public static int OneVersusOneAllPlayer() {
        int p = 0;
        p = p + SumoActivities.sumoQueueingList.size();
        p = p + TopfightActivities.topfightQueueingList.size();
        return p;
    }

    public static void clickedRed_Dye(Player player) {
        if (SumoActivities.sumoQueueingList.contains(player.getName())) {
            SumoActivities.sumoQueueingList.remove(player.getName());
            TextDisplayUtils.renameOneVersusOneSize(OneVersusOneGames.OneVersusOneAllPlayer());
        } else if (TopfightActivities.topfightQueueingList.contains(player.getName())) {
            TopfightActivities.topfightQueueingList.remove(player.getName());
            TextDisplayUtils.renameOneVersusOneSize(OneVersusOneGames.OneVersusOneAllPlayer());
        }
        if (player.getInventory().getItemInMainHand().getType().equals(Material.RED_DYE)) {
            player.getInventory().setItemInMainHand(null);
            player.sendMessage(Component.text("退出しました"));
        }
    }
}
