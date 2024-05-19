package be.gsnauw.respawnmine.listeners;

import be.gsnauw.respawnmine.utils.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class BlockListener implements Listener {


    @EventHandler
    public void BlockBreak(BlockBreakEvent e) {

        Player player = e.getPlayer();
        Material BrokenBlock = e.getBlock().getType();

        player.sendMessage( "Default: " + BrokenBlock);


        Location BlockLocation = e.getBlock().getLocation();

        if (Region.check(BlockLocation)) { //regio komt voor

            player.sendMessage("juiste region");

            Map<String, String> Blocks = Region.getRegionBlocks(e.getPlayer()); //haal de juiste map op die bij de regio hoort
            assert Blocks != null;
            if (Blocks.containsKey(BrokenBlock.toString())) {

                String BlockValue = Blocks.get(BrokenBlock.toString()); //haal de waarde van de block op

                player.sendMessage("Juist Block: " + BlockValue + " " + BrokenBlock);
            } else {
                player.sendMessage("Fout Block: " + BrokenBlock);
            }
        } else {
            player.sendMessage("foute region");
        }


//Dan timer starten voor dat block en dan replacen als timer gedana is.

    }
}
