package me.fromgate.reactions.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEvent;

@Deprecated
public class PlayerAttacksEntityEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final LivingEntity entity;
    @Getter
    private final EntityDamageEvent.DamageCause cause;
    private boolean cancel = false;
    @Getter
    @Setter
    private double damage;

    public PlayerAttacksEntityEvent(Player player, LivingEntity entity, double damage, EntityDamageEvent.DamageCause cause) {
        super(player);
        this.entity = entity;
        this.damage = damage;
        this.cause = cause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}