package be.gsnauw.respawnmine.configs;

import be.gsnauw.respawnmine.RespawnMine;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class MessageConfig {

    private final File configFile;
    private final YamlConfiguration config;

    public MessageConfig(String filename, Plugin instance) {
        this.configFile = new File(instance.getDataFolder(), filename);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        init();
    }

    private void init() {
        List<String> header = new ArrayList<>();
        header.add("--Message Config--");
        header.add("******************");


        config.options().setHeader(header);
        config.options().copyDefaults(true);

        readConfig();
    }

    public void reload() {
        try {
            this.config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("Could not reload " + configFile, e);
        }

        readConfig();
    }

    private void readConfig() {
        DefaultMessages();
        CommandMessages();
        saveConfig();
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
        }
    }

    private String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, def);
    }

    public String prefix = "&7[&a&lRespawn&f&lMine&7] &8-> ";
    public String noPermission = "&cYou're not allowed to perform this action.";
    public String unknownCommand = "&cUnknown command, use: %commands%.";
    public String consoleCommand = "&cOnly console can execute this command.";
    public String playerCommand = "Only players can execute this command.";

    private void DefaultMessages() {
        prefix = getString("Prefix", prefix);
        noPermission = getString("NoPermission", noPermission);
        unknownCommand = getString("UnknownCommand", unknownCommand);
    }

    public String help = "&fUse: &a%commands%&f.";
    public String reload = "&fThe plugin has been reloaded.";
    public String alreadyExists = "&cThere already exists a region with this name.";
    public String added = "&fThe region &a%region% &fhas been added. You can change the options for this region in the config.";
    public String doesNotExist = "&cThere was no region found with this name.";
    public String removed = "&fThe region &a%region% &fhas been removed from the config.";
    public String lookup = "&fThis block respawns in &a%time%&f.";
    public String list = "&fRegistered regions: &a%regions%&f.";
    public String listNone = "&cNo regions found.";

    private void CommandMessages() {
        reload = getString("Reload", reload);
        help = getString("Help", help);
        alreadyExists = getString("RegionAlreadyExists", alreadyExists);
        added = getString("RegionAdded", added);
        doesNotExist = getString("RegionDoesNotExist", doesNotExist);
        removed = getString("RegionRemoved", removed);
        lookup = getString("BlockLookup", lookup);
        list = getString("List", list);
        listNone = getString("ListEmpty", listNone);
    }

    public static MessageConfig MesCon() {
        return RespawnMine.getInstance().getMessageConfig();
    }
}
