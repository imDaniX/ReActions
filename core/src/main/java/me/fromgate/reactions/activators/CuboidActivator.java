package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.location.BlockLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// TODO: Cuboid flag
// TODO: Command to create cuboid activator
public class CuboidActivator extends Activator implements Locatable {
	private final String world;
	private final CuboidMode mode;
	private final int xMin, yMin, zMin;
	private final int xMax, yMax, zMax;
	private final boolean twoDimensional;
	private final Set<UUID> within = new HashSet<>();

	public CuboidActivator(ActivatorBase base, BlockLocation loc1, BlockLocation loc2, CuboidMode mode, boolean twoDimensional) {
		super(base);
		this.world = loc1.getWorld();
		this.mode = mode;
		int[] pair = Util.sortedIntPair(loc1.getX(), loc2.getX());
		xMin = pair[0]; xMax = pair[1];
		pair = Util.sortedIntPair(loc1.getZ(), loc2.getZ());
		zMin = pair[0]; zMax = pair[1];
		this.twoDimensional = twoDimensional;
		if(!twoDimensional) {
			pair = Util.sortedIntPair(loc1.getY(), loc2.getY());
			yMin = pair[0]; yMax = pair[1];
		} else {
			yMin = 0; yMax = 256;
		}
	}

	@Override
	public boolean activate(RAStorage event) {
		Player player = event.getPlayer();
		UUID id = player.getUniqueId();
		boolean inCuboid = checkInCuboid(player.getLocation(), true);
		switch(mode) {
			case CHECK:
				if(inCuboid)
					Actions.executeActivator(player, getBase());
				break;
			case ENTER:
				if(inCuboid && within.add(id))
					Actions.executeActivator(player, getBase());
				break;
			case LEAVE:
				if(!inCuboid && within.remove(id))
					Actions.executeActivator(player, getBase());
				break;
		}
		return false;
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return checkInCuboid(loc, false);
	}

	@Override
	public boolean isLocatedAt(World world, int x, int y, int z) {
		return isLocatedAt(new Location(world, x, y, z));
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("mode", mode.toString());
		cfg.set("two_dimensional", twoDimensional);
		cfg.set("world", world);
		cfg.set("loc1.x", xMin); cfg.set("loc2.x", xMax);
		cfg.set("loc1.y", yMin); cfg.set("loc2.y", yMax);
		cfg.set("loc1.z", zMin); cfg.set("loc2.z", zMax);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.CUBOID;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	private boolean checkInCuboid(Location loc, boolean head) {
		if(!loc.getWorld().getName().equalsIgnoreCase(world))
			return false;
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		if((xMin > x || xMax < x) || (zMin > z || zMax < z))
			return false;
		if(twoDimensional)
			return true;
		int y = loc.getBlockY();
		return (y >= yMin && y <= yMax) || (!head || (y+1.75 >= yMin && y+1.75 <= yMax));
	}

	private enum CuboidMode {
		CHECK,ENTER,LEAVE;
		static CuboidMode getByName(String name) {
			switch(name.toUpperCase()) {
				case "CHECK": return CHECK;
				case "LEAVE": return LEAVE;
				default: return ENTER;
			}
		}
	}

	// TODO: toString method
	
	public static CuboidActivator create(ActivatorBase base, Param param) {
		CuboidMode mode = CuboidMode.getByName(param.getParam("mode"));
		boolean twoDimensional = param.getParam("two_dimensional", true);
		String world = param.getParam("world", Bukkit.getWorlds().get(0).getName());
		BlockLocation loc1 = new BlockLocation(world, param.getParam("loc1.x", 0), param.getParam("loc1.y", 0), param.getParam("loc1.z", 0));
		BlockLocation loc2 = new BlockLocation(world, param.getParam("loc2.x", 0), param.getParam("loc2.y", 0), param.getParam("loc2.z", 0));
		return new CuboidActivator(base, loc1, loc2, mode, twoDimensional);
	}
	
	public static CuboidActivator load(ActivatorBase base, ConfigurationSection cfg) {
		CuboidMode mode = CuboidMode.getByName(cfg.getString("mode"));
		boolean twoDimensional = cfg.getBoolean("two_dimensional");
		String world = cfg.getString("world");
		BlockLocation loc1 = new BlockLocation(world, cfg.getInt("loc1.x"), cfg.getInt("loc1.y"), cfg.getInt("loc1.z"));
		BlockLocation loc2 = new BlockLocation(world, cfg.getInt("loc2.x"), cfg.getInt("loc2.y"), cfg.getInt("loc2.z"));
		return new CuboidActivator(base, loc1, loc2, mode, twoDimensional);
	}
}
