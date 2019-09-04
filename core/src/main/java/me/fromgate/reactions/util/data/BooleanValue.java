package me.fromgate.reactions.util.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BooleanValue implements DataValue {
	private boolean value;

	public BooleanValue(boolean value) {
		this.value = value;
	}

	@Override
	public String asString() {
		return Boolean.toString(value);
	}

	@Override
	public double asDouble() {
		return value ? 1 : 0;
	}

	@Override
	public boolean asBoolean() {
		return value;
	}

	@Override
	public Location asLocation() {
		return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
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
		this.value = value;
		return true;
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
