package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class ExecActivator extends Activator {
    private ExecActivator(ActivatorBase base) {
        super(base);
    }

    public static Activator create(ActivatorBase base, Parameters param) {
        return new ExecActivator(base);
    }

    public static Activator load(ActivatorBase base, ConfigurationSection cfg) {
        return new ExecActivator(base);
    }

    @Override
    public boolean proceed(Storage storage) {
        return true;
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.EXEC;
    }
}
