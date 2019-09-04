package me.fromgate.reactions.activators;

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.SneakStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class SneakActivator extends Activator {
	private final SneakType sneak;

	public SneakActivator(ActivatorBase base, SneakType sneak) {
		super(base);
		this.sneak = sneak;
	}

	@Override
	public boolean activate(Storage event) {
		SneakStorage se = (SneakStorage) event;
		if (!checkSneak(se.isSneaking())) return false;
		Variables.setTempVar("sneak", se.isSneaking() ? "TRUE" : "FALSE");
		return Actions.executeActivator(se.getPlayer(), getBase());
	}

	@Override
	public void save(ConfigurationSection cfg) {
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

	private boolean checkSneak(Boolean isSneak) {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("sneak:").append(this.sneak.name());
		sb.append(")");
		return sb.toString();
	}

	public static SneakActivator create(ActivatorBase base, Param param) {
		SneakType sneak = SneakType.getByName(param.getParam("sneak", "ANY"));
		return new SneakActivator(base, sneak);
	}

	public static SneakActivator load(ActivatorBase base, ConfigurationSection cfg) {
		SneakType sneak = SneakType.getByName(cfg.getString("sneak", "ANY"));
		return new SneakActivator(base, sneak);
	}
}
