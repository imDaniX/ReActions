package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.RAEvent;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// TODO: Cuboid flag
// TODO: Command to create cuboid activator
public class CuboidActivator extends Activator implements Locatable {
	private String world;
	private CuboidMode mode;
	private int xMin, yMin, zMin;
	private int xMax, yMax, zMax;
	private boolean twoDimensional;
	private Set<UUID> within = new HashSet<>();

	public CuboidActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	@Override
	public boolean activate(RAEvent event) {
		Player player = event.getPlayer();
		UUID id = player.getUniqueId();
		boolean inCuboid = checkInCuboid(player.getLocation(), true);
		switch(mode) {
			case CHECK:
				if(inCuboid)
					Actions.executeActivator(player, this);
				break;
			case ENTER:
				if(inCuboid && within.add(id))
					Actions.executeActivator(player, this);
				break;
			case LEAVE:
				if(!inCuboid && within.remove(id))
					Actions.executeActivator(player, this);
				break;
		}
		return false;
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return checkInCuboid(loc, false);
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
	public void load(ConfigurationSection cfg) {
		world = cfg.getString("world");
		twoDimensional = cfg.getBoolean("two_dimensional", false);
		mode = CuboidMode.getByName(cfg.getString("mode"));
		int[] pair = Util.sortedIntPair(cfg.getInt("loc1.x", 0), cfg.getInt("loc2.x", 0));
		xMin = pair[0]; xMax = pair[1];
		pair = Util.sortedIntPair(cfg.getInt("loc1.z", 0), cfg.getInt("loc2.z"));
		zMin = pair[0]; zMax = pair[1];
		if(!twoDimensional) {
			pair = Util.sortedIntPair(cfg.getInt("loc1.y"), cfg.getInt("loc2.y"));
			yMin = pair[0]; yMax = pair[1];
		}
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
				case "ENTER": return ENTER;
				case "LEAVE": return LEAVE;
				default: return CHECK;
			}
		}
	}
}
