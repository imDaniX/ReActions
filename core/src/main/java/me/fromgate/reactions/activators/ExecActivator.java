package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.ExecStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;

public class ExecActivator extends Activator {
	public ExecActivator(ActivatorBase base) {
		super(base);
	}

	@Override
	public boolean activate(Storage storage) {
		ExecStorage ce = (ExecStorage) storage;
		if (ce.getActivatorId().equalsIgnoreCase(getBase().getName())) {
			Variables.setTempVars(ce.getTempVars());
			return Actions.executeActivator(ce.getPlayer(), getBase());
		}
		return false;
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.EXEC;
	}

	public static Activator create(ActivatorBase base, Param param) {
		return new ExecActivator(base);
	}

	public static Activator load(ActivatorBase base, ConfigurationSection cfg) {
		return new ExecActivator(base);
	}
}
