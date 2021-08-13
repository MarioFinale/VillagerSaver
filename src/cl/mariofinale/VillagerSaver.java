package cl.mariofinale;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/** @noinspection unused*/
public class VillagerSaver extends JavaPlugin {
    static ArrayList<String> WorldBlackList = new ArrayList<>();
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        LogError("WARNING, WARNING. PLEASE READ ME:");
        LogError("IF YOU DOWNLOADED VILLAGERSAVER BETWEEN JULY 28 AND AUGUST 11 2021 YOU NEED TO DELETE IT IMMEDIATELY.");
        LogWarn("PLEASE MAKE SURE TO ALWAYS DOWNLOAD THE PLUGIN FROM THE GITHUB RELEASES PAGE: https://github.com/MarioFinale/VillagerSaver/releases");
        LogWarn("DO NOT DOWNLOAD THIS PLUGIN FROM ANY OTHER UNTRUSTED SOURCE!");
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

    @Override
    public void onDisable() {
        LogInfo("Saving config.");
        plugin.saveDefaultConfig();
        SaveWorldBlackList();
        LogInfo("VillagerSaver disabled!");
    }

    private void LogInfo(String line) {
        plugin.getLogger().log(Level.INFO,line);
    }
    private void LogWarn(String line) {
        plugin.getLogger().log(Level.WARNING,line);
    }
    private void LogError(String line) {
        plugin.getLogger().log(Level.SEVERE,line);
    }

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

    private void LoadWorldBlackList() {
        File WorldBlackListFile = new File(getDataFolder().getAbsolutePath(), "WorldBlackList.yml");
        try {
            WorldBlackListFile.createNewFile();
            FileConfiguration blacklistConfigFile = YamlConfiguration.loadConfiguration(WorldBlackListFile);
            WorldBlackList = (ArrayList<String>) blacklistConfigFile.get("BlackList");
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
