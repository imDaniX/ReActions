package me.fromgate.reactions.events;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Deprecated
public class PlayerMoveByBlockEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Location to;
    @Getter
    private final Location from;

    public PlayerMoveByBlockEvent(Player player, Location to, Location from) {
        super(player);
        this.to = to;
        this.from = from;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
