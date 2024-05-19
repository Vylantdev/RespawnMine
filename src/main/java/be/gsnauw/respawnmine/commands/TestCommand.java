package be.gsnauw.respawnmine.commands;

import be.gsnauw.respawnmine.RespawnMine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        //Allemaal test dingen

        sender.sendMessage(RespawnMine.getInstance().getDefaultConfig().hoi);

        List<String> list = RespawnMine.getInstance().getDefaultConfig().test;
        if (list != null) {
            for (String l : list) {
                sender.sendMessage(l);
            }
            list.add("heyyy");
        }

        RespawnMine.getInstance().getDefaultConfig().save("lijst.test", list);
        RespawnMine.getInstance().getDefaultConfig().save("wow", "test");
        RespawnMine.getInstance().getDefaultConfig().reload();
        return true;
    }
}
