package cl.mariofinale;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

/**
 * This class contains variables used by the VillagerSaver plugin.
 */
public class VillagerSaver_PluginVars {

    /**
     * The prefix used in plugin messages.
     */
    public static final String PluginPrefix = ChatColor.BOLD + "" + ChatColor.GREEN + "[Villager Saver]:" + ChatColor.RESET;

    /**
     * The list of zombie entity types.
     * These are the entity types that are considered zombie variants.
     */
    public static final ArrayList ZombieTypes = new ArrayList<EntityType>() {
        {
            add(EntityType.ZOMBIE);
            add(EntityType.ZOMBIE_VILLAGER);
            add(EntityType.DROWNED);
            add(EntityType.HUSK);
            add(EntityType.ZOMBIFIED_PIGLIN);
        }
    };
}
