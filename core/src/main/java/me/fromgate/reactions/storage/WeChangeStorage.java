/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WeChangeStorage extends RAStorage {
	@Getter private final Location location;
	@Getter private final Material blockType;

	public WeChangeStorage(Player player, Location location, Material blockType) {
		super(player, ActivatorType.WE_CHANGE);
		this.location = location;
		this.blockType = blockType;

	}
}
