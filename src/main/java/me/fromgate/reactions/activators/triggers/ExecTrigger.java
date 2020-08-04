package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class ExecTrigger extends Trigger {
    private ExecTrigger(ActivatorBase base) {
        super(base);
    }

    public static Trigger create(ActivatorBase base, Parameters param) {
        return new ExecTrigger(base);
    }

    public static Trigger load(ActivatorBase base, ConfigurationSection cfg) {
        return new ExecTrigger(base);
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
