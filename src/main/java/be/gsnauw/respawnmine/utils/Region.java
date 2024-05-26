package be.gsnauw.respawnmine.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.Map;

import static be.gsnauw.respawnmine.configs.RegionConfig.RegCon;


public class Region {

    public static boolean check(Location location) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        ApplicableRegionSet playerRegions = regionQuery.getApplicableRegions(BukkitAdapter.adapt(location));

        return playerRegions.getRegions().stream()
                .anyMatch(r -> RegCon().regions.containsKey(r.getId().toLowerCase()));
    }


    @Nullable
    public static RegionData getRegionData(Location location) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        ApplicableRegionSet playerRegions = regionQuery.getApplicableRegions(BukkitAdapter.adapt(location));
        for (ProtectedRegion region : playerRegions.getRegions()) {

            Map<String, Object> regionData = RegCon().regions.get(region.getId());

            if (regionData != null) {
                String replacementBlock = (String) regionData.get("replacement-block");
                boolean enableLookup = (Boolean) regionData.get("enable-lookup");
                boolean protectedRegion = (Boolean) regionData.get("protected-region");
                @SuppressWarnings("unchecked")
                Map<String, String> blocks = (Map<String, String>) regionData.get("blocks");

                return new RegionData(replacementBlock, enableLookup, protectedRegion , blocks);
            }
        }
        return null;
    }
}

