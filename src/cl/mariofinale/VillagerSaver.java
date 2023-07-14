package cl.mariofinale;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * The main class of the VillagerSaver plugin.
 */
public class VillagerSaver extends JavaPlugin {
    static ArrayList<String> WorldBlackList = new ArrayList<>();
    private static Plugin plugin;

    /**
     * Called when the plugin is enabled.
     * Initializes the plugin by registering the listener, loading the world blacklist, and registering commands.
     * Logs relevant messages to indicate the progress.
     */
    @Override
    public void onEnable() {
        plugin = this;
        LogWarn("Make sure you downloaded a trusted version of this plugin on: https://github.com/MarioFinale/VillagerSaver/releases");
        LogInfo("Registering listener.");
        getServer().getPluginManager().registerEvents(new VillagerSaver_Listener(), this);
        LogInfo("Listener registered.");
        LogInfo("Loading World Blacklist.");
        LoadWorldBlackList();
        LogInfo("Registering commands.");
        VillagerSaver_Commands villagerSaver_commands = new VillagerSaver_Commands();
        this.getCommand("villagersaver").setExecutor(villagerSaver_commands);
        LogInfo("Commands registered.");
        LogInfo("VillagerSaver loaded!");
    }

    /**
     * Called when the plugin is disabled.
     * Saves the configuration and world blacklist.
     * Logs a message indicating that the plugin has been disabled.
     */
    @Override
    public void onDisable() {
        LogInfo("Saving config.");
        plugin.saveDefaultConfig();
        SaveWorldBlackList();
        LogInfo("VillagerSaver disabled!");
    }

    /**
     * Logs an informational message.
     *
     * @param line The message to log
     */
    private void LogInfo(String line) {
        plugin.getLogger().log(Level.INFO, line);
    }

    /**
     * Logs a warning message.
     *
     * @param line The message to log
     */
    private void LogWarn(String line) {
        plugin.getLogger().log(Level.WARNING, line);
    }

    /**
     * Logs an error message.
     *
     * @param line The message to log
     */
    private void LogError(String line) {
        plugin.getLogger().log(Level.SEVERE, line);
    }

    /**
     * Saves the world blacklist to the configuration file.
     */
    private void SaveWorldBlackList() {
        LogInfo("Saving blacklist.");
        File WorldBlackListFile = new File(getDataFolder().getAbsolutePath(), "WorldBlackList.yml");
        try {
            WorldBlackListFile.delete();
            WorldBlackListFile.createNewFile();
            if (WorldBlackList == null) {
                WorldBlackList = new ArrayList<>();
            }
        } catch (Exception ex) {
            LogError("Error saving blacklist: " + ex.getMessage());
            return;
        }
        FileConfiguration playerPointsConfig = YamlConfiguration.loadConfiguration(WorldBlackListFile);
        playerPointsConfig.set("BlackList", WorldBlackList);

        try {
            playerPointsConfig.save(WorldBlackListFile);
        } catch (Exception ex) {
            LogError("Error saving blacklist: " + ex.getMessage());
            return;
        }
        LogInfo("Blacklist saved.");
    }

    /**
     * Loads the world blacklist from the configuration file.
     */
    private void LoadWorldBlackList() {
        File WorldBlackListFile = new File(getDataFolder().getAbsolutePath(), "WorldBlackList.yml");
        try {
            WorldBlackListFile.createNewFile();
            FileConfiguration blacklistConfigFile = YamlConfiguration.loadConfiguration(WorldBlackListFile);
            WorldBlackList = (ArrayList) blacklistConfigFile.get("BlackList");
        } catch (Exception ex) {
            LogWarn("World blacklist file has not been loaded: " + ex.getMessage());
            LogWarn("If this is the first time running the plugin the file will be created on server stop.");
            return;
        }
        if (WorldBlackList == null) {
            WorldBlackList = new ArrayList<>();
        }
        LogInfo("World Blacklist loaded.");
    }
}
