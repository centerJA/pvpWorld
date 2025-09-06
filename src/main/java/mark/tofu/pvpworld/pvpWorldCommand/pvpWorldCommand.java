package mark.tofu.pvpworld.pvpWorldCommand;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
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
            if (args.length == 0) {
                player.sendMessage("PVP WORLDへようこそ");
            }
        } else if(args[0].equals("buildmode")) {
            if(args[1].equals("true")) {
                String playerName = player.getName();
                if (playerName.equals("markcs11") || playerName.equals("InfInc")) {
                    if (!Config.adminBuildModeList.contains(playerName)) {
                        Config.adminBuildModeList.add(playerName);
                        player.sendMessage("ビルドモードに切り替えました");
                    } else {
                        player.sendMessage("既にビルドモードです");
                    }
                } else {
                    player.sendMessage("権限がありません");
                }
            } else if (args[1].equals("false")) {
                String playerName = player.getName();
                Config.adminBuildModeList.remove(playerName);
                player.sendMessage("ノーマルモードに切り替えました");
            }
        }
        return false;
    }
}
