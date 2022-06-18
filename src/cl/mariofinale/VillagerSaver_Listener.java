package cl.mariofinale;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityZombieVillager;
import net.minecraft.world.entity.npc.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_19_R1.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VillagerSaver_Listener implements Listener{

    /** @noinspection unused*/
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        Entity damagedEntity = event.getEntity();
        if(!(damagedEntity instanceof LivingEntity)) return; //Check if the damaged entity is a living entity
        LivingEntity damagedVillager = (LivingEntity) damagedEntity;
        Entity entityDamager = event.getDamager();
        if (!entityDamager.isValid()) return; //Check if the damage was issued by a valid entity
        if (!(damagedVillager.getHealth() - event.getDamage() <= 0)) return; //Check if the damage is enough to kill the entity
        if (!(damagedVillager.getType() == EntityType.VILLAGER)) return; //Check if the damaged entity is a Villager
        if (!(VillagerSaver_PluginVars.ZombieTypes.contains(entityDamager.getType()))) return; //Check if the zombie types list contains the damager
        if (VillagerSaver.WorldBlackList.contains(damagedVillager.getWorld().getName())) return; //Check if the villager is not in a blacklisted World
        handleSpawnZombieVillager(damagedVillager);
        event.setCancelled(true);
    }

    private void handleSpawnZombieVillager(LivingEntity livingEnt) {
        CraftVillager craftVillager = (CraftVillager) livingEnt;
        Entity vehicle = craftVillager.getVehicle();
        EntityVillager entityvillager = craftVillager.getHandle();
        VillagerData villagerData = entityvillager.fV();
        int experience = entityvillager.fK();;
        EntityVillagerAbstract villagerAbstract = entityvillager;
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        villagerAbstract.b(nbtTagCompound);
        Boolean isAdult = craftVillager.isAdult();

        // Transform Villager into Zombie Villager.
        EntityZombieVillager entityZombieVillager = villagerAbstract.convertTo(
                EntityTypes.bl,
                true,
                TransformReason.INFECTION,
                SpawnReason.INFECTION);
        if (entityZombieVillager == null) return; //Can happen in some rare cases.
        CraftVillagerZombie craftVillagerZombie = (CraftVillagerZombie) entityZombieVillager.getBukkitEntity();
        //Fix minecart/boat demounting bug
        if (vehicle != null && !vehicle.getPassengers().contains(craftVillagerZombie)) {
            vehicle.addPassenger(craftVillagerZombie);
        }

        //Store the Villager metadata in the Zombie Villager.
        entityZombieVillager.a(villagerData);
        entityZombieVillager.a(experience);
        entityZombieVillager.a(nbtTagCompound);

        //The Zombie Villager will inherit the speed of the fleeing Villager. We need to set it back to the normal speed.
        entityZombieVillager.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.23);

        //Set the Zombie Villager to a baby if the infected villager was one.
        if (!isAdult){
            craftVillagerZombie.setBaby();
        }
    }
}