package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.BlockBreakStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.Locator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakActivator extends Activator implements Locatable {
	private Material blockType;
	private String blockLocation;

	public BlockBreakActivator(String name, Block targetBlock, String param) {
		super(name, "activators");
		this.blockLocation = "";
		this.blockType = null;
		Param params = new Param(param);
		if (targetBlock != null) {
			this.blockLocation = Locator.locationToString(targetBlock.getLocation());
			this.blockType = targetBlock.getType();
		}
		String bt = params.getParam("block", "");
		if (this.blockType == null || this.blockType == Material.AIR || !bt.isEmpty() && !this.blockType.name().equalsIgnoreCase(bt)) {
			this.blockType = ItemUtil.getMaterial(bt);
			this.blockLocation = params.getParam("loc", "");
		}
	}

	public BlockBreakActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	@Override
	public boolean activate(RAStorage event) {
		BlockBreakStorage bbe = (BlockBreakStorage) event;
		Block brokenBlock = bbe.getBlockBreak();
		if (brokenBlock == null) return false;
		if (!isActivatorBlock(brokenBlock)) return false;
		Variables.setTempVar("blocklocation", Locator.locationToString(bbe.getBlockBreakLocation()));
		Variables.setTempVar("blocktype", brokenBlock.getType().name());
		Variables.setTempVar("block", ItemUtil.itemFromBlock(brokenBlock).toString());

		Variables.setTempVar("is_drop", bbe.isDropItems().toString());
		boolean result = Actions.executeActivator(bbe.getPlayer(), this);
		String isDropItem = Variables.getTempVar("is_drop");
		if (isDropItem.equalsIgnoreCase("true") || isDropItem.equalsIgnoreCase("false")) {
			bbe.setDropItems(Boolean.parseBoolean(isDropItem));
		}
		return result;
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
		Location loc = Locator.parseLocation(this.blockLocation, null);
		if (loc == null) return false;
		return l.getWorld().equals(loc.getWorld()) &&
				l.getBlockX() == loc.getBlockX() &&
				l.getBlockY() == loc.getBlockY() &&
				l.getBlockZ() == loc.getBlockZ();
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("block", this.blockType.name());
		cfg.set("location", this.blockLocation.isEmpty() ? null : this.blockLocation);
	}

	@Override
	public void load(ConfigurationSection cfg) {
		this.blockType = ItemUtil.getMaterial(cfg.getString("block", ""));
		this.blockLocation = cfg.getString("location", "");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.BLOCK_BREAK;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (");
		sb.append("block:").append(blockType == null ? "-" : blockType);
		sb.append(" loc:").append(blockLocation.isEmpty() ? "-" : blockLocation);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
