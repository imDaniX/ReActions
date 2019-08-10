/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.storage.WeChangeStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.Locator;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class WeChangeActivator extends Activator {
	private final Material blockType;
	private final String region;

	public WeChangeActivator(ActivatorBase base, Material blockType, String region) {
		super(base);
		this.blockType = blockType;
		this.region = region;
	}

	@Override
	public boolean activate(RAStorage event) {
		WeChangeStorage e = (WeChangeStorage) event;
		if (!checkBlockType(e.getBlockType())) return false;
		Variables.setTempVar("blocktype", e.getBlockType().name());
		Variables.setTempVar("blocklocation", Locator.locationToString(e.getLocation()));

		if (!region.isEmpty() && !RaWorldGuard.isLocationInRegion(e.getLocation(), region)) return false;

		return Actions.executeActivator(e.getPlayer(), this);
	}

	private boolean checkBlockType(Material check) {
		return blockType == null || blockType == check;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("block-type", this.blockType);
		cfg.set("region", this.region);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.WE_CHANGE;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (");
		sb.append("block-type:").append(blockType!=null ? "ANY" : blockType);
		sb.append(" region:").append(region.isEmpty() ? "-" : region.toUpperCase());
		sb.append(")");
		return sb.toString();
	}

	public static WeChangeActivator create(ActivatorBase base, Param param) {
		Material blockType = ItemUtil.getMaterial(param.getParam("blocktype"));
		String region = param.getParam("region", "");
		return new WeChangeActivator(base, blockType, region);
	}

	public static WeChangeActivator load(ActivatorBase base, ConfigurationSection cfg) {
		Material blockType = ItemUtil.getMaterial(cfg.getString("block-type").toUpperCase());
		String region = cfg.getString("region", "");
		return new WeChangeActivator(base, blockType, region);
	}
}
