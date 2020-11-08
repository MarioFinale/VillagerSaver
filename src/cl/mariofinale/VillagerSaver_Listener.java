package cl.mariofinale;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

public class VillagerSaver_Listener implements Listener{


    @EventHandler
    public void CustomEvent(VillagerSaver_CustomEvent event){
       Villager villager = (Villager) event.getEntity();
        UUID villagerUUID = villager.getUniqueId();
        villager.setVillagerType(villagerSaver.VillagersTypes.get(villagerUUID));
        villager.setProfession(villagerSaver.VillagersProfessions.get(villagerUUID));
        villager.setVillagerExperience(1);
        villager.setVillagerLevel(villagerSaver.VillagersLevels.get(villagerUUID));
        villager.setVillagerExperience(villagerSaver.VillagersExp.get(villagerUUID));
        villager.setRecipes(villagerSaver.VillagersTrades.get(villagerUUID));
        villager.setVillagerExperience(villagerSaver.VillagersExp.get(villagerUUID));
        villagerSaver.VillagersTypes.remove(villagerUUID);
        villagerSaver.VillagersProfessions.remove(villagerUUID);
        villagerSaver.VillagersLevels.remove(villagerUUID);
        villagerSaver.VillagersExp.remove(villagerUUID);
        villagerSaver.VillagersTrades.remove(villagerUUID);
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
        }
    }

    @EventHandler
    public void entityTransformEvent(EntityTransformEvent event){
        Entity zombie = event.getEntity();
        Entity villager = event.getTransformedEntity();
        UUID zombieUUID = zombie.getUniqueId();
        if (villagerSaver.VillagersProfessions.containsKey(zombieUUID)){
            villagerSaver.VillagersTypes.put(villager.getUniqueId(),villagerSaver.VillagersTypes.get(zombieUUID));
            villagerSaver.VillagersProfessions.put(villager.getUniqueId(),villagerSaver.VillagersProfessions.get(zombieUUID));
            villagerSaver.VillagersLevels.put(villager.getUniqueId(), villagerSaver.VillagersLevels.get(zombieUUID));
            villagerSaver.VillagersExp.put(villager.getUniqueId(), villagerSaver.VillagersExp.get(zombieUUID));
            villagerSaver.VillagersTrades.put(villager.getUniqueId(), villagerSaver.VillagersTrades.get(zombieUUID));
            villagerSaver.VillagersTypes.remove(zombieUUID);
            villagerSaver.VillagersProfessions.remove(zombieUUID);
            villagerSaver.VillagersLevels.remove(zombieUUID);
            villagerSaver.VillagersExp.remove(zombieUUID);
            villagerSaver.VillagersTrades.remove(zombieUUID);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        Entity villager = event.getEntity();
        if(!(villager instanceof LivingEntity)) return;
        LivingEntity tvillager = (LivingEntity)villager;
        if (event.getDamager() == null) return;
        Entity tkiller = event.getDamager();
        if (!(tvillager.getType() == EntityType.VILLAGER)) return;
        if (!((tkiller.getType() == EntityType.ZOMBIE) || (tkiller.getType() == EntityType.ZOMBIE_VILLAGER))) return;
        if (!(tvillager.getHealth() - event.getDamage() <= 0)) return;
        event.setCancelled(true);
        Location tloc = tvillager.getLocation();
        World tworld = tvillager.getWorld();
        Ageable tagvillager = (Ageable) villager;
        Villager oldVillager = (Villager) villager;

        if (tagvillager.isAdult()){
            Entity newZombie =  tworld.spawnEntity(tloc, EntityType.ZOMBIE_VILLAGER);
            ZombieVillager zVillager =  (ZombieVillager) newZombie;
            zVillager.setVillagerProfession(oldVillager.getProfession());

            villagerSaver.VillagersTypes.put(newZombie.getUniqueId(),oldVillager.getVillagerType());
            villagerSaver.VillagersProfessions.put(newZombie.getUniqueId(),oldVillager.getProfession());
            villagerSaver.VillagersLevels.put(newZombie.getUniqueId(), oldVillager.getVillagerLevel());
            villagerSaver.VillagersExp.put(newZombie.getUniqueId(), oldVillager.getVillagerExperience());
            villagerSaver.VillagersTrades.put(newZombie.getUniqueId(), new ArrayList<>(oldVillager.getRecipes()));
            tvillager.remove();
        }else{
            Entity newZombie =  tworld.spawnEntity(tloc, EntityType.ZOMBIE_VILLAGER);
            ZombieVillager zVillager =  (ZombieVillager) newZombie;
            zVillager.setBaby();
            tvillager.remove();
        }
    }
}