package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.DropStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.Locator;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-05-01.
 */
public class DropActivator extends Activator {

	private final String itemStr;

	public DropActivator(ActivatorBase base, String itemStr) {
		super(base);
		this.itemStr = itemStr;
	}

	@Override
	public boolean activate(RAStorage event) {
		DropStorage de = (DropStorage) event;
		if (!checkItem(de.getItemStack())) return false;
		Variables.setTempVar("droplocation", Locator.locationToString(de.getPlayer().getLocation()));
		Variables.setTempVar("pickupDelay", Double.toString(de.getPickupDelay()));
		boolean result = Actions.executeActivator(de.getPlayer(), getBase());
		String pickupDelayStr = Variables.getTempVar("pickupDelay");
		if (Util.INT.matcher(pickupDelayStr).matches()) de.setPickupDelay(Integer.parseInt(pickupDelayStr));
		Param itemParam = new Param(Variables.getTempVar("item"));
		if (!itemParam.isEmpty()) {
			String itemType = itemParam.getParam("type", "0");
			if (itemType.equalsIgnoreCase("AIR") || itemType.equalsIgnoreCase("null") || itemType.equalsIgnoreCase("0") || itemType.isEmpty()) {
				de.setItemStack(new ItemStack(Material.getMaterial("AIR"), 1));
			} else {
				de.setItemStack(ItemUtil.parseItemStack(itemParam.getParam("param-line", "")));
			}
		}
		return result;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("item", this.itemStr);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.DROP;
	}

	private boolean checkItem(ItemStack item) {
		if (this.itemStr.isEmpty()) return true;
		return ItemUtil.compareItemStr(item, this.itemStr, true);
	}

	public static DropActivator create(ActivatorBase base, Param param) {
		String itemStr = param.getParam("item", param.toString());
		return new DropActivator(base, itemStr);
	}

	public static DropActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String itemStr = cfg.getString("item", "");
		return new DropActivator(base, itemStr);
	}
}
