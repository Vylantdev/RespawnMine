package be.gsnauw.respawnmine.listeners;

import be.gsnauw.respawnmine.RespawnMine;
import be.gsnauw.respawnmine.utils.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BlockListener implements Listener {

    @EventHandler
    public void BlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Material brokenBlock = e.getBlock().getType();
        Location blockLocation = e.getBlock().getLocation();
        Map<Location, BrokenBlockData> brokenBlocks = RespawnMine.getInstance().getBrokenBlocks();

        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (!Region.check(blockLocation)) return;

        if (brokenBlocks.containsKey(blockLocation)) {
            e.setCancelled(true);
            return;
        }

        RegionData regionData = Region.getRegionData(blockLocation);
        assert regionData != null;
        Map<String, String> blocks = regionData.getBlocks();

        if (!blocks.containsKey(brokenBlock.toString())) {
            if (regionData.isProtectedRegion()) {
                e.setCancelled(true);
            }
        }

        if (blocks.containsKey(brokenBlock.toString())) {
            ItemStack[] drops = e.getBlock().getDrops(player.getInventory().getItemInMainHand()).toArray(new ItemStack[0]);
            e.setDropItems(false);
            Map<Integer, ItemStack> remaining = player.getInventory().addItem(drops);
            for (ItemStack item : remaining.values()) {
                player.getWorld().dropItemNaturally(blockLocation, item);
            }

            String replaceBlock = regionData.getReplacementBlock();
            RespawnMine.getInstance().getServer().getScheduler().runTaskLater(RespawnMine.getInstance(), () -> {
                try {
                    Material replacementMaterial = Material.valueOf(replaceBlock);
                    blockLocation.getBlock().setType(replacementMaterial);
                } catch (IllegalArgumentException event) {
                    blockLocation.getBlock().setType(Material.AIR);
                }
            }, 1L);

            String blockValue = blocks.get(brokenBlock.toString());
            TimeConvert.convertTime(blockValue);

            brokenBlocks.put(blockLocation, new BrokenBlockData(brokenBlock, blockValue));
        }
    }
}
