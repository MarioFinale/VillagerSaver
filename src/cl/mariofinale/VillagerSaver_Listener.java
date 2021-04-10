package cl.mariofinale;
import net.minecraft.server.v1_16_R3.*;
import net.minecraft.server.v1_16_R3.EntityVillager;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftVillagerZombie;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VillagerSaver_Listener implements Listener{

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        Entity villager = event.getEntity();
        if(!(villager instanceof LivingEntity)) return;
        LivingEntity tVillager = (LivingEntity)villager;
        if (event.getDamager() == null) return;
        Entity tKiller = event.getDamager();
        if (!(tVillager.getHealth() - event.getDamage() <= 0)) return;
        if (!(tVillager.getType() == EntityType.VILLAGER)) return;
        if (!((tKiller.getType() == EntityType.ZOMBIE) || (tKiller.getType() == EntityType.ZOMBIE_VILLAGER)  || (tKiller.getType() == EntityType.DROWNED) || (tKiller.getType() == EntityType.HUSK) || (tKiller.getType() == EntityType.ZOMBIFIED_PIGLIN))) return;
        if (villagerSaver.WorldBlackList.contains(tVillager.getWorld().getName())) return;
        handleSpawnZombieVillager(tVillager);
        event.setCancelled(true);
    }

    public void handleSpawnZombieVillager(LivingEntity entityliving) {
        CraftVillager craftVillager = (CraftVillager) entityliving;
        Entity vehicle = craftVillager.getVehicle();

        EntityVillager entityvillager = craftVillager.getHandle();
        VillagerData villagerData = entityvillager.getVillagerData();
        Reputation reputation = entityvillager.fj();
        int experience = entityvillager.getExperience();
        MerchantRecipeList offers = entityvillager.getOffers();

        // Transform Villager into Zombie Villager
        EntityZombieVillager entityZombieVillager = entityvillager.a(
                EntityTypes.ZOMBIE_VILLAGER,
                true,
                TransformReason.INFECTION,
                SpawnReason.INFECTION
        );
        if (entityZombieVillager == null) {
            return;
        }
        CraftVillagerZombie craftVillagerZombie = (CraftVillagerZombie) entityZombieVillager.getBukkitEntity();

        if (vehicle != null && !vehicle.getPassengers().contains(craftVillagerZombie)) {
            vehicle.addPassenger(craftVillagerZombie);
        }

        entityZombieVillager.setVillagerData(villagerData);
        // retain entityZombieVillager reputation with BaseNBT from the previous villager
        // DynamicOpsNBT.a is necessary because Gossips don't follow a specific Schema
        entityZombieVillager.a(reputation.a(DynamicOpsNBT.a).getValue());
        entityZombieVillager.a(experience);
        entityZombieVillager.setOffers(offers.a());
    }
}