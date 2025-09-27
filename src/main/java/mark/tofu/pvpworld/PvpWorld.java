package mark.tofu.pvpworld;

import mark.tofu.pvpworld.pvpWorldCommand.pvpWorldCommand;
import mark.tofu.pvpworld.worldEvents.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

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
        new inventoryClickEvent(this);
        new blockPlaceEvent(this);
        new playerMoveEvent(this);
        Objects.requireNonNull(getCommand("pvpworld")).setExecutor(new pvpWorldCommand());
        World world = Bukkit.getWorld("pvpWorld");
        if (world == null) return;
        Config.playerExpSetup(this);
        Config.playerLastRoginSetup(this);
        Bukkit.getLogger().info("pvpWorld enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
