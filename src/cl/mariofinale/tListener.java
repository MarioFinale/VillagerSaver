package cl.mariofinale;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class tListener implements Listener{
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        Entity villager = event.getEntity();
        LivingEntity tvillager = (LivingEntity)villager;
        Entity tkiller = event.getDamager();
        if (!(tvillager.getType() == EntityType.VILLAGER)) return;
        if (!((tkiller.getType() == EntityType.ZOMBIE) || (tkiller.getType() == EntityType.ZOMBIE_VILLAGER))) return;
        if (!(tvillager.getHealth() - event.getDamage() <= 0)) return;
        event.setCancelled(true);
        Location tloc = tvillager.getLocation();
        World tworld = tvillager.getWorld();
        tvillager.remove();
        Ageable tagvillager = (Ageable)villager;
        if (tagvillager.isAdult()){
            tworld.spawn(tloc, ZombieVillager.class);
        }else{
            tworld.spawn(tloc, ZombieVillager.class).setBaby(true);
        }
    }
}