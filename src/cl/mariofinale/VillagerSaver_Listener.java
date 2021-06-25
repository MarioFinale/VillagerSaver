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
<<<<<<< HEAD
        Entity villager = event.getEntity();
        if(!(villager instanceof LivingEntity)) return;
        LivingEntity tVillager = (LivingEntity)villager;
        if (event.getDamager() == null) return;
        Entity tKiller = event.getDamager();
        if (!(tVillager.getHealth() - event.getDamage() <= 0)) return;
        if (!(tVillager.getType() == EntityType.VILLAGER)) return;
<<<<<<< HEAD
<<<<<<< HEAD
        if (!((tKiller.getType() == EntityType.ZOMBIE) || (tKiller.getType() == EntityType.ZOMBIE_VILLAGER)  || (tKiller.getType() == EntityType.DROWNED) || (tKiller.getType() == EntityType.HUSK))) return;
        if (villagerSaver.WorldBlackList.contains(tVillager.getWorld().getName())) return;
        StoreAndClearVillager(villager);
        event.setCancelled(true);
    }

    public void StoreAndClearVillager(Entity villager){ //CALLED WHEN A VILLAGER IS ABOUT TO DIE
        LivingEntity tVillager = (LivingEntity) villager;
        Location tLoc = tVillager.getLocation();
        World tWorld = tVillager.getWorld();
        Ageable tAVillager = (Ageable) villager;
        Villager oldVillager = (Villager) villager;
        Entity vehicle = tVillager.getVehicle();
        ZombieVillager zVillager;
        if (tAVillager.isAdult()){
            CraftVillager craftVillager = (CraftVillager) villager;
            EntityVillager entityVillager = craftVillager.getHandle();
            Reputation villagerReputations = entityVillager.fj();

            Villager.Type villagerType = oldVillager.getVillagerType();
            Villager.Profession villagerProfession = oldVillager.getProfession();
            int villagerLevel = oldVillager.getVillagerLevel();
            int villagerExp = oldVillager.getVillagerExperience();
            List<MerchantRecipe> villagerTrades = new ArrayList<>(oldVillager.getRecipes());
            Location villagerJobSite =  oldVillager.getMemory(MemoryKey.JOB_SITE);
            Location villagerHome = oldVillager.getMemory(MemoryKey.HOME);

            tVillager.setMemory(MemoryKey.JOB_SITE, new Location(tVillager.getWorld(), 0d,0d,0d));
            tVillager.setMemory(MemoryKey.HOME, new Location(tVillager.getWorld(), 0d,0d,0d));
            tVillager.remove();

            Entity newZombie =  tWorld.spawnEntity(tLoc, EntityType.ZOMBIE_VILLAGER);
            zVillager =  (ZombieVillager) newZombie;
            zVillager.setVillagerProfession(villagerProfession);
            zVillager.setAdult();
            UUID zombieUUID = newZombie.getUniqueId();

            villagerSaver.VillagersReputation.put(zombieUUID, villagerReputations);
            villagerSaver.VillagersTypes.put(zombieUUID,villagerType);
            villagerSaver.VillagersProfessions.put(zombieUUID,villagerProfession);
            villagerSaver.VillagersLevels.put(zombieUUID, villagerLevel);
            villagerSaver.VillagersExp.put(zombieUUID, villagerExp);
            villagerSaver.VillagersTrades.put(zombieUUID, villagerTrades);
            villagerSaver.VillagersJobSites.put(zombieUUID, villagerJobSite);
            villagerSaver.VillagersHomes.put(zombieUUID, villagerHome);

        }else{
            tVillager.remove();
            Entity newZombie =  tWorld.spawnEntity(tLoc, EntityType.ZOMBIE_VILLAGER); //Baby villagers never have any profession
            zVillager =  (ZombieVillager) newZombie;
            zVillager.setBaby();
=======
        if (!((tKiller.getType() == EntityType.ZOMBIE) || (tKiller.getType() == EntityType.ZOMBIE_VILLAGER)  || (tKiller.getType() == EntityType.DROWNED) || (tKiller.getType() == EntityType.HUSK) || (tKiller.getType() == EntityType.ZOMBIFIED_PIGLIN))) return;
        if (villagerSaver.WorldBlackList.contains(tVillager.getWorld().getName())) return;
        handleSpawnZombieVillager(tVillager);
        event.setCancelled(true);
    }

=======
        if (!((tKiller.getType() == EntityType.ZOMBIE) || (tKiller.getType() == EntityType.ZOMBIE_VILLAGER)  || (tKiller.getType() == EntityType.DROWNED) || (tKiller.getType() == EntityType.HUSK) || (tKiller.getType() == EntityType.ZOMBIFIED_PIGLIN))) return;
        if (villagerSaver.WorldBlackList.contains(tVillager.getWorld().getName())) return;
        handleSpawnZombieVillager(tVillager);
        event.setCancelled(true);
    }

>>>>>>> parent of 688c16c (Minor changes)
    public void handleSpawnZombieVillager(LivingEntity entityliving) {
        CraftVillager craftVillager = (CraftVillager) entityliving;
=======
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
>>>>>>> parent of cb5e316 (Revert "Merge branch 'pr/14'")
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
<<<<<<< HEAD
        if (entityZombieVillager == null) {
            return;
        }
=======
        if (entityZombieVillager == null) return;
>>>>>>> parent of cb5e316 (Revert "Merge branch 'pr/14'")
        CraftVillagerZombie craftVillagerZombie = (CraftVillagerZombie) entityZombieVillager.getBukkitEntity();

        if (vehicle != null && !vehicle.getPassengers().contains(craftVillagerZombie)) {
            vehicle.addPassenger(craftVillagerZombie);
<<<<<<< HEAD
>>>>>>> parent of 688c16c (Minor changes)
=======
>>>>>>> parent of cb5e316 (Revert "Merge branch 'pr/14'")
        }

        entityZombieVillager.setVillagerData(villagerData);
        // retain entityZombieVillager reputation with BaseNBT from the previous villager
        // DynamicOpsNBT.a is necessary because Gossips don't follow a specific Schema
        entityZombieVillager.a(reputation.a(DynamicOpsNBT.a).getValue());
        entityZombieVillager.a(experience);
        entityZombieVillager.setOffers(offers.a());
    }
}