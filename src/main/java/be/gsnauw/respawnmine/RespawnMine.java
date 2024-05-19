package be.gsnauw.respawnmine;

import be.gsnauw.respawnmine.commands.ResMineCommand;
import be.gsnauw.respawnmine.commands.TestCommand;
import be.gsnauw.respawnmine.configs.DefaultConfig;
import be.gsnauw.respawnmine.listeners.BlockListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RespawnMine extends JavaPlugin {

    @Getter
    private DefaultConfig defaultConfig;

    @Getter
    private static RespawnMine instance;

    @Override
    public void onEnable() {
        instance = this;
        this.defaultConfig = new DefaultConfig("config.yml", this);

        getLogger().info("The plugin has started, Hello World!");
        Objects.requireNonNull(this.getCommand("respawnmine")).setExecutor(new ResMineCommand());
        Objects.requireNonNull(this.getCommand("test")).setExecutor(new TestCommand());
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("The plugin has been disabled, goodbye!");
    }


}
