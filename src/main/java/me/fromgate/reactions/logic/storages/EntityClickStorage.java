package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("entitytype", entity.getType().name());
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
    }
}
