package org.tofu.pvpWorld;

import org.tofu.pvpWorld.pvpWorldCommand.pvpWorldCommand;
import org.tofu.pvpWorld.utils.athletic.AthleticProperties;
import org.tofu.pvpWorld.utils.speedRun.SpeedRunTimerMulti;
import org.tofu.pvpWorld.utils.textDisplay.TextDisplayUtils;
import org.tofu.pvpWorld.utils.yamlProperties.athleticTimeUtils;
import org.tofu.pvpWorld.utils.yamlProperties.coinUtils;
import org.tofu.pvpWorld.utils.yamlProperties.expUtils;
import org.tofu.pvpWorld.worldEvents.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static org.tofu.pvpWorld.utils.yamlProperties.athleticTimeUtils.lobbyAthleticSetUp;
import static org.tofu.pvpWorld.utils.yamlProperties.coinUtils.playerCoinSetUp;
import static org.tofu.pvpWorld.utils.yamlProperties.expUtils.playerExpSetup;
import static org.tofu.pvpWorld.utils.yamlProperties.playerAdminList.playerAdminListSetup;

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
        new asyncChatEvent(this);
        new playerInteractAtEntityEvent(this);
        Objects.requireNonNull(getCommand("pvpworld")).setExecutor(new pvpWorldCommand());
        World world = Bukkit.getWorld("pvpWorld");
        if (world == null) return;
        playerExpSetup(this);
        Config.playerLastLoginSetup(this);
        lobbyAthleticSetUp(this);
        playerCoinSetUp(this);
        playerAdminListSetup(this);
        Config.systemConfigSetUp(this);
        expUtils.sortEntries();
        coinUtils.sortEntries();
        athleticTimeUtils.sortEntries();
        AthleticProperties.setup();
        TextDisplayUtils.locationSetUp();
        getLogger().info("pvpWorld enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
