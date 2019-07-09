package me.fromgate.reactions.event;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/2/2017.
 */
public class FlightEvent extends RAEvent {
	private boolean flying;

	public FlightEvent(Player p, boolean isFlying) {
		super(p, ActivatorType.FLIGHT);
		this.flying = isFlying;
	}

	public boolean isFlying() {
		return this.flying;
	}

}
