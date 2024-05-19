package be.gsnauw.respawnmine.configs.languages;

import be.gsnauw.respawnmine.configs.DefaultConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ENConfig {
    private final File configFile;
    private final YamlConfiguration config;

    public ENConfig(String filename, Plugin instance) {
        this.configFile = new File(instance.getDataFolder(), filename);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        init();
    }

    private void init() {
        List<String> header = new ArrayList<>();
        header.add("--English Language Config--");
        header.add("***************************");


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
        testDefaultConfig();
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

    private boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }


    private int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, def);
    }

    private String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, def);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getList(String path, List<T> def) {
        config.addDefault(path, def);
        return (List<T>) config.getList(path, def);
    }

    public String test = "Hallo, dit is een test.";

    private void testDefaultConfig() {
        test = getString("test", test);
    }
}
