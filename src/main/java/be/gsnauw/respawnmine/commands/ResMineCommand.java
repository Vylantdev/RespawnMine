package be.gsnauw.respawnmine.commands;

import be.gsnauw.respawnmine.RespawnMine;
import be.gsnauw.respawnmine.utils.Chat;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static be.gsnauw.respawnmine.configs.MessageConfig.MesCon;
import static be.gsnauw.respawnmine.configs.RegionConfig.RegCon;


public class ResMineCommand implements CommandExecutor, TabCompleter {

    private final List<String> COMMANDS = Arrays.asList("add", "remove", "list", "reload", "help");
    String commands = "/rm <add | remove | list | reload | help> <name>";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player p)) {
            Chat.playerCommand();
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Chat.formatprefix(MesCon().help.replace("%commands%", commands)));
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "reload":
                    if (!p.hasPermission("respawnmine.reload")) {
                        p.sendMessage(Chat.noPermission());
                        break;
                    }
                    RespawnMine.getInstance().reload();
                    p.sendMessage(Chat.formatprefix(MesCon().reload));
                    break;
                case "list":
                    if (!p.hasPermission("respawnmine.list")) {
                        p.sendMessage(Chat.noPermission());
                        break;
                    }
                    Set<String> regionNames = RegCon().regions.keySet();
                    if (regionNames.isEmpty()) {
                        p.sendMessage(Chat.formatprefix(MesCon().listNone));
                    } else {
                        String joinedNames = String.join(" &f- &a", regionNames);
                        p.sendMessage(Chat.formatprefix(MesCon().list.replace("%regions%", joinedNames)));
                    }
                    break;
                case "help":
                    if (!p.hasPermission("respawnmine.use")) {
                        p.sendMessage(Chat.noPermission());
                        break;
                    }
                    p.sendMessage(Chat.formatprefix(MesCon().help.replace("%commands%", commands)));
                    break;
                default:
                    if (!p.hasPermission("respawnmine.use")) {
                        p.sendMessage(Chat.noPermission());
                        break;
                    }
                    p.sendMessage(Chat.formatprefix(MesCon().unknownCommand.replace("%commands%", commands)));
                    break;
            }
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "add":
                    if (!p.hasPermission("respawnmine.add")) {
                        p.sendMessage(Chat.noPermission());
                        break;
                    }
                    if (RegCon().regions.containsKey(args[1].toLowerCase())) {
                        p.sendMessage(Chat.formatprefix(MesCon().alreadyExists));
                        break;
                    }
                    RegCon().addRegion(args[1]);
                    RespawnMine.getInstance().getRegionConfig().reload();
                    p.sendMessage(Chat.formatprefix(MesCon().added.replace("%region%", args[1])));
                    break;

                case "remove":
                    if (!p.hasPermission("respawnmine.remove")) {
                        p.sendMessage(Chat.noPermission());
                        break;
                    }
                    if (!RegCon().regions.containsKey(args[1].toLowerCase())) {
                        p.sendMessage(Chat.formatprefix(MesCon().doesNotExist));
                        break;
                    }
                    RegCon().removeRegion(args[1]);
                    RespawnMine.getInstance().getRegionConfig().reload();
                    p.sendMessage(Chat.formatprefix(MesCon().removed.replace("%region%", args[1])));
                    break;
                default:
                    if (!p.hasPermission("respawnmine.use")) {
                        p.sendMessage(Chat.noPermission());
                        break;
                    }
                    p.sendMessage(Chat.formatprefix(MesCon().unknownCommand.replace("%commands%", commands)));
                    break;
            }
        }

        if (args.length > 2) {
                if (!p.hasPermission("respawnmine.use")) {
                    p.sendMessage(Chat.noPermission());
                    return true;
                }
                p.sendMessage(Chat.formatprefix(MesCon().unknownCommand.replace("%commands%", commands)));
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (String cmd : COMMANDS) {
                if (cmd.startsWith(args[0].toLowerCase())) {
                    suggestions.add(cmd);
                }
            }
            return suggestions;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {
                List<String> regionNames = new ArrayList<>(RegCon().regions.keySet());
                Collections.sort(regionNames);
                return regionNames;
            }
            if (args[0].equalsIgnoreCase("add")) {
                List<String> worldGuardRegions = getWorldGuardRegions();
                List<String> suggestions = new ArrayList<>();
                for (String region : worldGuardRegions) {
                    if (!RegCon().regions.containsKey(region.toLowerCase())) {
                        suggestions.add(region);
                    }
                }
                Collections.sort(suggestions);
                return suggestions;
            }
        }
        return Collections.emptyList();
    }

    private List<String> getWorldGuardRegions() {
        List<String> regionNames = new ArrayList<>();
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            if (regionManager != null) {
                Map<String, ProtectedRegion> regions = regionManager.getRegions();
                regionNames.addAll(regions.keySet());
            }
        }
        return regionNames;
    }
}
