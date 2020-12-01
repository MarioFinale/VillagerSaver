package cl.mariofinale;
import com.destroystokyo.paper.entity.villager.Reputation;
import com.destroystokyo.paper.entity.villager.ReputationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Map;
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
    public void CustomEvent2(VillagerSaver_CustomEvent2 event){
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

            villagerSaver.VillagersTypes.remove(zombieUUID);
            villagerSaver.VillagersProfessions.remove(zombieUUID);
            villagerSaver.VillagersLevels.remove(zombieUUID);
            villagerSaver.VillagersExp.remove(zombieUUID);
            villagerSaver.VillagersTrades.remove(zombieUUID);
            villagerSaver.VillagersReputation.remove(zombieUUID);
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
        if (!(tVillager.getType() == EntityType.VILLAGER)) return;
        if (!((tKiller.getType() == EntityType.ZOMBIE) || (tKiller.getType() == EntityType.ZOMBIE_VILLAGER))) return;
        if (!(tVillager.getHealth() - event.getDamage() <= 0)) return;
        if (villagerSaver.WorldBlackList.contains(tVillager.getWorld().getName())) return;
        event.setCancelled(true);
        Location tLoc = tVillager.getLocation();
        World tWorld = tVillager.getWorld();
        Ageable tAVillager = (Ageable) villager;
        Villager oldVillager = (Villager) villager;
        if (tAVillager.isAdult()){
            Entity newZombie =  tWorld.spawnEntity(tLoc, EntityType.ZOMBIE_VILLAGER);
            ZombieVillager zVillager =  (ZombieVillager) newZombie;
            zVillager.setVillagerProfession(oldVillager.getProfession());
            zVillager.setAdult();
            villagerSaver.VillagersReputation.put(newZombie.getUniqueId(), oldVillager.getReputations());
            villagerSaver.VillagersTypes.put(newZombie.getUniqueId(),oldVillager.getVillagerType());
            villagerSaver.VillagersProfessions.put(newZombie.getUniqueId(),oldVillager.getProfession());
            villagerSaver.VillagersLevels.put(newZombie.getUniqueId(), oldVillager.getVillagerLevel());
            villagerSaver.VillagersExp.put(newZombie.getUniqueId(), oldVillager.getVillagerExperience());
            villagerSaver.VillagersTrades.put(newZombie.getUniqueId(), new ArrayList<>(oldVillager.getRecipes()));
            tVillager.remove();
        }else{
            Entity newZombie =  tWorld.spawnEntity(tLoc, EntityType.ZOMBIE_VILLAGER);
            ZombieVillager zVillager =  (ZombieVillager) newZombie;
            zVillager.setBaby();
            tVillager.remove();
        }
    }
}