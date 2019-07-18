package me.fromgate.reactions.event;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Actually move event
 */
public class CuboidEvent extends RAEvent {
	public CuboidEvent(Player player) {
		super(player, ActivatorType.CUBOID);
	}
}
