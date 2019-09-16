package me.fromgate.reactions.util.data;

import me.fromgate.reactions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DoubleValue implements DataValue {
	private double value;

	public DoubleValue(double value) {
		this.value = value;
	}

	@Override
	public String asString() {
		return Double.toString(value);
	}

	@Override
	public double asDouble() {
		return value;
	}

	@Override
	public boolean asBoolean() {
		return value >= 0;
	}

	@Override
	public Location asLocation() {
		return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	}

	@Override
	public ItemStack asItemStack() {
		int i = 0;
		for(Material mat : Material.values())
			if(value <= i++) return new ItemStack(mat);
		return new ItemStack(Material.STONE);
	}

	@Override
	public boolean set(String value) {
		if(Util.FLOAT.matcher(value).matches()) {
			this.value = Double.valueOf(value);
			return true;
		}
		return false;
	}

	@Override
	public boolean set(double value) {
		this.value = value;
		return true;
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
