package be.gsnauw.respawnmine.utils;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

import static be.gsnauw.respawnmine.configs.StoredBlocksConfig.StoredCon;

public class StoreBlock {
    public static void store(Material blockType, Location blockLoc) {
        Map<String, Object> blockData = new HashMap<>();

        Map<String, Object> locationData = new HashMap<>();
        locationData.put("X", blockLoc.getBlockX());
        locationData.put("Y", blockLoc.getBlockY());
        locationData.put("Z", blockLoc.getBlockZ());
        locationData.put("World", blockLoc.getWorld().getName());

        blockData.put("Location", locationData);
        blockData.put("Material", blockType.toString());

        StoredCon().storedBlocks.add(blockData);
        StoredCon().save("StoredBlocks", StoredCon().storedBlocks);
    }
}
