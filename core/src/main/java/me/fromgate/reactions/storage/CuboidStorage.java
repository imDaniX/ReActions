package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Actually move event
 */
public class CuboidStorage extends RAStorage {
	public CuboidStorage(Player player) {
		super(player, ActivatorType.CUBOID);
	}
}
