package me.fromgate.reactions.util.location;

import lombok.Getter;
import me.fromgate.reactions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

// TODO: Use it in activators
/**
 * Location class, where all the coordinates can be null
 * If coordinate is null, it will be ignored in comparison
 */
public class VirtualLocation {
	@Getter private final String world;
	@Getter private final Integer x;
	@Getter private final Integer y;
	@Getter private final Integer z;
	private final int hash;

	public VirtualLocation() {
		this.world = null;
		this.x = null;
		this.y = null;
		this.z = null;
		this.hash = calcHash();
	}

	public VirtualLocation(String loc) {
		if(!Util.isStringEmpty(loc)) {
			this.world = null;
			this.x = null;
			this.y = null;
			this.z = null;
		} else {
			String[] locSplit = loc.split(",");
			this.world = locSplit[0];
			if(locSplit.length > 1 && Util.FLOAT.matcher(locSplit[1]).matches())
				this.x = Double.valueOf(locSplit[1]).intValue();
			else this.x = null;
			if(locSplit.length > 2 && Util.FLOAT.matcher(locSplit[2]).matches())
				this.y = Double.valueOf(locSplit[2]).intValue();
			else this.y = null;
			if(locSplit.length > 3 && Util.FLOAT.matcher(locSplit[3]).matches())
				this.z = Double.valueOf(locSplit[3]).intValue();
			else this.z = null;
		}
		this.hash = calcHash();
	}

	public VirtualLocation(String world, Integer x, Integer y, Integer z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hash = calcHash();
	}

	public VirtualLocation(Location loc) {
		if(loc == null) {
			this.world = null;
			this.x = null;
			this.y = null;
			this.z = null;
		} else {
			this.world = loc.getWorld().getName();
			this.x = loc.getBlockX();
			this.y = loc.getBlockY();
			this.z = loc.getBlockZ();
		}
		this.hash = calcHash();
	}

	public int getX(int def) {
		return x == null ? def : x;
	}

	public int getY(int def) {
		return y == null ? def : y;
	}

	public int getZ(int def) {
		return z == null ? def : z;
	}

	public Location getLocation() {
		World world = Bukkit.getWorld(this.world);
		return new Location(
				world == null ? Bukkit.getWorlds().get(0) : world,
				x == null ? 0 : x,
				y == null ? 0 : y,
				z == null ? 0 : z);
	}

	public boolean compare(Location loc) {
		if(x != null && x != loc.getBlockX()) return false;
		if(y != null && y != loc.getBlockY()) return false;
		if(z != null && z != loc.getBlockZ()) return false;
		if(world != null) return world.equals(loc.getWorld().getName());
		return true;
	}

	public boolean compare(World world, int x, int y, int z) {
		if(this.x != null && this.x != x) return false;
		if(this.y != null && this.y != y) return false;
		if(this.z != null && this.z != z) return false;
		if(this.world != null) return this.world.equals(world.getName());
		return true;
	}

	private int calcHash() {
		int hash = 3;
		int x = this.x != null ? this.x : -7;
		int y = this.y != null ? this.y : -19;
		int z = this.z != null ? this.z : 31;
		hash = 19 * hash + (world != null ? world.hashCode() : 3);
		hash = 19 * hash + x ^ x >>> 16;
		hash = 19 * hash + y ^ y >>> 16;
		hash = 19 * hash + z ^ z >>> 16;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof VirtualLocation) {
			VirtualLocation loc = (VirtualLocation) obj;
			return Objects.equals(loc.x, this.x) &&
					Objects.equals(loc.y, this.y) &&
					Objects.equals(loc.z, this.z) &&
					Objects.equals(loc.world, this.world);
		}
		if(obj instanceof Location) {
			Location loc = (Location) obj;
			return Objects.equals(loc.getBlockX(), this.x) &&
					Objects.equals(loc.getBlockY(), this.y) &&
					Objects.equals(loc.getBlockZ(), this.z) &&
					Objects.equals(loc.getWorld().getName(), this.world);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public String toString() {
		return this.world + "," + x + "," + y + "," + z + ",0,0";
	}

	public boolean isEmpty() {
		return world == null && x == null && y == null && z == null;
	}
}
