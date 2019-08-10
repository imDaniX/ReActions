package me.fromgate.reactions.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Openable;

import java.util.HashSet;
import java.util.Set;

/**
 * Some helpful methods related to blocks to minify size of code
 */
public class BlockUtil {
	private final static Set<Material> PLATES = new HashSet<>();
	static {
		for(Material mat : Material.values())
			if(mat.name().endsWith("PRESSURE_PLATE")) PLATES.add(mat);
	}

	public static BlockFace getFaceByName(String name) {
		for(BlockFace face : BlockFace.values())
			if(face.name().equalsIgnoreCase(name)) return face;
		return null;
	}

	public static boolean isPlate(Block block) {
		return PLATES.contains(block.getType());
	}

	public static boolean isSign(Block block) {
		try {return Tag.SIGNS.isTagged(block.getType());}
		catch(NoSuchFieldError e) {return block.getType().name().contains("SIGN");}
	}

	public static boolean isSameBlock(Location loc1, Location loc2) {
		return loc1.getWorld().equals(loc2.getWorld()) && !(loc1.getBlockX() != loc2.getX()) && !(loc1.getBlockZ() != loc2.getZ()) && !(loc1.getBlockY() != loc2.getY());
	}

	public static boolean setOpen(Block b, boolean open) {
		if (isOpenable(b)) {
			Openable om = (Openable) b.getBlockData();
			om.setOpen(open);
			b.setBlockData(om);
			return true;
		}
		return false;
	}

	public static boolean isOpen(Block b) {
		if (isOpenable(b)) {
			Openable om = (Openable) b.getBlockData();
			return om.isOpen();
		}
		return false;
	}

	public static Block getDoorBottomBlock(Block block) {
		if (Tag.DOORS.isTagged(block.getType())) {
			Block bottomBlock = block.getRelative(BlockFace.DOWN);
			if (Tag.DOORS.isTagged(bottomBlock.getType()))
				return bottomBlock;
		}
		return block;
	}

	public static boolean isOpenable(Block b) {
		return b.getBlockData() instanceof Openable;
	}
}
