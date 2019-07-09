package me.fromgate.reactions.event;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class SneakEvent extends RAEvent {
	private Boolean sneak;

	public SneakEvent(Player player, boolean sneak) {
		super(player, ActivatorType.SNEAK);
		this.sneak = sneak;
	}

	public boolean isSneaking() {
		return this.sneak;
	}
}