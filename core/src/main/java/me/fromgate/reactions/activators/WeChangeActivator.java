/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.storages.WeChangeStorage;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.parameter.Param;
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
	public boolean activate(Storage event) {
		WeChangeStorage e = (WeChangeStorage) event;
		if (!checkBlockType(e.getBlockType())) return false;
		Variables.setTempVar("blocktype", e.getBlockType().name());
		Variables.setTempVar("blocklocation", LocationUtil.locationToString(e.getLocation()));

		if (!region.isEmpty() && !RaWorldGuard.isLocationInRegion(e.getLocation(), region)) return false;

		return Actions.executeActivator(e.getPlayer(), getBase());
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
		StringBuilder sb = new StringBuilder(super.toString());
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
