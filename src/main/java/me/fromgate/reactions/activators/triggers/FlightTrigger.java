package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.FlightStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 5/2/2017.
 */
public class FlightTrigger extends Trigger {
    private final FlightType flight;

    private FlightTrigger(ActivatorBase base, FlightType type) {
        super(base);
        this.flight = type;
    }

    public static FlightTrigger create(ActivatorBase base, Parameters param) {
        FlightType type = FlightType.getByName(param.getString("flight", "ANY"));
        return new FlightTrigger(base, type);
    }

    public static FlightTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        FlightType type = FlightType.getByName(cfg.getString("flight", "ANY"));
        return new FlightTrigger(base, type);
    }

    @Override
    public boolean proceed(Storage event) {
        FlightStorage fe = (FlightStorage) event;
        return checkFlight(fe.isFlying());
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("flight:").append(this.flight.name());
        sb.append(")");
        return sb.toString();
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

}
