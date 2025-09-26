package cl.mariofinale;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Implements the Listener interface to handle villager death events in the VillagerSaver plugin.
 * When a villager is killed by a zombie variant in a non-blacklisted world, it is zombified instead of dying.
 */
public class VillagerSaver_Listener implements Listener {
    private final VillagerSaver plugin;

    /**
     * Constructs a new listener with a reference to the main plugin instance.
     *
     * @param plugin The VillagerSaver plugin instance, used to access the world blacklist and logging
     */
    public VillagerSaver_Listener(VillagerSaver plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the EntityDeathEvent to detect villager deaths and zombify them under specific conditions.
     * A villager is zombified if it is killed by a zombie variant (e.g., zombie, husk, drowned) via entity attack,
     * projectile, or explosion (caused by one the zombie variants), and the villager's world is not blacklisted.
     * Logs an error if zombification fails.
     *
     * @param event The EntityDeathEvent triggered when an entity dies
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Villager villager)) return;

        EntityDamageEvent damageCauseEvent = villager.getLastDamageCause();
        if (damageCauseEvent == null) return;
        EntityDamageEvent.DamageCause cause = damageCauseEvent.getCause();

        if (!isValidDamageCause(cause)) return;

        Entity killerEntity = getKillerEntity(damageCauseEvent, cause);
        if (killerEntity == null || !killerEntity.isValid() || !isZombieVariant(killerEntity)) return;

        if (isVillagerWorldBlacklisted(villager)) return;
        Location loc = villager.getLocation();
        String worldName = villager.getWorld().getName();
        String coordinates = String.format("at %d %d %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        try {
            villager.zombify();
            plugin.logInfo("Villager zombified in world " + worldName + " " + coordinates);
        } catch (IllegalStateException e) {
            plugin.logError("Failed to zombify villager in world " + worldName + " " + coordinates + ": " + e.getMessage());
        }
    }

    /**
     * Retrieves the entity responsible for killing the villager based on the damage event.
     * Supports projectiles (returns the shooter), direct entity attacks (returns the attacker), and explosions
     * (returns the TNT prime source or Creeper's target, if applicable).
     *
     * @param damageCauseEvent The event containing details of the damage that caused the death
     * @param cause            The specific cause of the damage (e.g., PROJECTILE, ENTITY_ATTACK)
     * @return The entity responsible for the damage, or null if not applicable or undetermined
     */
    private Entity getKillerEntity(EntityDamageEvent damageCauseEvent, EntityDamageEvent.DamageCause cause) {
        if (!(damageCauseEvent instanceof EntityDamageByEntityEvent entityEvent)) return null;
        Entity damagerEntity = entityEvent.getDamager();

        switch (cause) {
            case PROJECTILE:
                if (damagerEntity instanceof Projectile projectile) {
                    ProjectileSource shooter = projectile.getShooter();
                    return shooter instanceof Entity ? (Entity) shooter : null;
                }
                return null;
            case ENTITY_ATTACK:
                return damagerEntity;
            case ENTITY_EXPLOSION:
                if (damagerEntity instanceof TNTPrimed tnt) {
                    return tnt.getSource();
                }
                if (damagerEntity instanceof Creeper creeper) {
                    Entity target = creeper.getTarget();
                    return (target != null && target.isValid()) ? target : null;
                }
                return null;
            default:
                return null;
        }
    }

    /**
     * Determines if the damage cause is one that triggers villager zombification.
     * Valid causes are direct entity attacks, projectiles, and entity-induced explosions.
     *
     * @param cause The damage cause to check
     * @return true if the cause is ENTITY_ATTACK, PROJECTILE, or ENTITY_EXPLOSION, false otherwise
     */
    private boolean isValidDamageCause(EntityDamageEvent.DamageCause cause) {
        return cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
                cause == EntityDamageEvent.DamageCause.PROJECTILE ||
                cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
    }

    /**
     * Checks if the entity is a zombie variant as defined in VillagerSaver_PluginVars.ZombieTypes.
     * Players are explicitly excluded, even if they match a zombie-like entity type.
     *
     * @param entity The entity to check
     * @return true if the entity is a zombie variant (e.g., zombie, husk, drowned) and not a player, false otherwise
     */
    private boolean isZombieVariant(Entity entity) {
        if (entity instanceof Player) return false;
        return VillagerSaver_PluginVars.ZombieTypes.contains(entity.getType());
    }

    /**
     * Checks if the villager’s world is in the plugin’s blacklist.
     * Blacklisted worlds prevent villager zombification.
     *
     * @param villager The villager entity to check
     * @return true if the villager’s world is blacklisted, false otherwise
     */
    private boolean isVillagerWorldBlacklisted(Villager villager) {
        return plugin.getWorldBlackList().contains(villager.getWorld().getName());
    }
}