package me.fromgate.reactions.util.data;

import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.location.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class StringValue implements DataValue {
	private String value;

	public StringValue(String value) {
		this.value = value;
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public double asDouble() {
		return value.length();
	}

	@Override
	public boolean asBoolean() {
		return value.equalsIgnoreCase("true");
	}

	@Override
	public Location asLocation() {
		return LocationUtil.parseLocation(value, new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
	}

	@Override
	public ItemStack asItemStack() {
		return VirtualItem.fromString(value);
	}

	@Override
	public boolean set(String value) {
		this.value = value;
		return true;
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
		return false;
	}

	@Override
	public boolean set(ItemStack value) {
		return false;
	}
}
