package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.PickupItemStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.Locator;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-09-04.
 */
public class PickupItemActivator extends Activator {

	private String itemStr;

	public PickupItemActivator(String name, String param) {
		super(name, "activators");
		Param params = new Param(param);
		this.itemStr = params.getParam("item");
	}

	public PickupItemActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	@Override
	public boolean activate(RAStorage event) {
		PickupItemStorage pie = (PickupItemStorage) event;
		if (!checkItem(pie.getItem())) return false;
		Variables.setTempVar("droplocation", Locator.locationToString(pie.getPlayer().getLocation()));
		Variables.setTempVar("pickupDelay", Double.toString(pie.getPickupDelay()));
		Variables.setTempVar("item", ItemUtil.itemToString(pie.getItem()));
		boolean result = Actions.executeActivator(pie.getPlayer(), this);
		String pickupDelayStr = Variables.getTempVar("pickupDelay");
		if (Util.FLOAT.matcher(pickupDelayStr).matches()) pie.setPickupDelay(Integer.parseInt(pickupDelayStr));
		Param itemParam = new Param(Variables.getTempVar("item"));
		if (!itemParam.isEmpty()) {
			String itemType = itemParam.getParam("type", "0");
			if (itemType.equalsIgnoreCase("AIR") || itemType.equalsIgnoreCase("null") || itemType.equalsIgnoreCase("0") || itemType.isEmpty()) {
				pie.setItem(new ItemStack(Material.getMaterial("AIR"), 1));
			} else {
				pie.setItem(ItemUtil.parseItemStack(itemParam.getParam("param-line", "")));
			}
		}
		return result;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("item", this.itemStr);
	}

	@Override
	public void load(ConfigurationSection cfg) {
		this.itemStr = cfg.getString("item", "");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.PICKUP_ITEM;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	private boolean checkItem(ItemStack item) {
		if (this.itemStr.isEmpty()) return true;
		return ItemUtil.compareItemStr(item, this.itemStr, true);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (");
		sb.append("item:").append(this.itemStr);
		sb.append(")");
		return sb.toString();
	}
}
