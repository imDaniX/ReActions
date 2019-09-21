package me.fromgate.reactions.activators;

import me.fromgate.reactions.storages.EntityClickStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class EntityClickActivator extends Activator {
	private final String entityType;

	private EntityClickActivator(ActivatorBase base, String entityType) {
		super(base);
		this.entityType = entityType;
	}

	@Override
	public boolean activate(Storage event) {
		EntityClickStorage ece = (EntityClickStorage) event;
		if (ece.getEntity() == null) return false;
		if (!isActivatorEntity(ece.getEntity())) return false;
		return true;
	}

	private boolean isActivatorEntity(Entity entity) {
		return this.entityType.isEmpty() || entity.getType().toString().equalsIgnoreCase(this.entityType);
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("entity-type", this.entityType);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.ENTITY_CLICK;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("type:").append(entityType.isEmpty() ? "-" : entityType.toUpperCase());
		sb.append(")");
		return sb.toString();
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public static EntityClickActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String entityType = cfg.getString("entity-type");
		return new EntityClickActivator(base, entityType);
	}

	public static EntityClickActivator create(ActivatorBase base, Param param) {
		String entityType = param.getParam("type", "");
		return new EntityClickActivator(base, entityType);
	}
}
