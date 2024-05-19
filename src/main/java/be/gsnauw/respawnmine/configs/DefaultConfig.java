package be.gsnauw.respawnmine.configs;

import be.gsnauw.respawnmine.RespawnMine;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;

public final class DefaultConfig {

    public static class MapBuilder<K, V> implements Serializable {

        private final Map<K, V> map = new HashMap<>();
        public MapBuilder<K, V> put(K k, V v) {
            this.map.put(k, v);
            return this;
        }
        public MapBuilder<K, V> put(K[] array, V v) {
            Arrays.stream(array).forEach(k -> this.put(k, v));
            return this;
        }
        public MapBuilder<K, V> putAll(Map<? extends K, ? extends V> map) {
            this.map.putAll(map);
            return this;
        }
        public Map<K, V> build() {
            return this.map;
        }

    }
    private final File configFile;
    private final YamlConfiguration config;

    public DefaultConfig(String filename, Plugin instance) {
        this.configFile = new File(instance.getDataFolder(), filename);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        init();
    }

    private void init() {
        List<String> header = new ArrayList<>();
        header.add("--Default Config--");
        header.add("******************");


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
        test();
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

    public int aantal = 2;
    public String hoi = "hello";
    public boolean mss = true;
    public List<String> test = new ArrayList<>();

    private void testDefaultConfig() {
        aantal = getInt("Aantal", aantal);
        hoi = getString("wow", hoi);
        test = getList("lijst.test", test);
        mss = getBoolean("boolean.mss", mss);
    }

    private Map<String, Map<String, String>> getRegionMap(String path, Map<String, Map<String, String>> def) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            config.createSection(path, def);
            return def;
        }
        Map<String, Map<String, String>> regionMap = new HashMap<>();
        for (String key : section.getKeys(false)) {
            ConfigurationSection regionSection = section.getConfigurationSection(key);
            if (regionSection != null) {
                Map<String, String> blockMap = new HashMap<>();
                for (String block : regionSection.getKeys(false)) {
                    blockMap.put(block, regionSection.getString(block));
                }
                regionMap.put(key, blockMap);
            }
        }
        return regionMap;
    }



    public Map<String, Map<String, String>> regions = new MapBuilder<String, Map<String, String>>()
            .put("Eosmine", new MapBuilder<String, String>()
                    .put("DIAMOND_ORE", "5m")
                    .put("GOLD_ORE", "2m")
                    .put("DEEPSLATE_GOLD_ORE", "2m")
                    .put("DEEPSLATE_DIAMOND_ORE", "2m")
                    .put("DEEPSLATE_EMERALD_ORE", "2m")
                    .build())
            .put("Anderemine", new MapBuilder<String, String>()
                    .put("DIAMOND_ORE", "5m")
                    .put("EMERALD_ORE", "5m")
                    .build())
            .build();

    private void test() {
        regions =  getRegionMap("Regions", regions);
    }

    public static DefaultConfig DefCon() {
        return RespawnMine.getInstance().getDefaultConfig();
    }
}
