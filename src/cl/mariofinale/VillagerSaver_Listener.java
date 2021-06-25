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
        Entity damagedEntity = event.getEntity();
        if(!(damagedEntity instanceof LivingEntity)) return;
        LivingEntity damagedVillager = (LivingEntity) damagedEntity;
        Entity entityDamager = event.getDamager();
        if (entityDamager == null) return;
        if (!(damagedVillager.getHealth() - event.getDamage() <= 0)) return;
        if (!(damagedVillager.getType() == EntityType.VILLAGER)) return;
        if (!(VillagerSaver_PluginVars.ZombieTypes.contains(entityDamager.getType()))) return; //Check if the zombie types list contains the damager
        if (villagerSaver.WorldBlackList.contains(damagedVillager.getWorld().getName())) return;
        handleSpawnZombieVillager(damagedVillager);
        event.setCancelled(true);
    }

    public void handleSpawnZombieVillager(LivingEntity livingEnt) {
        CraftVillager craftVillager = (CraftVillager) livingEnt;
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
        if (entityZombieVillager == null) return;
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