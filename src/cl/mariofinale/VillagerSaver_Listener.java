package cl.mariofinale;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Class that implements the Listener interface to handle entity death events.
 */
public class VillagerSaver_Listener implements Listener {

    /**
     * Event handler for the EntityDeathEvent.
     *
     * @param event The EntityDeathEvent.
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Check if the dead entity is a villager
        if (!(event.getEntity() instanceof Villager)) return;

        Villager villagerEnt = (Villager) event.getEntity();
        EntityDamageEvent damageCauseEvent = villagerEnt.getLastDamageCause();
        EntityDamageEvent.DamageCause cause = damageCauseEvent.getCause();

        // Check if the death was caused by another entity or an explosion or a projectile
        if (!isValidDamageCause(cause)) return;

        // Get the killer entity
        Entity killerEntity = getKillerEntity(damageCauseEvent, cause);

        // Check if the killer is not null
        if (killerEntity == null) return;

        // Check if the killer is a valid entity
        if (!killerEntity.isValid()) return;

        // Check if the killer is a zombie variant
        if (!isZombieVariant(killerEntity)) return;

        // Check if the villager is in a blacklisted world
        if (isVillagerWorldBlacklisted(villagerEnt)) return;

        // Zombify the villager
        villagerEnt.zombify();
    }


    /**
     * Gets the killer entity from the damage cause event.
     *
     * @param damageCauseEvent The damage cause event.
     * @param cause            The damage cause.
     * @return The killer entity, or null if not found.
     */
    private Entity getKillerEntity(EntityDamageEvent damageCauseEvent, EntityDamageEvent.DamageCause cause) {
        switch (cause) {
            case PROJECTILE:
                // Set the damager as the entity that shot the projectile
                return (Entity) ((Projectile) ((EntityDamageByEntityEvent) damageCauseEvent).getDamager()).getShooter();
            case ENTITY_ATTACK:
                // Set the damager as the entity attacking
                return ((EntityDamageByEntityEvent) damageCauseEvent).getDamager();
            case ENTITY_EXPLOSION:
                // Set the damager as the entity that primed the TNT
                return ((TNTPrimed) ((EntityDamageByEntityEvent) damageCauseEvent).getDamager()).getSource();
            default:
                return null;
        }
    }

    /**
     * Checks if the given damage cause is valid for villager death.
     *
     * @param cause The damage cause.
     * @return true if the cause is valid, false otherwise.
     */
    private boolean isValidDamageCause(EntityDamageEvent.DamageCause cause) {
        return cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
                cause == EntityDamageEvent.DamageCause.PROJECTILE ||
                cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
    }

    /**
     * Checks if the given entity is a zombie variant.
     *
     * @param entity The entity to check.
     * @return true if the entity is a zombie variant, false otherwise.
     */
    private boolean isZombieVariant(Entity entity) {
        return VillagerSaver_PluginVars.ZombieTypes.contains(entity.getType());
    }

    /**
     * Checks if the given villager is in a blacklisted world.
     *
     * @param villager The villager entity.
     * @return true if the villager is in a blacklisted world, false otherwise.
     */
    private boolean isVillagerWorldBlacklisted(Villager villager) {
        return VillagerSaver.WorldBlackList.contains(villager.getWorld().getName());
    }
}