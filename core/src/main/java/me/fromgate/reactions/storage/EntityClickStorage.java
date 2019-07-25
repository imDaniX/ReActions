package me.fromgate.reactions.storage;

/**
 * Created by MaxDikiy on 2017-05-14.
 */

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityClickStorage extends RAStorage {
	private final Entity entity;

	public EntityClickStorage(Player p, Entity entity) {
		super(p, ActivatorType.ENTITY_CLICK);
		this.entity = entity;
	}

	public Entity getEntity() {
		return this.entity;
	}

}
