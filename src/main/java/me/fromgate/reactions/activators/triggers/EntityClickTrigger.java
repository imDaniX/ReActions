package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.EntityClickStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class EntityClickTrigger extends Trigger {
    private final EntityType entityType;

    private EntityClickTrigger(ActivatorBase base, String entityType) {
        super(base);
        this.entityType = Utils.getEnum(EntityType.class, entityType);
    }

    public static EntityClickTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        String entityType = cfg.getString("entity-type");
        return new EntityClickTrigger(base, entityType);
    }

    public static EntityClickTrigger create(ActivatorBase base, Parameters param) {
        String entityType = param.getString("type", "");
        return new EntityClickTrigger(base, entityType);
    }

    @Override
    public boolean proceed(Storage event) {
        EntityClickStorage ece = (EntityClickStorage) event;
        if (ece.getEntity() == null) return false;
        return isActivatorEntity(ece.getEntity());
    }

    private boolean isActivatorEntity(Entity entity) {
        return this.entityType == null || entity.getType() == entityType;
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
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
