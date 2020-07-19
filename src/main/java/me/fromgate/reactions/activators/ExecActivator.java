package me.fromgate.reactions.activators;

import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;

public class ExecActivator extends Activator {
    private ExecActivator(ActivatorBase base) {
        super(base);
    }

    public static Activator create(ActivatorBase base, Param param) {
        return new ExecActivator(base);
    }

    public static Activator load(ActivatorBase base, ConfigurationSection cfg) {
        return new ExecActivator(base);
    }

    @Override
    public boolean activate(Storage storage) {
        return true;
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.EXEC;
    }
}
