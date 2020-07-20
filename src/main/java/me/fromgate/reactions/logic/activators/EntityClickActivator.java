package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.EntityClickStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class EntityClickActivator extends Activator {
    private final EntityType entityType;

    private EntityClickActivator(ActivatorBase base, String entityType) {
        super(base);
        this.entityType = Util.getEnum(EntityType.class, entityType);
    }

    public static EntityClickActivator load(ActivatorBase base, ConfigurationSection cfg) {
        String entityType = cfg.getString("entity-type");
        return new EntityClickActivator(base, entityType);
    }

    public static EntityClickActivator create(ActivatorBase base, Parameters param) {
        String entityType = param.getParam("type", "");
        return new EntityClickActivator(base, entityType);
    }

    @Override
    public boolean activate(Storage event) {
        EntityClickStorage ece = (EntityClickStorage) event;
        if (ece.getEntity() == null) return false;
        return isActivatorEntity(ece.getEntity());
    }

    private boolean isActivatorEntity(Entity entity) {
        return this.entityType == null || entity.getType() == entityType;
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("entity-type", this.entityType.name());
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.ENTITY_CLICK;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("type:").append(entityType == null ? "-" : entityType.name());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
