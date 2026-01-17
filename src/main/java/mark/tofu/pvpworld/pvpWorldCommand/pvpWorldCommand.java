package mark.tofu.pvpworld.pvpWorldCommand;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import mark.tofu.pvpworld.utils.athletic.AthleticUtils;
import mark.tofu.pvpworld.utils.ffaGames.SpleefActivities;
import mark.tofu.pvpworld.utils.oneVersusOne.SumoActivities;
import mark.tofu.pvpworld.utils.scoreBoard.ScoreBoardUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

import static mark.tofu.pvpworld.utils.yamlProperties.coinUtils.getPlayerCoin;
import static mark.tofu.pvpworld.utils.yamlProperties.coinUtils.playerSetCoin;
import static mark.tofu.pvpworld.utils.yamlProperties.expUtils.getPlayerExp;
import static mark.tofu.pvpworld.utils.yamlProperties.expUtils.playerSetExp;
import static mark.tofu.pvpworld.utils.yamlProperties.playerAdminList.*;

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
                player.sendMessage("PVP WORLDへようこそ");
                player.sendMessage(ChatColor.RED + ">" + ChatColor.DARK_PURPLE + ">" + ChatColor.DARK_RED + ">" + "コマンド一覧" + ChatColor.DARK_RED + "<" + ChatColor.DARK_PURPLE + "<" + ChatColor.RED + "<");

                return true;
            } else if (args[0].equals("op")) {//pvpworld op
                if (playerHasAdmin(player)) {
                    player.sendMessage("playerAdminListに登録されています");
                } else {
                    player.sendMessage("playerAdminListに登録されていません");
                }
                if (playerName.equals("markcs11") || playerName.equals("InfInc") || playerName.equals("m1n_Dry_Eye") || playerName.equals("10000m")) {
                    if (args[1].equals("bm")) { //pvpworld op bm
                        if(args[2].equals("true")) { //pvpworld op bm true
                            if (!Config.AdminBuildModeList.contains(playerName)) {
                                Config.AdminBuildModeList.add(playerName);
                                player.sendMessage("ビルドモードに切り替えました");
                                return true;
                            } else {
                                player.sendMessage("既にビルドモードです");
                                return true;
                            }
                        } else if (args[2].equals("false")) { //pvpworld op bm false
                            Config.AdminBuildModeList.remove(playerName);
                            player.sendMessage("ノーマルモードに切り替えました");
                            return true;
                        }
                    } else if (args[1].equals("gm")) { //pvpworld op gm
                        if (args[2].equals("c")) {//pvpworld op gm c
                            player.setGameMode(GameMode.CREATIVE);
                            player.sendMessage("クリエイティブモードに切り替えました");
                            return true;
                        } else if (args[2].equals("s")) { //pvpworld op gm s
                            player.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage("サバイバルモードに切り替えました");
                            return true;
                        }
                    } else if (args[1].equals("info")) { //pvpworld op info
                        player.sendMessage("WorldAllPlayerList");
                        player.sendMessage(String.valueOf(Config.WorldAllPlayerList));
                        player.sendMessage("DoNotReceiveDamageList");
                        player.sendMessage(String.valueOf(Config.DoNotReceiveDamageList));
                        player.sendMessage("AdminBuildModeList");
                        player.sendMessage(String.valueOf(Config.AdminBuildModeList));
                        player.sendMessage("SpeedRunSingleOnHoldList");
                        player.sendMessage(String.valueOf(Config.SpeedRunSingleOnHoldList));
                        player.sendMessage("SpeedRunSingleList");
                        player.sendMessage(String.valueOf(Config.SpeedRunSingleList));
                        player.sendMessage("YourExp Score");
                        player.sendMessage(String.valueOf(getPlayerExp(player)));
                        player.sendMessage("YourCoin Score");
                        player.sendMessage(String.valueOf(getPlayerCoin(player)));
                        player.sendMessage("FreePvpPlayerList");
                        player.sendMessage(String.valueOf(Config.FreePvpPlayerList));
                        player.sendMessage("sumoQueueingList");
                        player.sendMessage(String.valueOf(SumoActivities.sumoQueueingList));
                        player.sendMessage("spleefQueueingList");
                        player.sendMessage(String.valueOf(SpleefActivities.spleefQueueingList));
                        printAllAdminPlayers(player);
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
                        player.sendMessage("x: " + args[2] + "y: " + args[3] + "z: " + args[4] + "を正常にクリアしました");
                    } else if (args[1].equals("getcoin")) {
                        try {
                            playerSetCoin(player, Integer.parseInt(args[2]));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.AQUA + "ADMINユーザーのみ使用できます!");
                    return true;
                }
            } else if (args[0].equals("notice")) { //pvpworld notice
                player.sendMessage(ChatColor.AQUA + Config.worldUpdateNotice());
                return true;
            } else if (args[0].equals("actions")) {
                if (args[1].equals("lobbyAthletic")) {
                    if (args[2].equals("clear")) { //pvpworld actions lobbyAthletic clear
                        AthleticUtils.clearAthleticTimes(player);
                    }
                }
            } else if (args[0].equals("help")) {
                TextComponent showCommandList = new TextComponent(ChatColor.AQUA + "・コマンド一覧を表示する");
                showCommandList.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pvpworld command"));
                TextComponent sendHomePage = new TextComponent(ChatColor.AQUA + "・ホームページにアクセスする(現在アクセスできません)");
                sendHomePage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://google.com"));
                player.sendMessage("ヘルプが必要ですか？");
                player.sendMessage("押すと、それに応じたヘルプが表示されます");
                player.spigot().sendMessage(showCommandList);
                player.spigot().sendMessage(sendHomePage);
            } else if (args[0].equals("command")) {
                player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "-----PVP WORLD-----");
                player.sendMessage("/pvpworld -このワールドについてを表示します");
                player.sendMessage("/pvpworld help -ヘルプを表示します");
                player.sendMessage("/pvpworld command -コマンドのリストを表示します");
                player.sendMessage("/pvpworld notice -お知らせを表示します");
            }
        }
        return false;
    }
}
