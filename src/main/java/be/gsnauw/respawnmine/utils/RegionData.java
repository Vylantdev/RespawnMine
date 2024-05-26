package be.gsnauw.respawnmine.utils;

import lombok.Getter;

import java.util.Map;

@Getter
public class RegionData {

    private final String replacementBlock;
    private final boolean enableLookup;
    private final boolean protectedRegion;
    private final Map<String, String> blocks;

    public RegionData(String replacementBlock, boolean enableLookup, boolean protectedRegion, Map<String, String> blocks) {
        this.replacementBlock = replacementBlock;
        this.enableLookup = enableLookup;
        this.protectedRegion = protectedRegion;
        this.blocks = blocks;
    }
}
