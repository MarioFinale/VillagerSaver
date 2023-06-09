package cl.mariofinale;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class VillagerSaver_PluginVars {
    public static final String PluginPrefix = ChatColor.BOLD + "" + ChatColor.GREEN + "[Villager Saver]:" + ChatColor.RESET;
    public static final ArrayList ZombieTypes = new ArrayList<EntityType>(){
        {
            add(EntityType.ZOMBIE);
            add(EntityType.ZOMBIE_VILLAGER);
            add(EntityType.DROWNED);
            add(EntityType.HUSK);
            add(EntityType.ZOMBIFIED_PIGLIN);
        }
    };
}
