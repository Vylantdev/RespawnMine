package be.gsnauw.respawnmine;

import org.bukkit.plugin.java.JavaPlugin;

public final class RespawnMine extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("The plugin has started, Hello World!");
    }

    @Override
    public void onDisable() {
        getLogger().info("The plugin has been disabled, goodbye!");
    }
}
