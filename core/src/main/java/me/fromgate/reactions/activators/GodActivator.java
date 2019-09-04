package me.fromgate.reactions.activators;

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.GodStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.parameter.Param;
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
	public boolean activate(Storage event) {
		GodStorage e = (GodStorage) event;
		if (!checkGod(e.isGod())) return false;
		Variables.setTempVar("god", e.isGod() ? "TRUE" : "FALSE");
		return Actions.executeActivator(e.getPlayer(), getBase());
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
		StringBuilder sb = new StringBuilder(super.toString());
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
