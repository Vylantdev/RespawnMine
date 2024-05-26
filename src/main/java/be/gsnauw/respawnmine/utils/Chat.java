package be.gsnauw.respawnmine.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

import static be.gsnauw.respawnmine.configs.MessageConfig.MesCon;

public class Chat {
    public static Component format(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text)
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, false)
                .decoration(TextDecoration.UNDERLINED, false);
    }

    public static Component formatprefix(String text) {
        String prefixedText = MesCon().prefix + text;
        return format(prefixedText);
    }

    public static Component noPermission() {
        return formatprefix(MesCon().noPermission);
    }

    public static void playerCommand() {
        sendConsolePrefixInfo(MesCon().playerCommand);
    }

    public static Component consoleCommand() {
        return formatprefix(MesCon().consoleCommand);
    }

    static String ConsolePrefix = "[RespawnMine] ";
    public static void sendConsolePrefixInfo(String text) {
        Bukkit.getServer().getLogger().info(ConsolePrefix + text);
    }
    public static void sendConsolePrefixWarn(String text) {
        Bukkit.getServer().getLogger().warning(ConsolePrefix + text);
    }
    public static void sendConsolePrefixError(String text) {
        Bukkit.getServer().getLogger().severe(ConsolePrefix + text);
    }
}
