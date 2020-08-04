package me.fromgate.reactions.events.listeners;

import me.fromgate.reactions.logic.StoragesManager;
import me.fromgate.reactions.events.PlayerMoveByBlockEvent;
import me.fromgate.reactions.events.PlayerStayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RaListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveByBlockEvent event) {
        StoragesManager.raiseAllRegionActivators(event.getPlayer(), event.getTo(), event.getFrom());
        StoragesManager.raiseCuboidActivator(event.getPlayer());
    }

    @EventHandler
    public void onStay(PlayerStayEvent event) {
        StoragesManager.raiseCuboidActivator(event.getPlayer());
    }
}
