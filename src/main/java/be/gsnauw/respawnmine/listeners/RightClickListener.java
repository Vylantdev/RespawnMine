package be.gsnauw.respawnmine.listeners;

import be.gsnauw.respawnmine.RespawnMine;
import be.gsnauw.respawnmine.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

import static be.gsnauw.respawnmine.configs.MessageConfig.MesCon;

public class RightClickListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.OFF_HAND) return;
        Location clickedBlockLocation = Objects.requireNonNull(e.getClickedBlock()).getLocation();
        Block clickedBlock = e.getClickedBlock();
        Material clickedBlockMaterial = clickedBlock.getType();
        Player player = e.getPlayer();

        if (!Region.check(clickedBlockLocation)) return;
        RegionData regionData = Region.getRegionData(clickedBlockLocation);
        assert regionData != null;

        String replaceBlock = Material.AIR.toString();
        try {
            Material replacementMaterial = Material.valueOf(regionData.getReplacementBlock());
            replaceBlock = replacementMaterial.toString();
        } catch (IllegalArgumentException ignored) { }

        boolean enableLookup = regionData.isEnableLookup();

        if (!clickedBlockMaterial.equals(Material.valueOf(replaceBlock))) return;
        if (!enableLookup) return;

        if (!RespawnMine.getInstance().getBrokenBlocks().containsKey(clickedBlockLocation)) return;

        BrokenBlockData brokenBlocks = RespawnMine.getInstance().getBrokenBlocks().get(clickedBlockLocation);

        long now = System.currentTimeMillis();
        Long brokenAt = brokenBlocks.getBrokenAt();
        int interval = TimeConvert.convertTime(brokenBlocks.getBlockValue());
        long difference = (interval * 1000L - (now - brokenAt)) / 1000;

        player.sendMessage(Chat.formatprefix(MesCon().lookup.replace("%time%", TimeConvert.formatTime((int) difference))));
        e.setCancelled(true);
    }
}
