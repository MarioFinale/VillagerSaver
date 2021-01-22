package cl.mariofinale;
import com.destroystokyo.paper.entity.villager.Reputation;
import com.destroystokyo.paper.entity.villager.ReputationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
            UUID healerUUID = villagerSaver.VillagersHealers.get(villagerUUID);
            Map<UUID, Reputation> rep = villagerSaver.VillagersReputation.get(villagerUUID);
            Reputation reputation = rep.get(healerUUID);
            if (reputation == null) reputation = new Reputation();

            int major_positive = reputation.getReputation(ReputationType.MAJOR_POSITIVE);
            int minor_positive = reputation.getReputation(ReputationType.MINOR_POSITIVE);

            major_positive += 20;
            minor_positive += 25;

            if (major_positive > 100) major_positive = 100;
            if (minor_positive > 200) minor_positive = 200;

            reputation.setReputation(ReputationType.MAJOR_POSITIVE, major_positive);
            reputation.setReputation(ReputationType.MINOR_POSITIVE, minor_positive);

            rep.put(healerUUID,reputation);
            villager.setReputations(rep);

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
        if (villagerSaver.VillagersHealers.containsKey(zombieUUID)){
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
        if (!((tKiller.getType() == EntityType.ZOMBIE) || (tKiller.getType() == EntityType.ZOMBIE_VILLAGER)  || (tKiller.getType() == EntityType.DROWNED))) return;
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
        if (tAVillager.isAdult()){
            Map<UUID,Reputation> villagerReputations = oldVillager.getReputations();
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
            ZombieVillager zVillager =  (ZombieVillager) newZombie;
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
            Entity newZombie =  tWorld.spawnEntity(tLoc, EntityType.ZOMBIE_VILLAGER);
            ZombieVillager zVillager =  (ZombieVillager) newZombie;
            zVillager.setBaby();
        }
    }
}