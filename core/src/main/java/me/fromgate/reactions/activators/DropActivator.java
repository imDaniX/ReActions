package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.DropEvent;
import me.fromgate.reactions.event.RAEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-05-01.
 */
public class DropActivator extends Activator {

	private String itemStr;

	public DropActivator(String name, String param) {
		super(name, "activators");
		Param params = new Param(param);
		this.itemStr = params.getParam("item");
	}

	public DropActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	@Override
	public boolean activate(RAEvent event) {
		if (!(event instanceof DropEvent)) return false;
		DropEvent de = (DropEvent) event;
		if (!checkItem(de.getItemStack())) return false;
		Variables.setTempVar("droplocation", Locator.locationToString(de.getPlayer().getLocation()));
		Variables.setTempVar("pickupDelay", Double.toString(de.getPickupDelay()));
		boolean result = Actions.executeActivator(de.getPlayer(), this);
		String pickupDelayStr = Variables.getTempVar("pickupDelay");
		if (Util.FLOAT.matcher(pickupDelayStr).matches()) de.setPickupDelay(Double.parseDouble(pickupDelayStr));
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
	public void load(ConfigurationSection cfg) {
		this.itemStr = cfg.getString("item", "");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.DROP;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	private boolean checkItem(ItemStack item) {
		if (this.itemStr.isEmpty()) return true;
		return ItemUtil.compareItemStr(item, this.itemStr, true);
	}
}
