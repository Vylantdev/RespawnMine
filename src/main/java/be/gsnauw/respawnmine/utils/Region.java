package be.gsnauw.respawnmine.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

import static be.gsnauw.respawnmine.configs.DefaultConfig.DefCon;


public class Region {
   public static boolean check(Player player) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        ApplicableRegionSet playerRegions = regionQuery.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));

        return playerRegions.getRegions().stream()
                .anyMatch(r -> DefCon().regions.containsKey(r.getId()));
    }

    public Map<String, String> getRegionBlocks(String region) {
        //Checken of de region in config staat (null voorkomen), dan de soorten blokken + hun value terug sturen in map of array vorm
        return null;
    }
}

