package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class SneakStorage extends RAStorage {
	@Getter private final boolean sneaking;

	public SneakStorage(Player player, boolean sneaking) {
		super(player, ActivatorType.SNEAK);
		this.sneaking = sneaking;
	}
}