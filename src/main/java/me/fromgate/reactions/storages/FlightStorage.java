package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by MaxDikiy on 5/2/2017.
 */
public class FlightStorage extends Storage {
	@Getter private boolean flying;

	public FlightStorage(Player p, boolean flying) {
		super(p, ActivatorType.FLIGHT);
		this.flying = flying;
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		tempVars.put("flight", Boolean.toString(flying));
	}

	@Override
	void defaultChangeables(Map<String, DataValue> changeables) {
		changeables.put(CANCEL_EVENT, new BooleanValue(false));
	}
}
