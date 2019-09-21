package me.fromgate.reactions.activators;

import me.fromgate.reactions.storages.BlockBreakStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakActivator extends Activator implements Locatable {
	private final Material blockType;
	private final String blockLocation;

	private BlockBreakActivator(ActivatorBase base, Material block, String location) {
		super(base);
		this.blockType = block;
		this.blockLocation = location;
	}

	@Override
	public boolean activate(Storage event) {
		BlockBreakStorage bbe = (BlockBreakStorage) event;
		Block brokenBlock = bbe.getBlock();
		if (brokenBlock == null) return false;
		if (!isActivatorBlock(brokenBlock)) return false;
		return true;
	}

	private boolean checkLocations(Block block) {
		if (this.blockLocation.isEmpty()) return true;
		return this.isLocatedAt(block.getLocation());
	}

	private boolean isActivatorBlock(Block block) {
		if (this.blockType != null && blockType != block.getType()) return false;
		return checkLocations(block);
	}

	@Override
	public boolean isLocatedAt(Location l) {
		if (this.blockLocation.isEmpty()) return false;
		Location loc = LocationUtil.parseLocation(this.blockLocation, null);
		if (loc == null) return false;
		return l.getWorld().equals(loc.getWorld()) &&
				l.getBlockX() == loc.getBlockX() &&
				l.getBlockY() == loc.getBlockY() &&
				l.getBlockZ() == loc.getBlockZ();
	}

	@Override
	public boolean isLocatedAt(World world, int x, int y, int z) {
		return isLocatedAt(new Location(world, x, y, z));
	}

	@Override
	public void save(ConfigurationSection cfg) {
		if(blockType != null) cfg.set("block", this.blockType.name());
		cfg.set("location", this.blockLocation.isEmpty() ? null : this.blockLocation);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.BLOCK_BREAK;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("block:").append(blockType == null ? "-" : blockType);
		sb.append("; loc:").append(blockLocation.isEmpty() ? "-" : blockLocation);
		sb.append(")");
		return sb.toString();
	}

	public static BlockBreakActivator create(ActivatorBase base, Param param) {
		Material block = ItemUtil.getMaterial(param.getParam("block"));
		String loc = param.getParam("loc");
		return new BlockBreakActivator(base, block, loc);
	}

	public static BlockBreakActivator load(ActivatorBase base, ConfigurationSection cfg) {
		Material block = ItemUtil.getMaterial(cfg.getString("block"));
		String loc = cfg.getString("loc");
		return new BlockBreakActivator(base, block, loc);
	}
}
