package cl.mariofinale;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class villagerSaver extends JavaPlugin{

    public static HashMap<UUID, Integer> VillagersLevels = new HashMap();
    public static HashMap<UUID, List<MerchantRecipe>> VillagersTrades = new HashMap();
    public static HashMap<UUID, Integer> VillagersExp = new HashMap();
    public static HashMap<UUID, Villager.Profession> VillagersProfessions = new HashMap();
    public static HashMap<UUID, Villager.Type> VillagersTypes = new HashMap();
    public static Plugin plugin;

    @Override
    public void onEnable(){
        plugin = this;
        PluginPrintln("Registering listener.");
        getServer().getPluginManager().registerEvents(new VillagerSaver_Listener(), this);
        PluginPrintln("Listener registered.");
        PluginPrintln("Plugin loaded!.");
    }
    @Override
    public void onDisable(){
    //not necessary
    }

    public void PluginPrintln(String line){
        String name = getDescription().getName();
        System.out.println("[" + name + "] " + line );
    }
}
