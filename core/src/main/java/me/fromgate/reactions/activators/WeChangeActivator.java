/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.storage.WeChangeStorage;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class WeChangeActivator extends Activator {
	private String blockType;
	private String region;

	public WeChangeActivator(String name, String param) {
		super(name, "activators");
		Param params = new Param(param);
		blockType = params.getParam("block-type");
		region = params.getParam("region", "");
		blockType();
	}

	public WeChangeActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	@Override
	public boolean activate(RAStorage event) {
		WeChangeStorage e = (WeChangeStorage) event;
		String type = e.getBlockType();
		Variables.setTempVar("blocktype", type);
		if (!checkBlockType(type)) return false;
		Variables.setTempVar("blocklocation", Locator.locationToString(e.getLocation()));

		if (!region.isEmpty() && !RaWorldGuard.isLocationInRegion(e.getLocation(), region)) return false;

		return Actions.executeActivator(e.getPlayer(), this);
	}

	private boolean checkBlockType(String blockType) {
		blockType();
		return blockType.isEmpty() || this.blockType.equalsIgnoreCase("ANY") || this.blockType.equalsIgnoreCase(blockType);
	}

	private void blockType() {
		String bType = blockType.toUpperCase();
		if (bType.isEmpty()) blockType = "ANY";
		else if (StringUtils.isNumeric(bType)) {
			blockType = Material.getMaterial(bType).name();
		} else if (!bType.equalsIgnoreCase("ANY") && Material.getMaterial(bType) != null)
			blockType = Material.getMaterial(bType).name();
		else blockType = "ANY";
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("block-type", this.blockType);
		cfg.set("region", this.region);
	}

	@Override
	public void load(ConfigurationSection cfg) {
		this.blockType = cfg.getString("block-type", "");
		this.region = cfg.getString("region", "");
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
		sb.append("block-type:").append(blockType.isEmpty() ? "ANY" : blockType.toUpperCase());
		sb.append(" region:").append(region.isEmpty() ? "-" : region.toUpperCase());
		sb.append(")");
		return sb.toString();
	}
}
