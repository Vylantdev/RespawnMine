package be.gsnauw.respawnmine;

import be.gsnauw.respawnmine.commands.ResMineCommand;
import be.gsnauw.respawnmine.configs.MessageConfig;
import be.gsnauw.respawnmine.configs.RegionConfig;
import be.gsnauw.respawnmine.listeners.BlockListener;
import be.gsnauw.respawnmine.listeners.RightClickListener;
import be.gsnauw.respawnmine.utils.BrokenBlockData;
import be.gsnauw.respawnmine.utils.TimeConvert;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class RespawnMine extends JavaPlugin {


    @Getter
    private Map<Location, BrokenBlockData> brokenBlocks = new HashMap<>();
    @Getter
    private RegionConfig regionConfig;
    @Getter
    private MessageConfig messageConfig;
    @Getter
    private static RespawnMine instance;

    @Override
    public void onEnable() {
        instance = this;
        regionConfig = new RegionConfig("regions.yml", this);
        messageConfig = new MessageConfig("messages.yml", this);

        getLogger().info("The plugin has started, Hello World!");
        Objects.requireNonNull(this.getCommand("respawnmine")).setExecutor(new ResMineCommand());
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new RightClickListener(), this);

        initializeLoop();
    }

    @Override
    public void onDisable() {
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
}
