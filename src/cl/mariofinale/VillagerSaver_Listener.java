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
        EntityDamageEvent damageCauseEvent = villagerEnt.getLastDamageCause();
        EntityDamageEvent.DamageCause cause = damageCauseEvent.getCause();

        //Check if death was caused by another entity or an explosion or a projectile
        if(!isValidDamageCause(cause)) return;

        //Get the Killer entity
        Entity killerEntity = getKillerEntity(damageCauseEvent, cause);

        //Check if killer is not null
        if (killerEntity == null) return;

        //Check if killer is a valid Entity
        if (!killerEntity.isValid()) return;

        //Check if killer is a zombie variant
        if(!isZombieVariant(killerEntity)) return;
        
        //Check if villager is in a blacklisted world
        if (isVillagerWorldBlacklisted(villagerEnt)) return;

        //Zombify Villager!
        villagerEnt.zombify();
    }


    private Entity getKillerEntity(EntityDamageEvent damageCauseEvent, EntityDamageEvent.DamageCause cause) {
        switch (cause) {
            case PROJECTILE:  //Set damager as the entity that shot the projectile
                return  (Entity) ((Projectile)((EntityDamageByEntityEvent) damageCauseEvent).getDamager()).getShooter();
            case ENTITY_ATTACK: //Set damager as the entity attacking
                return ((EntityDamageByEntityEvent) damageCauseEvent).getDamager();
            case ENTITY_EXPLOSION: //Set damager as the entity that primed the TNT
                return ((TNTPrimed) ((EntityDamageByEntityEvent) damageCauseEvent).getDamager()).getSource();
        }
        return null;
    }

    private boolean isValidDamageCause(EntityDamageEvent.DamageCause cause) {
        return cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
                cause == EntityDamageEvent.DamageCause.PROJECTILE ||
                cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
    }

    private boolean isZombieVariant(Entity entity) {
        return VillagerSaver_PluginVars.ZombieTypes.contains(entity.getType());
    }

    private boolean isVillagerWorldBlacklisted(Villager villagerEnt) {
        return VillagerSaver.WorldBlackList.contains(villagerEnt.getWorld().getName());
    }
}