package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class SneakStorage extends RAStorage {
	private boolean sneak;

	public SneakStorage(Player player, boolean sneak) {
		super(player, ActivatorType.SNEAK);
		this.sneak = sneak;
	}

	public boolean isSneaking() {
		return this.sneak;
	}
}