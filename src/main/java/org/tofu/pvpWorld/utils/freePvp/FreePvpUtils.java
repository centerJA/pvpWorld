package org.tofu.pvpWorld.utils.freePvp;

import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.lobbyAthletic.AthleticTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.tofu.pvpWorld.utils.textComponent;

import java.util.Objects;

public class FreePvpUtils {
    public static void ruleExplain(Player player) {
        player.sendMessage(textComponent.parse("<aqua>-----Free PVP-----"));
        player.sendMessage(textComponent.parse("<white>このゲームは、自由参加型の<gold>FFA<white>PVPゲームです!"));
        player.sendMessage(textComponent.parse("<white>FFAとは、味方がいない、全員敵のゲームのことです。"));
        player.sendMessage(textComponent.parse("<white>下の闘技場に飛び込むことで参加でき、3秒後に無敵化が切れてアイテムが支給されます"));
        player.sendMessage(textComponent.parse("<white>鉄剣、弓、矢8本、ヘルメット以外のチェーン装備が支給されます。"));
        player.sendMessage(textComponent.parse("<white>敵を倒すと金リンゴがもらえ、継続的に試合を続けられます!"));
        player.sendMessage(textComponent.parse("<white>退出する際は、インベントリ内にある赤いキノコをホットバーに移動させて、右クリックしてください。"));
        player.sendMessage(textComponent.parse("<aqua>---------------------"));
    }

    public static void joinAction(Player player, PvpWorld plugin) {

        if (!Config.FreePvpPlayerList.contains(player.getName())) {
            AthleticTimer.stopTimer(player);
            Config.FreePvpPlayerList.add(player.getName());
            player.getInventory().setItem(0, null);
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Config.DoNotReceiveDamageList.remove(player.getName());
                    player.getInventory().setItem(0, Config.itemMeta("聖なる剣", Material.IRON_SWORD, 1));
                    player.getInventory().setItem(1, Config.itemMeta("弓", Material.BOW, 1));
                    player.getInventory().setItem(8, Config.itemMeta("矢", Material.ARROW, 8));
                    player.getInventory().setItem(38, Config.itemMeta("チェストプレート", Material.CHAINMAIL_CHESTPLATE, 1));
                    player.getInventory().setItem(37, Config.itemMeta("レギンス", Material.CHAINMAIL_LEGGINGS, 1));
                    player.getInventory().setItem(36, Config.itemMeta("ブーツ", Material.CHAINMAIL_BOOTS, 1));
                    player.getInventory().setItem(12, Config.itemMeta("ロビーに戻る", Material.RED_MUSHROOM, 1));
                }
            }, 60L);
            for (String PlayerName: Config.WorldAllPlayerList) {
                Player player2 = Bukkit.getPlayer(PlayerName);
                Objects.requireNonNull(player2).sendMessage(textComponent.parse("<gold>"+ player.getName() + "さんがFree PVPスペースに参加しました"));
            }
            if (Config.FreePvpPlayerList.isEmpty()) {
                player.sendMessage(textComponent.parse("<white>現在誰もいません"));
            }
        } else {
            player.sendMessage(textComponent.parse("エラーが発生しました"));
        }
    }

    public static void freePvpPlayerRespawnAction(Player player, PvpWorld plugin) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Config.FreePvpPlayerList.remove(player.getName());
                Config.DoNotReceiveDamageList.add(player.getName());
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(Config.freePvpSpawnPoint);
            }
        }, 2L);
    }
}
