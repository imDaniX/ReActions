package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/2/2017.
 */
public class FlightStorage extends RAStorage {
	@Getter private boolean flying;

	public FlightStorage(Player p, boolean flying) {
		super(p, ActivatorType.FLIGHT);
		this.flying = flying;
	}
}
