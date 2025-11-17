package mark.tofu.pvpworld;

import mark.tofu.pvpworld.pvpWorldCommand.pvpWorldCommand;
import mark.tofu.pvpworld.worldEvents.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static mark.tofu.pvpworld.utils.yamlProperties.athleticTimeUtils.lobbyAthleticSetUp;
import static mark.tofu.pvpworld.utils.yamlProperties.coinUtils.playerCoinSetUp;
import static mark.tofu.pvpworld.utils.yamlProperties.expUtils.playerExpSetup;
import static mark.tofu.pvpworld.utils.yamlProperties.playerAdminList.playerAdminListSetup;

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
        new asyncPlayerChatEvent(this);
        Objects.requireNonNull(getCommand("pvpworld")).setExecutor(new pvpWorldCommand());
        World world = Bukkit.getWorld("pvpWorld");
        if (world == null) return;
        playerExpSetup(this);
        Config.playerLastLoginSetup(this);
        lobbyAthleticSetUp(this);
        playerCoinSetUp(this);
        playerAdminListSetup(this);
        Bukkit.getLogger().info("pvpWorld enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
