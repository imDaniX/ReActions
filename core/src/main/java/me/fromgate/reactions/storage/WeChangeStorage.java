/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WeChangeStorage extends RAStorage {
	private Location location;
	private Material blockType;

	public WeChangeStorage(Player player, Location location, Material blockType) {
		super(player, ActivatorType.WE_CHANGE);
		this.location = location;
		this.blockType = blockType;

	}

	public Location getLocation() {
		return location;
	}

	public String getBlockType() {
		return blockType.name();
	}
}
