package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.GodStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-10-28.
 */
public class GodActivator extends Activator {
	private final GodType god;

	public GodActivator(ActivatorBase base, GodType type) {
		super(base);
		this.god = type;
	}

	@Override
	public boolean activate(RAStorage event) {
		GodStorage e = (GodStorage) event;
		if (!checkGod(e.isGod())) return false;
		Variables.setTempVar("god", e.isGod() ? "TRUE" : "FALSE");
		return Actions.executeActivator(e.getPlayer(), this);
	}

	@Override
	public void save(ConfigurationSection cfg) {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (");
		sb.append("god:").append(this.god.name());
		sb.append(")");
		return sb.toString();
	}

	public static GodActivator create(ActivatorBase base, Param param) {
		GodType type = GodType.getByName(param.getParam("god", "ANY"));
		return new GodActivator(base, type);
	}

	public static GodActivator load(ActivatorBase base, ConfigurationSection cfg) {
		GodType type =  GodType.getByName(cfg.getString("god", "ANY"));
		return new GodActivator(base, type);
	}
}
