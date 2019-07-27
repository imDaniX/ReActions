package me.fromgate.reactions.storage;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GodStorage extends RAStorage {
	@Getter @Setter private boolean god;

	public GodStorage(Player player, boolean god) {
		super(player, ActivatorType.GOD);
		this.god = god;
	}
}
