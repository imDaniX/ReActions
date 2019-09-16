package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakStorage extends Storage {
	@Getter private final Block block;
	@Getter private final boolean dropItems;

	public BlockBreakStorage(Player p, Block block, boolean dropItems) {
		super(p, ActivatorType.BLOCK_BREAK);
		this.block = block;
		this.dropItems = dropItems;
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		tempVars.put("blocklocation", LocationUtil.locationToString(block.getLocation()));
		tempVars.put("blocktype", block.getType().name());
		tempVars.put("block", ItemUtil.itemFromBlock(block).toString());
	}

	@Override
	void defaultChangeables(Map<String, DataValue> changeables) {
		changeables.put(Storage.CANCEL_EVENT, new BooleanValue(false));
		changeables.put("is_drop", new BooleanValue(dropItems));
	}

	public Location getBlockBreakLocation() {
		return block.getLocation();
	}

}
