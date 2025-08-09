package mark.tofu.pvpworld.worldEvents;

import mark.tofu.pvpworld.Config;
import mark.tofu.pvpworld.PvpWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangeWorldEvent implements Listener {
    PvpWorld plugin;

    private World world;

//    public static ArrayList<String> worldAllPlayerList;
    public PlayerChangeWorldEvent(PvpWorld plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Bukkit.getLogger().info("pvpWorld plugin loaded");
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                world = Bukkit.getWorld("pvpWorld");
            }
        }, 10L);
//        worldAllPlayerList = new ArrayList<>();
    }

    @EventHandler
    public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        if (this.world != world) { //他のワールドに移動した時
            if(Config.worldAllPlayerList.contains(playerName)) {
                Config.worldAllPlayerList.remove(playerName);
            }
            if (Config.doNotReciveDamageList.contains(playerName)) {
                Config.doNotReciveDamageList.remove(playerName);
            }
        } else { //自分のサーバーに来た時
            if (!Config.worldAllPlayerList.contains(playerName)) {
                Config.worldAllPlayerList.add(playerName);
                if (!Config.doNotReciveDamageList.contains(playerName)) {
                    Config.doNotReciveDamageList.add(playerName);
                }
                player.teleport(Config.lobby);
                player.setGameMode(GameMode.SURVIVAL);
                player.setFoodLevel(20);
                player.setHealth(20);
                player.sendTitle(player.getName() + ChatColor.AQUA + "さん", ChatColor.AQUA + "こんにちは！", 20, 40, 20);

            }
        }
        if (playerName.equals("InfInc") || playerName.equals("markcs11")) {
            player.sendMessage(String.valueOf(Config.worldAllPlayerList));
        }
    }
}
