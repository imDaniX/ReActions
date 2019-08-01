package me.fromgate.reactions.util.location;

import lombok.Getter;
import me.fromgate.reactions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

// TODO: Use it in activators
/**
 * Location class, where all the coordinates can be null
 * If coordinate is null, it will be ignored in comparison
 */
public class BlockLocation {
	@Getter private final String world;
	@Getter private final Integer x;
	@Getter private final Integer y;
	@Getter private final Integer z;

	public BlockLocation(String loc) {
		if(loc == null || loc.isEmpty()) {
			this.world = null;
			this.x = null;
			this.y = null;
			this.z = null;
		} else {
			String[] locSplit = loc.split(",");
			this.world = locSplit[0];
			if(locSplit.length > 1 && Util.FLOAT_NEG.matcher(locSplit[1]).matches())
				this.x = Double.valueOf(locSplit[1]).intValue();
			else this.x = null;
			if(locSplit.length > 2 && Util.FLOAT_NEG.matcher(locSplit[2]).matches())
				this.y = Double.valueOf(locSplit[2]).intValue();
			else this.y = null;
			if(locSplit.length > 3 && Util.FLOAT_NEG.matcher(locSplit[3]).matches())
				this.z = Double.valueOf(locSplit[3]).intValue();
			else this.z = null;
		}
	}

	public BlockLocation(String world, Integer x, Integer y, Integer z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockLocation(Location loc) {
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
		if(x != null) if(x != loc.getBlockX()) return false;
		if(y != null) if(y != loc.getBlockY()) return false;
		if(z != null) if(z != loc.getBlockZ()) return false;
		if(world != null) return world.equals(loc.getWorld().getName());
		return true;
	}

	public boolean compare(World world, int x, int y, int z) {
		if(this.x != null) if(this.x != x) return false;
		if(this.y != null) if(this.y != y) return false;
		if(this.z != null) if(this.z != z) return false;
		if(this.world != null) return this.world.equals(world.getName());
		return true;
	}

	@Override
	public String toString() {
		return this.world + "," + x + "," + y + "," + z + ",0,0";
	}

	public boolean isEmpty() {
		return world == null && x == null && y == null && z == null;
	}
}
