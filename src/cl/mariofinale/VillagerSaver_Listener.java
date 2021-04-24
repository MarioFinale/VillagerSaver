package cl.mariofinale;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.Reputation;
import net.minecraft.server.v1_16_R3.ReputationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftVillager;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VillagerSaver_Listener implements Listener{

    @EventHandler
    public void CustomEvent(VillagerSaver_CustomEvent event){ //CALLED AFTER A ZOMBIE VILLAGER HAS TRANSFORMED FROM VILLAGER
       Villager villager = (Villager) event.getEntity();
        UUID villagerUUID = villager.getUniqueId();

        villager.setVillagerType(villagerSaver.VillagersTypes.get(villagerUUID));
        villager.setProfession(villagerSaver.VillagersProfessions.get(villagerUUID));
        villager.setVillagerExperience(1);
        villager.setVillagerLevel(villagerSaver.VillagersLevels.get(villagerUUID));
        villager.setVillagerExperience(villagerSaver.VillagersExp.get(villagerUUID));
        villager.setRecipes(villagerSaver.VillagersTrades.get(villagerUUID));
        villager.setVillagerExperience(villagerSaver.VillagersExp.get(villagerUUID));
        villager.setMemory(MemoryKey.JOB_SITE,villagerSaver.VillagersJobSites.get(villagerUUID));
        villager.setMemory(MemoryKey.HOME,villagerSaver.VillagersHomes.get(villagerUUID));

        villagerSaver.VillagersTypes.remove(villagerUUID);
        villagerSaver.VillagersProfessions.remove(villagerUUID);
        villagerSaver.VillagersLevels.remove(villagerUUID);
        villagerSaver.VillagersExp.remove(villagerUUID);
        villagerSaver.VillagersTrades.remove(villagerUUID);
        villagerSaver.VillagersJobSites.remove(villagerUUID);
        villagerSaver.VillagersHomes.remove(villagerUUID);

    }

    @EventHandler
    public void CustomEvent2(VillagerSaver_CustomEvent2 event){ //CALLED AFTER A ZOMBIE VILLAGER HAS TRANSFORMED FROM VILLAGER AND HAS BEEN ASSIGNED A PROFESSION
        Villager villager = (Villager) event.getEntity();
        UUID villagerUUID = villager.getUniqueId();

        if (villagerSaver.VillagersHealers.containsKey(villagerUUID)){
            CraftVillager newCraftVillager = (CraftVillager) villager;
            EntityVillager newEntVillager = newCraftVillager.getHandle();
            UUID healerUUID = villagerSaver.VillagersHealers.get(villagerUUID);
            Reputation newReputations = newEntVillager.fj();
            Reputation originalReputations = villagerSaver.VillagersReputation.get(villagerUUID);
            Map<UUID, Reputation.a> originalReputationsMap = originalReputations.getReputations();

            for (Map.Entry<UUID, Reputation.a> rep: originalReputationsMap.entrySet()){
                UUID entityUUID = rep.getKey();
                int majorPositive = originalReputations.a(entityUUID, reputationType -> reputationType == ReputationType.MAJOR_POSITIVE);
                int minorPositive = originalReputations.a(entityUUID, reputationType -> reputationType == ReputationType.MINOR_POSITIVE);
                int majorNegative = originalReputations.a(entityUUID, reputationType -> reputationType == ReputationType.MAJOR_NEGATIVE);
                int minorNegative = originalReputations.a(entityUUID, reputationType -> reputationType == ReputationType.MINOR_NEGATIVE);
                int trading = originalReputations.a(entityUUID, reputationType -> reputationType == ReputationType.TRADING);

                int oldMajorPositive = newReputations.a(entityUUID, reputationType -> reputationType == ReputationType.MAJOR_POSITIVE);
                int oldMinorPositive = newReputations.a(entityUUID, reputationType -> reputationType == ReputationType.MINOR_POSITIVE);
                int oldMajorNegative = newReputations.a(entityUUID, reputationType -> reputationType == ReputationType.MAJOR_NEGATIVE);
                int oldMinorNegative = newReputations.a(entityUUID, reputationType -> reputationType == ReputationType.MINOR_NEGATIVE);
                int oldTrading = newReputations.a(entityUUID, reputationType -> reputationType == ReputationType.TRADING);

                newReputations.a(entityUUID, ReputationType.MAJOR_POSITIVE, -1 * oldMajorPositive);
                newReputations.a(entityUUID, ReputationType.MINOR_POSITIVE, -1 * oldMinorPositive);
                newReputations.a(entityUUID, ReputationType.MAJOR_NEGATIVE, -1 * oldMajorNegative);
                newReputations.a(entityUUID, ReputationType.MINOR_NEGATIVE, -1 * oldMinorNegative);
                newReputations.a(entityUUID, ReputationType.TRADING, -1 * oldTrading);

                if (entityUUID == healerUUID){
                    majorPositive += 20;
                    minorPositive += 25;
                    if (majorPositive > 100) majorPositive = 100;
                    if (minorPositive > 200) minorPositive = 200;
                }

                newReputations.a(entityUUID, ReputationType.MAJOR_POSITIVE,majorPositive);
                newReputations.a(entityUUID, ReputationType.MINOR_POSITIVE,minorPositive);
                newReputations.a(entityUUID, ReputationType.MAJOR_NEGATIVE, majorNegative);
                newReputations.a(entityUUID, ReputationType.MINOR_NEGATIVE, minorNegative);
                newReputations.a(entityUUID, ReputationType.TRADING,trading);

            }
        }
        villagerSaver.VillagersReputation.remove(villagerUUID);
        villagerSaver.VillagersHealers.remove(villagerUUID);
    }

    @EventHandler
    public void CreatureSpawnEvent(CreatureSpawnEvent event){
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CURED){
            Entity ent = (Villager) event.getEntity();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(villagerSaver.plugin, new Runnable(){
                @Override
                public void run(){
                    Bukkit.getPluginManager().callEvent(new VillagerSaver_CustomEvent(ent));
                }
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(villagerSaver.plugin, new Runnable(){
                @Override
                public void run(){
                    Bukkit.getPluginManager().callEvent(new VillagerSaver_CustomEvent2(ent));
                }
            }, 25L);

        }
    }

    @EventHandler
    public void entityTransformEvent(EntityTransformEvent event){
        Entity zombie = event.getEntity();
        Entity villager = event.getTransformedEntity();
        UUID zombieUUID = zombie.getUniqueId();
        UUID villagerUUID = villager.getUniqueId();
        if (villagerSaver.VillagersProfessions.containsKey(zombieUUID)){
            villagerSaver.VillagersTypes.put(villagerUUID,villagerSaver.VillagersTypes.get(zombieUUID));
            villagerSaver.VillagersProfessions.put(villagerUUID,villagerSaver.VillagersProfessions.get(zombieUUID));
            villagerSaver.VillagersLevels.put(villagerUUID, villagerSaver.VillagersLevels.get(zombieUUID));
            villagerSaver.VillagersExp.put(villagerUUID, villagerSaver.VillagersExp.get(zombieUUID));
            villagerSaver.VillagersTrades.put(villagerUUID, villagerSaver.VillagersTrades.get(zombieUUID));
            villagerSaver.VillagersReputation.put(villagerUUID, villagerSaver.VillagersReputation.get(zombieUUID));
            villagerSaver.VillagersJobSites.put(villagerUUID, villagerSaver.VillagersJobSites.get(zombieUUID));
            villagerSaver.VillagersHomes.put(villagerUUID, villagerSaver.VillagersHomes.get(zombieUUID));

            villagerSaver.VillagersTypes.remove(zombieUUID);
            villagerSaver.VillagersProfessions.remove(zombieUUID);
            villagerSaver.VillagersLevels.remove(zombieUUID);
            villagerSaver.VillagersExp.remove(zombieUUID);
            villagerSaver.VillagersTrades.remove(zombieUUID);
            villagerSaver.VillagersReputation.remove(zombieUUID);
            villagerSaver.VillagersJobSites.remove(zombieUUID);
            villagerSaver.VillagersHomes.remove(zombieUUID);
        }
        if (villagerSaver.VillagersHealers.containsKey(zombieUUID)){ //In a different "if" so baby villagers reputation also gets updated
            villagerSaver.VillagersHealers.put(villagerUUID, villagerSaver.VillagersHealers.get(zombieUUID));
            villagerSaver.VillagersHealers.remove(zombieUUID);
        }
    }

    @EventHandler
    public void PlayerInteractEntityEvent(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if(!(entity instanceof LivingEntity)) return;
        if (!(entity.getType() == EntityType.ZOMBIE_VILLAGER)) return;
        LivingEntity zombieVillager = (LivingEntity) entity;
        PotionEffect effect =  zombieVillager.getPotionEffect(PotionEffectType.WEAKNESS);
        if (effect == null) return;
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack gApple =  new ItemStack(Material.GOLDEN_APPLE, 1);
        if (!(offHandItem.isSimilar(gApple) || mainHandItem.isSimilar(gApple))) return;
        villagerSaver.VillagersHealers.put(entity.getUniqueId(),player.getUniqueId());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        Entity villager = event.getEntity();
        if(!(villager instanceof LivingEntity)) return;
        LivingEntity tVillager = (LivingEntity)villager;
        if (event.getDamager() == null) return;
        Entity tKiller = event.getDamager();
        if (!(tVillager.getHealth() - event.getDamage() <= 0)) return;
        if (!(tVillager.getType() == EntityType.VILLAGER)) return;
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
        }

            vehicle.addPassenger(zVillager);
        }
    }
}