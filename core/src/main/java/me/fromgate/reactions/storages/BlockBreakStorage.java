package me.fromgate.reactions.storages;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakStorage extends Storage {
	@Getter private final Block block;
	@Getter @Setter private boolean dropItems;

	public BlockBreakStorage(Player p, Block block, boolean dropItems) {
		super(p, ActivatorType.BLOCK_BREAK);
		this.block = block;
		this.dropItems = dropItems;
		setDefaults();
	}

	@Override
	Map<String, String> getTempVariables() {
		Map<String, String> tempVars = new HashMap<>();
		tempVars.put("blocklocation", LocationUtil.locationToString(block.getLocation()));
		tempVars.put("blocktype", block.getType().name());
		tempVars.put("block", ItemUtil.itemFromBlock(block).toString());
		return tempVars;
	}

	@Override
	Map<String, DataValue> getChangeables() {
		Map<String, DataValue> changeables = new HashMap<>();
		changeables.put("is_drop", new BooleanValue(dropItems));
		return changeables;
	}

	public Location getBlockBreakLocation() {
		return block.getLocation();
	}

}
