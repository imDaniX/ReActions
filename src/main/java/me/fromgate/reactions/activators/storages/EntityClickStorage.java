package me.fromgate.reactions.activators.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.triggers.ActivatorType;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
@Getter
public class EntityClickStorage extends Storage {

    private final Entity entity;

    public EntityClickStorage(Player p, Entity entity) {
        super(p, ActivatorType.ENTITY_CLICK);
        this.entity = entity;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        tempVars.put("entitytype", entity.getType().name());
        return tempVars;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return MapBuilder.single(CANCEL_EVENT, new BooleanValue(false));
    }
}
