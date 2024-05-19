package be.gsnauw.respawnmine.utils;

import be.gsnauw.respawnmine.RespawnMine;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Map;

import static be.gsnauw.respawnmine.configs.DefaultConfig.DefCon;


public class Region {

    public static boolean check(Location location) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        ApplicableRegionSet playerRegions = regionQuery.getApplicableRegions(BukkitAdapter.adapt(location));

        return playerRegions.getRegions().stream()
                .anyMatch(r -> DefCon().regions.containsKey(r.getId().toLowerCase()));
    }


    @Nullable
    public static Map<String, String> getRegionBlocks(Player player) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        ApplicableRegionSet playerRegions = regionQuery.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        for (ProtectedRegion region : playerRegions.getRegions()) {

            if (DefCon().regions.get(region.getId()) != null) {
                return  DefCon().regions.get(region.getId());
            }

        }
        return null;
    }
}

