package me.fromgate.reactions.storages;

/**
 * Created by MaxDikiy on 2017-05-14.
 */

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;

public class EntityClickStorage extends Storage {
	@Getter private final Entity entity;

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
		changeables.put(Storage.CANCEL_EVENT, new BooleanValue(false));
	}
}
