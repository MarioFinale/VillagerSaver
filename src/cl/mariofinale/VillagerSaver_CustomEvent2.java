package cl.mariofinale;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VillagerSaver_CustomEvent2 extends Event {


    private static final HandlerList HANDLERS = new HandlerList();
    private Entity entity;

    public VillagerSaver_CustomEvent2(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity(){
        return this.entity;
    }

    public VillagerSaver_CustomEvent2() {
    }


    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}