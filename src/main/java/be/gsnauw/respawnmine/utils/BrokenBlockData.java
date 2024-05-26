package be.gsnauw.respawnmine.utils;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public class BrokenBlockData {

    private final Long brokenAt;
    private final Material blockType;
    private final String blockValue;

    public BrokenBlockData(Material blockType, String value) {
        this.blockType = blockType;
        this.brokenAt = System.currentTimeMillis();
        this.blockValue = value;
    }
}
