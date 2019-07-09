package me.fromgate.reactions.event;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakEvent extends RAEvent {
	private Block block;
	private Boolean isDropItems;

	public BlockBreakEvent(Player p, Block block, Boolean isDropItems) {
		super(p, ActivatorType.BLOCK_BREAK);
		this.block = block;
		this.isDropItems = isDropItems;
	}

	public Block getBlockBreak() {
		return this.block;
	}

	public Boolean isDropItems() {
		return this.isDropItems;
	}

	public void setDropItems(Boolean isDropItems) {
		this.isDropItems = isDropItems;
	}

	public Location getBlockBreakLocation() {
		return block.getLocation();
	}

}
