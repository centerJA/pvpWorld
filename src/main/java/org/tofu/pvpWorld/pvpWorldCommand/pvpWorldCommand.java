package org.tofu.pvpWorld.pvpWorldCommand;

import org.tofu.pvpWorld.Config;
import org.tofu.pvpWorld.PvpWorld;
import org.tofu.pvpWorld.utils.ffaGames.SpleefActivities;
import org.tofu.pvpWorld.utils.lobbyAthletic.AthleticUtils;
import org.tofu.pvpWorld.utils.oneVersusOne.SumoActivities;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunActionMulti;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tofu.pvpWorld.utils.textComponent;
import org.tofu.pvpWorld.utils.yamlProperties.playerAdminList;

import javax.swing.text.JTextComponent;
import java.io.IOException;

import static org.tofu.pvpWorld.utils.yamlProperties.coinUtils.getPlayerCoin;
import static org.tofu.pvpWorld.utils.yamlProperties.coinUtils.playerSetCoin;
import static org.tofu.pvpWorld.utils.yamlProperties.expUtils.getPlayerExp;
import static org.tofu.pvpWorld.utils.yamlProperties.expUtils.playerSetExp;
import static org.tofu.pvpWorld.utils.yamlProperties.playerAdminList.*;

public class pvpWorldCommand implements CommandExecutor {
    private static final PvpWorld plugin = PvpWorld.getPlugin(PvpWorld.class);

    World world;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String playerName = player.getName();
        World world = player.getWorld();
        if (command.getName().equalsIgnoreCase("pvpworld")) { //pvpworld
            if (args.length == 0) {
                player.sendMessage(textComponent.parse("PVP WORLDへようこそ"));
                player.sendMessage(textComponent.parse("<red>></red><dark_purple>></dark_purple><dark_red>></dark_red>コマンド一覧<dark_red><</dark_red><dark_purple><</dark_purple><red><</red>"));
//                player.sendMessage(ChatColor.RED + ">" + ChatColor.DARK_PURPLE + ">" + ChatColor.DARK_RED + ">" + "コマンド一覧" + ChatColor.DARK_RED + "<" + ChatColor.DARK_PURPLE + "<" + ChatColor.RED + "<");

                return true;
            } else if (args[0].equals("op")) {//pvpworld op
                if (playerHasAdmin(player)) {
                    player.sendMessage(textComponent.parse("playerAdminListに登録されています"));
                } else {
                    player.sendMessage(textComponent.parse("playerAdminListに登録されていません"));
                }
                if (playerAdminList.playerHasAdmin(player)) {
                    if (args[1].equals("bm")) { //pvpworld op bm
                        if(args[2].equals("true")) { //pvpworld op bm true
                            if (!Config.AdminBuildModeList.contains(playerName)) {
                                Config.AdminBuildModeList.add(playerName);
                                player.sendMessage(textComponent.parse("ビルドモードに切り替えました"));
                                return true;
                            } else {
                                player.sendMessage(textComponent.parse("既にビルドモードです"));
                                return true;
                            }
                        } else if (args[2].equals("false")) { //pvpworld op bm false
                            Config.AdminBuildModeList.remove(playerName);
                            player.sendMessage(textComponent.parse("ノーマルモードに切り替えました"));
                            return true;
                        }
                    } else if (args[1].equals("gm")) { //pvpworld op gm
                        if (args[2].equals("c")) {//pvpworld op gm c
                            player.setGameMode(GameMode.CREATIVE);
                            player.sendMessage(textComponent.parse("クリエイティブモードに切り替えました"));
                            return true;
                        } else if (args[2].equals("s")) { //pvpworld op gm s
                            player.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage(textComponent.parse("サバイバルモードに切り替えました"));
                            return true;
                        }
                    } else if (args[1].equals("info")) { //pvpworld op info
                        player.sendMessage(textComponent.parse("WorldAllPlayerList"));
                        player.sendMessage(textComponent.parse(String.valueOf(Config.WorldAllPlayerList)));
//                        player.sendMessage("DoNotReceiveDamageList");
//                        player.sendMessage(String.valueOf(Config.DoNotReceiveDamageList));
//                        player.sendMessage("AdminBuildModeList");
//                        player.sendMessage(String.valueOf(Config.AdminBuildModeList));
//                        player.sendMessage("SpeedRunSingleOnHoldList");
//                        player.sendMessage(String.valueOf(Config.SpeedRunSingleOnHoldList));
//                        player.sendMessage("SpeedRunSingleList");
//                        player.sendMessage(String.valueOf(Config.SpeedRunSingleList));
//                        player.sendMessage("YourExp Score");
//                        player.sendMessage(String.valueOf(getPlayerExp(player)));
//                        player.sendMessage("YourCoin Score");
//                        player.sendMessage(String.valueOf(getPlayerCoin(player)));
//                        player.sendMessage("FreePvpPlayerList");
//                        player.sendMessage(String.valueOf(Config.FreePvpPlayerList));
//                        player.sendMessage("sumoQueueingList");
//                        player.sendMessage(String.valueOf(SumoActivities.sumoQueueingList));
//                        player.sendMessage("spleefQueueingList");
//                        player.sendMessage(String.valueOf(SpleefActivities.spleefQueueingList));
//                        player.sendMessage("speedrunMultiList");
//                        player.sendMessage(String.valueOf(SpeedRunActionMulti.multiPlayingList));
//                        printAllAdminPlayers(player);
                    } else if (args[1].equals("getexp")) { //pvpworld op getexp
                        try {
                            playerSetExp(player, 5);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (args[1].equals("fill")) {
                        double x = Integer.parseInt(args[2]);
                        double y = Integer.parseInt(args[3]);
                        double z = Integer.parseInt(args[4]);
                        Location location = new Location(world, x, y, z);
                        Block block = location.getBlock();
                        block.setType(Material.AIR);
                        player.sendMessage(textComponent.parse("x: " + args[2] + "y: " + args[3] + "z: " + args[4] + "を正常にクリアしました"));
                    } else if (args[1].equals("getcoin")) {
                        try {
                            playerSetCoin(player, Integer.parseInt(args[2]));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    player.sendMessage(textComponent.parse("<aqua>ADMINユーザーのみ使用できます!</aqua>"));
                    return true;
                }
            } else if (args[0].equals("notice")) { //pvpworld notice
                player.sendMessage(textComponent.parse(Config.worldUpdateNotice()));
                return true;
            } else if (args[0].equals("actions")) {
                if (args[1].equals("lobbyAthletic")) {
                    if (args[2].equals("clear")) { //pvpworld actions lobbyAthletic clear
                        AthleticUtils.clearAthleticTimes(player);
                    }
                }
            } else if (args[0].equals("help")) {
                player.sendMessage(textComponent.parse("<click:run_command:'/pvpworld command'><aqua>・コマンド一覧を表示する</click>"));
                player.sendMessage(textComponent.parse("<click:open_url:'https://google.com'><aqua>・ホームページにアクセスする(現在アクセスできません)</click>"));
                player.sendMessage(textComponent.parse("ヘルプが必要ですか？"));
                player.sendMessage(textComponent.parse("押すと、それに応じたヘルプが表示されます"));
            } else if (args[0].equals("command")) {
                player.sendMessage(textComponent.parse("<b><yellow>-----PVP WORLD-----</yellow></b>"));
                player.sendMessage(textComponent.parse("<click:run_command:'/pvpworld'>/pvpworld -このワールドについてを表示します</click>"));
                player.sendMessage("<click:run_command:'/pvpworld help'>/pvpworld help -ヘルプを表示します</click>");
                player.sendMessage("<click:run_command:'/pvpworld command'>/pvpworld command -コマンドのリストを表示します</click>");
                player.sendMessage("<click:run_command:'/pvpworld notice'>/pvpworld notice -お知らせを表示します</click>");
            }
        }
        return false;
    }
}
