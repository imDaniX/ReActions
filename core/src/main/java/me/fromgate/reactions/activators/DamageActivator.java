package me.fromgate.reactions.activators;

import me.fromgate.reactions.storages.DamageStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.enums.DamageType;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
// TODO: Assemble to one activator
public class DamageActivator extends Activator {
	private final String damageCause;
	private final DamageType source;

	public DamageActivator(ActivatorBase base, String cause, DamageType source) {
		super(base);
		this.damageCause = cause;
		this.source = source;
	}

	@Override
	public boolean activate(Storage event) {
		DamageStorage de = (DamageStorage) event;
		if (!damageCauseCheck(de.getCause())) return false;
		if (!sourceCheck(de.getSource())) return false;
		return true;
	}

	private boolean damageCauseCheck(EntityDamageEvent.DamageCause dc) {
		if (damageCause.equals("ANY")) return true;
		return dc.name().equals(damageCause);
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
		DamageType source = DamageType.getByName(param.getParam("source", "ANY"));
		return new DamageActivator(base, cause, source);
	}

	public static DamageActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String cause = cfg.getString("cause", "ANY");
		DamageType source = DamageType.getByName(cfg.getString("source", "ANY"));
		return new DamageActivator(base, cause, source);
	}
}
