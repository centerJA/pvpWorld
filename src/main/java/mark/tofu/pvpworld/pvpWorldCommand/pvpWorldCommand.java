package mark.tofu.pvpworld.pvpWorldCommand;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

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
            } else if (args[0].equals("op")) { //pvpworld op
                if (playerName.equals("markcs11") || playerName.equals("InfInc")) {
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
                        player.sendMessage(String.valueOf(Config.getPlayerExp(player)));
                    } else if (args[1].equals("getexp")) { //pvpworld op getexp
                        try {
                            Config.playerSetLoginExp(player);
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
            }
        }
        return false;
    }
}
