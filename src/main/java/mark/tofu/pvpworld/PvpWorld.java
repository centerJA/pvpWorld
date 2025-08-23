package mark.tofu.pvpworld;

import mark.tofu.pvpworld.worldEvents.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class PvpWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new playerChangeWorldEvent(this);
        new playerDeathEvent(this);
        new playerRespawnEvent(this);
        new entityDamageEvent(this);
        new blockBreakEvent(this);
        new playerInteractEvent(this);
        new foodLevelChangeEvent(this);
        new onPlayerQuitEvent(this);
        World world = Bukkit.getWorld("pvpWorld");
        if (world == null) return;
        Bukkit.getLogger().info("pvpWorld enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
