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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class RegionConfig {
    public static class MapBuilder<K, V> implements Serializable {
        private final Map<K, V> map = new HashMap<>();

        public MapBuilder<K, V> put(K k, V v) {
            this.map.put(k, v);
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

    public RegionConfig(String filename, Plugin instance) {
        this.configFile = new File(instance.getDataFolder(), filename);
        this.config = YamlConfiguration.loadConfiguration(configFile);
        init();
    }

    private void init() {
        List<String> header = new ArrayList<>();
        header.add("--Region Config--");
        header.add("*****************");


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
        Regions();
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

    private Map<String, Map<String, Object>> getRegionMap(String path, Map<String, Map<String, Object>> def) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            config.createSection(path, def);
            return def;
        }

        Map<String, Map<String, Object>> regionMap = new HashMap<>();
        for (String key : section.getKeys(false)) {
            ConfigurationSection regionSection = section.getConfigurationSection(key);
            if (regionSection != null) {
                Map<String, Object> regionData = new HashMap<>();
                String replacementBlock = regionSection.getString("replacement-block");
                boolean enableLookup = regionSection.getBoolean("enable-lookup");
                boolean protectedRegion = regionSection.getBoolean("protected-region");

                regionData.put("replacement-block", replacementBlock);
                regionData.put("enable-lookup", enableLookup);
                regionData.put("protected-region", protectedRegion);

                ConfigurationSection blocksSection = regionSection.getConfigurationSection("blocks");
                if (blocksSection != null) {
                    Map<String, String> blockMap = new HashMap<>();
                    for (String block : blocksSection.getKeys(false)) {
                        blockMap.put(block, blocksSection.getString(block));
                    }
                    regionData.put("blocks", blockMap);
                }

                regionMap.put(key, regionData);
            }
        }
        return regionMap;
    }

    public void addRegion(String regionName) {
        Map<String, Object> defaultRegionData = new HashMap<>();
        defaultRegionData.put("replacement-block", "BEDROCK");
        defaultRegionData.put("enable-lookup", true);
        defaultRegionData.put("protected-region", true);

        Map<String, String> defaultBlocks = new HashMap<>();
        defaultBlocks.put("COAL_ORE", "10m");
        defaultBlocks.put("DEEPSLATE_COAL_ORE", "10m");
        defaultBlocks.put("IRON_ORE", "20m");
        defaultBlocks.put("DEEPSLATE_IRON_ORE", "20m");
        defaultBlocks.put("COPPER_ORE", "10m");
        defaultBlocks.put("DEEPSLATE_COPPER_ORE", "10m");
        defaultBlocks.put("GOLD_ORE", "30m");
        defaultBlocks.put("DEEPSLATE_GOLD_ORE", "30m");
        defaultBlocks.put("REDSTONE_ORE", "10m");
        defaultBlocks.put("DEEPSLATE_REDSTONE_ORE", "10m");
        defaultBlocks.put("EMERALD_ORE", "45m");
        defaultBlocks.put("DEEPSLATE_EMERALD_ORE", "45m");
        defaultBlocks.put("LAPIS_ORE", "10m");
        defaultBlocks.put("DEEPSLATE_LAPIS_ORE", "10m");
        defaultBlocks.put("DIAMOND_ORE", "1h");
        defaultBlocks.put("DEEPSLATE_DIAMOND_ORE", "1h");
        defaultBlocks.put("NETHER_GOLD_ORE", "30m");
        defaultBlocks.put("NETHER_QUARTZ_ORE", "20m");
        defaultBlocks.put("ANCIENT_DEBRIS", "2h");

        defaultRegionData.put("blocks", defaultBlocks);
        regions.put(regionName.toLowerCase(), defaultRegionData);
        save("Regions." + regionName.toLowerCase(), defaultRegionData);
    }

    public void removeRegion(String regionName) {
        if (regions.containsKey(regionName.toLowerCase())) {
            regions.remove(regionName.toLowerCase());
            config.set("Regions." + regionName.toLowerCase(), null);
            saveConfig();
        }
    }


    public Map<String, Map<String, Object>> regions = new MapBuilder<String, Map<String, Object>>()
            .put("example_mine", new MapBuilder<String, Object>()
                    .put("replacement-block", "BEDROCK")
                    .put("enable-lookup", true)
                    .put("protected-region", true)
                    .put("blocks", new MapBuilder<String, String>()
                            .put("COAL_ORE", "10m")
                            .put("DEEPSLATE_COAL_ORE", "10m")
                            .put("IRON_ORE", "20m")
                            .put("DEEPSLATE_IRON_ORE", "20m")
                            .put("COPPER_ORE", "10m")
                            .put("DEEPSLATE_COPPER_ORE", "10m")
                            .put("GOLD_ORE", "30m")
                            .put("DEEPSLATE_GOLD_ORE", "30m")
                            .put("REDSTONE_ORE", "10m")
                            .put("DEEPSLATE_REDSTONE_ORE", "10m")
                            .put("EMERALD_ORE", "45m")
                            .put("DEEPSLATE_EMERALD_ORE", "45m")
                            .put("LAPIS_ORE", "10m")
                            .put("DEEPSLATE_LAPIS_ORE", "10m")
                            .put("DIAMOND_ORE", "1h")
                            .put("DEEPSLATE_DIAMOND_ORE", "1h")
                            .put("NETHER_GOLD_ORE", "30m")
                            .put("NETHER_QUARTZ_ORE", "20m")
                            .put("ANCIENT_DEBRIS", "2h")
                            .build())
                    .build())
            .build();

    private void Regions() {
        regions = getRegionMap("Regions", regions);
    }

    public static RegionConfig RegCon() {
        return RespawnMine.getInstance().getRegionConfig();
    }
}

