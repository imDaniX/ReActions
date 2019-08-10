package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.ExecStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.configuration.ConfigurationSection;

public class Activator {
	private final ActivatorBase base;

	public Activator(ActivatorBase base) {
		this.base = base;
	}

	/**
	 * Execution of activator
	 * @param storage Storage with data for activator
	 * @return Cancel original event or not
	 */
	public boolean executeActivator(RAStorage storage) {
		boolean result = activate(storage);
		Variables.clearAllTempVar();
		return result;
	}

	/**
	 * Get base of activator
	 * @return Related ActivatorBase
	 */
	public ActivatorBase getBase() {
		return base;
	}

	/**
	 * Execution of activator
	 * @param storage Storage with data for activator
	 * @return Do we need to cancel original event (actually used just for action CANCEL_EVENT)
	 */
	public boolean activate(RAStorage storage) {
		ExecStorage ce = (ExecStorage) storage;
		if (ce.getActivatorId().equalsIgnoreCase(getBase().getName())) {
			Variables.setTempVars(ce.getTempVars());
			return Actions.executeActivator(ce.getTargetPlayer(), getBase());
		}
		return false;
	}

	/**
	 * Save activator to config with actions, reactions and flags
	 * @param cfg Section of activator
	 */
	public void saveActivator(ConfigurationSection cfg) {
		base.saveBase(cfg);
		save(cfg);
	}

	/**
	 * Save activator to config
	 * @param cfg Section of activator
	 */
	public void save(ConfigurationSection cfg) {}

	/**
	 * Get type of activator
	 * @return Type of activator
	 */
	public ActivatorType getType() {
		return ActivatorType.EXEC;
	}

	/**
	 * Check if activator is valid
	 * @return Is activator valid
	 */
	public boolean isValid() {
		return true;
	}

	@Override
	public int hashCode() {
		return base.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(base.getGroup()).append(", ").append(base.getName()).append(" [").append(getType()).append("]");
		sb.append(base.toString());
		return sb.toString();
	}

	public static Activator create(ActivatorBase base, Param param) {
		return new Activator(base);
	}

	public static Activator load(ActivatorBase base, ConfigurationSection cfg) {
		return new Activator(base);
	}
}
