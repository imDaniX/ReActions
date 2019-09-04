package me.fromgate.reactions.storages;

/**
 * Created by MaxDikiy on 2017-05-14.
 */

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityClickStorage extends Storage {
	@Getter private final Entity entity;

	public EntityClickStorage(Player p, Entity entity) {
		super(p, ActivatorType.ENTITY_CLICK);
		this.entity = entity;
	}
}
