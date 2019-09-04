package me.fromgate.reactions.util.data;

import me.fromgate.reactions.util.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LocationValue implements DataValue {
	private Location value;

	public LocationValue(Location value) {
		this.value = value;
	}

	@Override
	public String asString() {
		return LocationUtil.locationToString(value);
	}

	@Override
	public double asDouble() {
		return value.getX() + value.getY() + value.getZ();
	}

	@Override
	public boolean asBoolean() {
		return value.isWorldLoaded();
	}

	@Override
	public Location asLocation() {
		return value;
	}

	@Override
	public ItemStack asItemStack() {
		return new ItemStack(Material.STONE);
	}

	@Override
	public boolean set(String value) {
		return false;
	}

	@Override
	public boolean set(double value) {
		return false;
	}

	@Override
	public boolean set(boolean value) {
		return false;
	}

	@Override
	public boolean set(Location value) {
		this.value = value;
		return true;
	}

	@Override
	public boolean set(ItemStack value) {
		return false;
	}
}
