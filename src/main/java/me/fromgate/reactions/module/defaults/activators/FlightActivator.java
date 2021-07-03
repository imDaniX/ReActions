package me.fromgate.reactions.module.defaults.activators;

import me.fromgate.reactions.logic.ActivatorLogic;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.storages.FlightStorage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 5/2/2017.
 */
public class FlightActivator extends Activator {
    private final FlightType flight;

    private FlightActivator(ActivatorLogic base, FlightType type) {
        super(base);
        this.flight = type;
    }

    public static FlightActivator create(ActivatorLogic base, Parameters param) {
        FlightType type = FlightType.getByName(param.getString("flight", "ANY"));
        return new FlightActivator(base, type);
    }

    public static FlightActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        FlightType type = FlightType.getByName(cfg.getString("flight", "ANY"));
        return new FlightActivator(base, type);
    }

    @Override
    public boolean checkStorage(Storage event) {
        FlightStorage fe = (FlightStorage) event;
        return checkFlight(fe.isFlying());
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("flight", flight.name());
    }

    private boolean checkFlight(boolean isFlight) {
        return switch (flight) {
            case ANY -> true;
            case TRUE -> isFlight;
            case FALSE -> !isFlight;
        };
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
