package me.fromgate.reactions.storage;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakStorage extends RAStorage {
	@Getter private final Block block;
	@Getter @Setter private boolean dropItems;

	public BlockBreakStorage(Player p, Block block, boolean dropItems) {
		super(p, ActivatorType.BLOCK_BREAK);
		this.block = block;
		this.dropItems = dropItems;
	}

	public Location getBlockBreakLocation() {
		return block.getLocation();
	}

}
