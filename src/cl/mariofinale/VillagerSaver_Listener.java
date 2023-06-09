package cl.mariofinale;

import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VillagerSaver_Listener implements Listener {

    /** @noinspection unused*/
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        //Check if dead entity is a villager
        if(!(event.getEntity() instanceof Villager)) return;
        Villager villagerEnt = (Villager) event.getEntity();
        
        //Check if death was caused by another entity
        if(!(villagerEnt.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        Entity damagerEnt = ((EntityDamageByEntityEvent) villagerEnt.getLastDamageCause()).getDamager();
 
        //Check if killer is a zombie variant
        if(!(VillagerSaver_PluginVars.ZombieTypes.contains(damagerEnt.getType()))) return;
        
        //Check if villager is in a blacklisted world
        if (VillagerSaver.WorldBlackList.contains(villagerEnt.getWorld().getName())) return;

        villagerEnt.zombify();
    }
}