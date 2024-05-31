package be.gsnauw.respawnmine.configs;

import be.gsnauw.respawnmine.RespawnMine;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class StoredBlocksConfig {

    private final File configFile;
    private final YamlConfiguration config;

    public StoredBlocksConfig(String filename, Plugin instance) {
        this.configFile = new File(instance.getDataFolder(), filename);
        this.config = YamlConfiguration.loadConfiguration(configFile);
        init();
    }

    private void init() {
        List<String> header = new ArrayList<>();
        header.add("--Stored Blocks Config--");
        header.add("************************");
        header.add("Don't change this file if you don't know what you're doing!!!!!");


        config.options().setHeader(header);
        config.options().copyDefaults(true);
        readConfig();
    }

    public void reload() {
        try {
            this.config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("Could not reload " + configFile, e);
        }

        readConfig();
    }

    private void readConfig() {
        loadBlocks();
        saveConfig();
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
        }
    }

    public void save(String path, Object val) {
        config.set(path, val);
        saveConfig();
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getList(String path, List<T> def) {
        config.addDefault(path, def);
        return (List<T>) config.getList(path, def);
    }

    public List<Map<String, Object>> storedBlocks = new ArrayList<>();

    private void loadBlocks() {
        storedBlocks = getList("StoredBlocks", storedBlocks);
    }

    public static StoredBlocksConfig StoredCon() {
        return RespawnMine.getInstance().getStoredBlocksConfig();
    }

}
