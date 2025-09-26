package cl.mariofinale;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * The main class of the VillagerSaver plugin.
 */
public class VillagerSaver extends JavaPlugin {
    private ArrayList<String> worldBlackList = new ArrayList<>();
    private final Plugin plugin = this;

    /**
     * Called when the plugin is enabled.
     * Initializes the plugin by registering the listener, loading the world blacklist, and registering commands.
     * Logs relevant messages to indicate the progress.
     */
    @Override
    public void onEnable() {
        logWarn("Make sure you downloaded a trusted version of this plugin on: https://github.com/MarioFinale/VillagerSaver/releases");
        logInfo("Registering listener.");
        getServer().getPluginManager().registerEvents(new VillagerSaver_Listener(this), this);
        logInfo("Listener registered.");
        logInfo("Loading World Blacklist.");
        loadWorldBlackList();
        logInfo("Registering commands.");
        VillagerSaver_Commands villagerSaver_commands = new VillagerSaver_Commands(this);
        PluginCommand command = getCommand("villagersaver");
        if (command != null) {
            command.setExecutor(villagerSaver_commands);
            logInfo("Commands registered.");
        } else {
            logInfo("Command 'villagersaver' not found in plugin.yml!");
        }
        logInfo("VillagerSaver loaded!");
    }

    /**
     * Called when the plugin is disabled.
     * Saves the configuration and world blacklist.
     * Logs a message indicating that the plugin has been disabled.
     */
    @Override
    public void onDisable() {
        logInfo("Saving config.");
        plugin.saveDefaultConfig();
        saveWorldBlackList();
        logInfo("VillagerSaver disabled!");
    }

    /**
     * Logs an informational message.
     *
     * @param line The message to log
     */
    public void logInfo(String line) {
        plugin.getLogger().log(Level.INFO, line);
    }

    /**
     * Logs a warning message.
     *
     * @param line The message to log
     */
    public void logWarn(String line) {
        plugin.getLogger().log(Level.WARNING, line);
    }

    /**
     * Logs an error message.
     *
     * @param line The message to log
     */
    public void logError(String line) {
        plugin.getLogger().log(Level.SEVERE, line);
    }

    /**
     * Saves the world blacklist to the configuration file.
     */
    private void saveWorldBlackList() {
        logInfo("Saving blacklist.");
        File worldBlackListFile = new File(getDataFolder(), "WorldBlackList.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(worldBlackListFile);
        config.set("BlackList", getWorldBlackList());
        try {
            config.save(worldBlackListFile);
            logInfo("Blacklist saved.");
        } catch (IOException ex) {
            logError("Error saving blacklist: " + ex.getMessage());
        }
    }

    /**
     * Loads the world blacklist from the configuration file.
     */
    private void loadWorldBlackList() {
        File WorldBlackListFile = new File(getDataFolder().getAbsolutePath(), "WorldBlackList.yml");
        try {
            WorldBlackListFile.createNewFile();
            FileConfiguration blacklistConfigFile = YamlConfiguration.loadConfiguration(WorldBlackListFile);
            Object blackListObj = blacklistConfigFile.get("BlackList");
            if (blackListObj instanceof ArrayList<?>) {
                worldBlackList = new ArrayList<>(blacklistConfigFile.getStringList("BlackList"));
            } else {
                worldBlackList = new ArrayList<>();
            }
        } catch (Exception ex) {
            logWarn("World blacklist file has not been loaded: " + ex.getMessage());
            logWarn("If this is the first time running the plugin the file will be created on server stop.");
            worldBlackList = new ArrayList<>();
            return;
        }
        logInfo("World Blacklist loaded.");
    }

    public ArrayList<String> getWorldBlackList() {
        return worldBlackList;
    }

}
