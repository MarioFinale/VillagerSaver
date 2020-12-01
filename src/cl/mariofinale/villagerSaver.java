package cl.mariofinale;
import com.destroystokyo.paper.entity.villager.Reputation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class villagerSaver extends JavaPlugin{

    public static HashMap<UUID, Integer> VillagersLevels = new HashMap();
    public static HashMap<UUID, List<MerchantRecipe>> VillagersTrades = new HashMap();
    public static HashMap<UUID, Integer> VillagersExp = new HashMap();
    public static HashMap<UUID, Villager.Profession> VillagersProfessions = new HashMap();
    public static HashMap<UUID, Villager.Type> VillagersTypes = new HashMap();
    public static HashMap<UUID, Map<UUID, Reputation>> VillagersReputation = new HashMap();
    public static HashMap<UUID, UUID> VillagersHealers = new HashMap();
    public static ArrayList<String> WorldBlackList = new ArrayList<>();
    public static Plugin plugin;

    @Override
    public void onEnable(){
        plugin = this;
        PluginPrintln("Registering listener.");
        getServer().getPluginManager().registerEvents(new VillagerSaver_Listener(), this);
        PluginPrintln("Listener registered.");
        PluginPrintln("Loading World Blacklist.");
        LoadWorldBlackList();
        PluginPrintln("Registering commands.");
        VillagerSaver_Commands villagerSaver_commands = new VillagerSaver_Commands();
        this.getCommand("villagersaver").setExecutor(villagerSaver_commands);
        PluginPrintln("Commands registered.");
        PluginPrintln("Plugin loaded!.");
    }
    @Override
    public void onDisable(){
        plugin.saveDefaultConfig();
        SaveWorldBlackList();
    }

    public void PluginPrintln(String line){
        String name = getDescription().getName();
        System.out.println("[" + name + "] " + line );
    }

    public void SaveWorldBlackList() {
        PluginPrintln("Saving blacklist.");
        File WorldBlackListFile = new File(getDataFolder().getAbsolutePath(), "WorldBlackList.yml");
        try {
            WorldBlackListFile.delete();
            WorldBlackListFile.createNewFile();

            if (WorldBlackList == null){
                WorldBlackList = new ArrayList<String>();
            }
        } catch (Exception ex) {
            PluginPrintln("Error saving blacklist: " + ex.getMessage());
            return;
        }
        FileConfiguration playerPointsConfig = YamlConfiguration.loadConfiguration(WorldBlackListFile);;
        playerPointsConfig.set("BlackList", WorldBlackList);

        try {
            playerPointsConfig.save(WorldBlackListFile);
        } catch (Exception ex) {
            PluginPrintln("Error saving blacklist: " + ex.getMessage());
            return;
        }
        PluginPrintln("Blacklist saved.");
    }

    public void LoadWorldBlackList(){
        File WorldBlackListFile = new File(getDataFolder().getAbsolutePath(), "WorldBlackList.yml");
        try {
            WorldBlackListFile.createNewFile();
            FileConfiguration blacklistConfigFile = YamlConfiguration.loadConfiguration(WorldBlackListFile);
            WorldBlackList = (ArrayList<String>) blacklistConfigFile.get("BlackList");
        } catch (Exception ex) {
            PluginPrintln("Error loading blacklist: " + ex.getMessage());
            return;
        }
        if (WorldBlackList == null){
            WorldBlackList = new ArrayList<>();
        }
        PluginPrintln("World Blacklist loaded.");
    }
}
