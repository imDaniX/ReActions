package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.DamageStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
// TODO: Assemble to one activator
public class DamageActivator extends Activator {
	private final String damageCause;
	private final SourceType source;

	public DamageActivator(ActivatorBase base, String cause, SourceType source) {
		super(base);
		this.damageCause = cause;
		this.source = source;
	}

	@Override
	public boolean activate(RAStorage event) {
		DamageStorage de = (DamageStorage) event;
		if (!damageCauseCheck(de.getCause())) return false;
		if (!sourceCheck(de.getSource())) return false;
		Variables.setTempVar("damage", Double.toString(de.getDamage()));
		Variables.setTempVar("cause", de.getCause().name());
		Variables.setTempVar("source", de.getSource());
		return Actions.executeActivator(de.getPlayer(), getBase());
	}

	private static String getCauseByName(String damageCauseStr) {
		if (damageCauseStr != null) {
			for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
				if (damageCauseStr.equalsIgnoreCase(damageCause.name())) {
					return damageCause.name();
				}
			}
		}
		return "ANY";
	}

	private boolean damageCauseCheck(EntityDamageEvent.DamageCause dc) {
		if (damageCause.equals("ANY")) return true;
		return dc.name().equals(damageCause);
	}

	enum SourceType {
		BLOCK,
		ENTITY,
		OTHER,
		ANY;

		public static SourceType getByName(String sourceStr) {
			if (sourceStr != null) {
				for (SourceType SourceType : values()) {
					if (sourceStr.equalsIgnoreCase(SourceType.name())) {
						return SourceType;
					}
				}
			}
			return SourceType.ANY;
		}
	}

	private boolean sourceCheck(String st) {
		if (source.name().equals("ANY")) return true;
		return st.equals(source.name());
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("cause", this.damageCause);
		cfg.set("source", this.source.name());
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.DAMAGE;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("cause:").append(this.damageCause);
		sb.append("; source:").append(this.source.name());
		sb.append(")");
		return sb.toString();
	}

	public static DamageActivator create(ActivatorBase base, Param param) {
		String cause = param.getParam("cause", "ANY");
		SourceType source = SourceType.getByName(param.getParam("source", "ANY"));
		return new DamageActivator(base, cause, source);
	}

	public static DamageActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String cause = cfg.getString("cause", "ANY");
		SourceType source = SourceType.getByName(cfg.getString("source", "ANY"));
		return new DamageActivator(base, cause, source);
	}
}
