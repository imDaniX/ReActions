package me.fromgate.reactions.events.listeners;

import me.fromgate.reactions.events.PlayerMoveByBlockEvent;
import me.fromgate.reactions.events.PlayerStayEvent;
import me.fromgate.reactions.module.defaults.StoragesManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RaListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveByBlockEvent event) {
        StoragesManager.triggerAllRegions(event.getPlayer(), event.getTo(), event.getFrom());
        StoragesManager.triggerCuboid(event.getPlayer());
    }

    @EventHandler
    public void onStay(PlayerStayEvent event) {
        StoragesManager.triggerCuboid(event.getPlayer());
    }
}
