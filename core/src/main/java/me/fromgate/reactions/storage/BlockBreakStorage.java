package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakStorage extends RAStorage {
	private final Block block;
	private boolean isDropItems;

	public BlockBreakStorage(Player p, Block block, boolean isDropItems) {
		super(p, ActivatorType.BLOCK_BREAK);
		this.block = block;
		this.isDropItems = isDropItems;
	}

	public Block getBlockBreak() {
		return this.block;
	}

	public boolean isDropItems() {
		return this.isDropItems;
	}

	public void setDropItems(boolean isDropItems) {
		this.isDropItems = isDropItems;
	}

	public Location getBlockBreakLocation() {
		return block.getLocation();
	}

}
