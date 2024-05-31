package be.gsnauw.respawnmine;

import be.gsnauw.respawnmine.commands.ResMineCommand;
import be.gsnauw.respawnmine.configs.MessageConfig;
import be.gsnauw.respawnmine.configs.RegionConfig;
import be.gsnauw.respawnmine.configs.StoredBlocksConfig;
import be.gsnauw.respawnmine.listeners.BlockListener;
import be.gsnauw.respawnmine.listeners.RightClickListener;
import be.gsnauw.respawnmine.utils.BrokenBlockData;
import be.gsnauw.respawnmine.utils.TimeConvert;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class RespawnMine extends JavaPlugin {


    @Getter
    private Map<Location, BrokenBlockData> brokenBlocks = new HashMap<>();
    @Getter
    private RegionConfig regionConfig;
    @Getter
    private MessageConfig messageConfig;
    @Getter
    private StoredBlocksConfig storedBlocksConfig;
    @Getter
    private static RespawnMine instance;

    @Override
    public void onEnable() {
        instance = this;
        regionConfig = new RegionConfig("regions.yml", this);
        messageConfig = new MessageConfig("messages.yml", this);
        storedBlocksConfig = new StoredBlocksConfig("storedblocks.yml", this);

        getLogger().info("The plugin has started, Hello World!");
        Objects.requireNonNull(this.getCommand("respawnmine")).setExecutor(new ResMineCommand());
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new RightClickListener(), this);

        initializeLoop();
    }

    @Override
    public void onDisable() {

        if (!(brokenBlocks == null || brokenBlocks.isEmpty())) {
            for (Map.Entry<Location, BrokenBlockData> brokenBlock : brokenBlocks.entrySet()) {
                Material brokenMaterial = brokenBlock.getValue().getBlockType();
                brokenBlock.getKey().getBlock().setType(brokenMaterial);
            }
            getLogger().info("Stored blocks have been placed.");
        } else {
            getLogger().info("No stored blocks found, none placed.");
        }
        getLogger().info("The plugin has been disabled, goodbye!");
    }

    public void reload() {
        getRegionConfig().reload();
        getMessageConfig().reload();
    }

    public void initializeLoop() {
        getServer().getScheduler().runTaskTimer(this, () -> {

            ArrayList<Location> replacedBlocks = new ArrayList<>();

            for (Map.Entry<Location, BrokenBlockData> brokenBlock : brokenBlocks.entrySet()) {

                long now = System.currentTimeMillis();
                Long brokenAt = brokenBlock.getValue().getBrokenAt();
                Material brokenMaterial = brokenBlock.getValue().getBlockType();
                int interval = TimeConvert.convertTime(brokenBlock.getValue().getBlockValue());
                long difference = (interval * 1000L - (now - brokenAt)) / 1000;

                if (difference > 0) continue;
                brokenBlock.getKey().getBlock().setType(brokenMaterial);
                replacedBlocks.add(brokenBlock.getKey());
            }
            for (Location replacedBlock : replacedBlocks) {
                brokenBlocks.remove(replacedBlock);
            }
        }, 0, 20 );
    }

    public void storedBlocksCheck() {
        List<Map<String, Object>> storedBlocks = storedBlocksConfig.storedBlocks;

        if (storedBlocks == null || storedBlocks.isEmpty()) {
            return;
        }

        for (Map<String, Object> block : storedBlocks) {
            String materialName = (String) block.get("Material");
            @SuppressWarnings("unchecked")
            Map<String, Object> locationData = (Map<String, Object>) block.get("Location");
            int x = (int) locationData.get("X");
            int y = (int) locationData.get("Y");
            int z = (int) locationData.get("Z");
            String worldName = (String) locationData.get("World");

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                continue;
            }
            Location location = new Location(world, x, y, z);
            Material material = Material.valueOf(materialName);
            location.getBlock().setType(material);
        }

        storedBlocksConfig.storedBlocks.clear();
        storedBlocksConfig.save("StoredBlocks", storedBlocksConfig.storedBlocks);
    }
}
