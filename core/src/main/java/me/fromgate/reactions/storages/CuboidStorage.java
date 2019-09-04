package me.fromgate.reactions.storages;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

public class CuboidStorage extends Storage {
	public CuboidStorage(Player player) {
		super(player, ActivatorType.CUBOID);
	}
}
