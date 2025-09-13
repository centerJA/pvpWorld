package mark.tofu.pvpworld.pvpWorldCommand;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class pvpWorldCommand implements CommandExecutor {
    private static final PvpWorld plugin = PvpWorld.getPlugin(PvpWorld.class);

    World world;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        World world = player.getWorld();
        if (command.getName().equalsIgnoreCase("pvpworld")) {
            if (args.length == 0) { //pvpworld
                player.sendMessage(ChatColor.AQUA + "PVP WORLDへようこそ");
                return true;
            } else if (args[0].equals("buildmode")) { //pvpworld buildmode
                if (args.length < 2) { //pvpworld buildmode
                    player.sendMessage(ChatColor.AQUA + "プレイヤーをbuildモードまたはnormalモードに変更します");
                    player.sendMessage(ChatColor.AQUA + "使い方: /pvpworld buildmode true/false");
                    return true;
                } else if (args[1].equals("true")) { //pvpworld buildmode true
                    String playerName = player.getName();
                    player.sendMessage(playerName);
                    if (playerName.equals("markcs11") || playerName.equals("InfInc")) {
                        if (!Config.adminBuildModeList.contains(playerName)) {
                            Config.adminBuildModeList.add(playerName);
                            player.sendMessage("ビルドモードに切り替えました");
                            player.sendMessage(String.valueOf(Config.adminBuildModeList));
                        } else {
                            player.sendMessage("既にビルドモードです");
                        }
                    } else {
                        player.sendMessage("権限を持っていません");
                    }
                } else if(args[1].equals("false")) { //pvpworld buildmode false
                    String playerName = player.getName();
                    player.sendMessage(playerName);
                    Config.adminBuildModeList.remove(playerName);
                    player.sendMessage("ノーマルモードに切り替えました");
                    player.sendMessage(String.valueOf(Config.adminBuildModeList));
                }
            } else if (args[0].equals("worldinfo")) { //pvpworld worldinfo
                String playerName = player.getName();
                if (playerName.equals("markcs11") || playerName.equals("InfInc")) {
                    player.sendMessage("worldAllPlayerList");
                    player.sendMessage(String.valueOf(Config.worldAllPlayerList));
                    player.sendMessage("doNotReceiveDamageList");
                    player.sendMessage(String.valueOf(Config.doNotReceiveDamageList));
                    player.sendMessage("SpeedRunSingleOnHoldList");
                    player.sendMessage(String.valueOf(Config.SpeedRunSingleOnHoldList));
                    player.sendMessage("adminBuildModeList");
                    player.sendMessage(String.valueOf(Config.adminBuildModeList));
                } else {
                    player.sendMessage("権限がありません");
                }
            }
        }
        return false;
    }
}
