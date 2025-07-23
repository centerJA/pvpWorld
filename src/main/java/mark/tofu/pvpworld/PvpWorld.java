package mark.tofu.pvpworld;

import mark.tofu.pvpworld.worldOptions.PlayerChangeWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class PvpWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new PlayerChangeWorldEvent(this);
        World world = Bukkit.getWorld("pvpWorld");
        if (world == null) return;
        Bukkit.getLogger().info("pvpWorld enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
