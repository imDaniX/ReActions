package me.fromgate.reactions.logic.triggers;

import me.fromgate.reactions.logic.storages.SneakStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class SneakActivator extends Activator {
    private final SneakType sneak;

    private SneakActivator(ActivatorBase base, SneakType sneak) {
        super(base);
        this.sneak = sneak;
    }

    public static SneakActivator create(ActivatorBase base, Parameters param) {
        SneakType sneak = SneakType.getByName(param.getString("sneak", "ANY"));
        return new SneakActivator(base, sneak);
    }

    public static SneakActivator load(ActivatorBase base, ConfigurationSection cfg) {
        SneakType sneak = SneakType.getByName(cfg.getString("sneak", "ANY"));
        return new SneakActivator(base, sneak);
    }

    @Override
    public boolean proceed(Storage event) {
        SneakStorage se = (SneakStorage) event;
        return checkSneak(se.isSneaking());
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("sneak", sneak.name());
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.SNEAK;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private boolean checkSneak(boolean isSneak) {
        switch (sneak) {
            case ANY:
                return true;
            case TRUE:
                return isSneak;
            case FALSE:
                return !isSneak;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("sneak:").append(this.sneak.name());
        sb.append(")");
        return sb.toString();
    }

    enum SneakType {
        TRUE,
        FALSE,
        ANY;

        public static SneakType getByName(String sneakStr) {
            if (sneakStr.equalsIgnoreCase("true")) return SneakType.TRUE;
            if (sneakStr.equalsIgnoreCase("any")) return SneakType.ANY;
            return SneakType.FALSE;
        }
    }
}
