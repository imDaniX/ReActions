package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.GodStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-10-28.
 */
public class GodActivator extends Activator {
    private final GodType god;

    private GodActivator(ActivatorLogic base, GodType type) {
        super(base);
        this.god = type;
    }

    public static GodActivator create(ActivatorLogic base, Parameters param) {
        GodType type = GodType.getByName(param.getString("god", "ANY"));
        return new GodActivator(base, type);
    }

    public static GodActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        GodType type = GodType.getByName(cfg.getString("god", "ANY"));
        return new GodActivator(base, type);
    }

    @Override
    public boolean proceed(Storage event) {
        GodStorage e = (GodStorage) event;
        return checkGod(e.isGod());
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("god", god.name());
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.GOD;
    }

    private boolean checkGod(boolean isGod) {
        switch (god) {
            case ANY:
                return true;
            case TRUE:
                return isGod;
            case FALSE:
                return !isGod;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("god:").append(this.god.name());
        sb.append(")");
        return sb.toString();
    }

    private enum GodType {
        TRUE,
        FALSE,
        ANY;

        public static GodType getByName(String godStr) {
            if (godStr.equalsIgnoreCase("true")) return GodType.TRUE;
            if (godStr.equalsIgnoreCase("any")) return GodType.ANY;
            return GodType.FALSE;
        }
    }
}
