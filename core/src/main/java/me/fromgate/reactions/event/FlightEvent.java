package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/2/2017.
 */
public class FlightEvent extends RAEvent {
	private boolean isFlying;

	public FlightEvent(Player p, boolean isFlying) {
		super(p);
		this.isFlying = isFlying;
	}

	public boolean getFlight() {
		return this.isFlying;
	}

}
