package me.fromgate.reactions.activators;

import me.fromgate.reactions.storages.FlightStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 5/2/2017.
 */
public class FlightActivator extends Activator {
	private final FlightType flight;

	private FlightActivator(ActivatorBase base, FlightType type) {
		super(base);
		this.flight = type;
	}

	@Override
	public boolean activate(Storage event) {
		FlightStorage fe = (FlightStorage) event;
		if (!checkFlight(fe.isFlying())) return false;
		return true;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("flight", flight.name());
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.FLIGHT;
	}

	private boolean checkFlight(boolean isFlight) {
		switch (flight) {
			case ANY:
				return true;
			case TRUE:
				return isFlight;
			case FALSE:
				return !isFlight;
		}
		return false;
	}

	private enum FlightType {
		TRUE,
		FALSE,
		ANY;

		public static FlightType getByName(String flightStr) {
			if (flightStr.equalsIgnoreCase("true")) return FlightType.TRUE;
			if (flightStr.equalsIgnoreCase("any")) return FlightType.ANY;
			return FlightType.FALSE;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("flight:").append(this.flight.name());
		sb.append(")");
		return sb.toString();
	}

	public static FlightActivator create(ActivatorBase base, Param param) {
		FlightType type = FlightType.getByName(param.getParam("flight", "ANY"));
		return new FlightActivator(base, type);
	}

	public static FlightActivator load(ActivatorBase base, ConfigurationSection cfg) {
		FlightType type = FlightType.getByName(cfg.getString("flight", "ANY"));
		return new FlightActivator(base, type);
	}

}
