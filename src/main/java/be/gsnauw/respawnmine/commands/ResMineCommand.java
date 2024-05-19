package be.gsnauw.respawnmine.commands;

import be.gsnauw.respawnmine.RespawnMine;
import be.gsnauw.respawnmine.utils.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static be.gsnauw.respawnmine.configs.DefaultConfig.DefCon;


public class ResMineCommand implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("NEIN");
            return true;
        }

        if (args.length == 0) {
            RespawnMine.getInstance().getDefaultConfig().reload();
            player.sendMessage("hey");
        }
        return true;
    }
}
