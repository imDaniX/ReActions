package me.fromgate.reactions.activators;

import me.fromgate.reactions.storages.PickupItemStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-09-04.
 */
public class PickupItemActivator extends Activator {

	private String itemStr;

	public PickupItemActivator(ActivatorBase base, String item) {
		super(base);
		this.itemStr = item;
	}

	@Override
	public boolean activate(Storage event) {
		PickupItemStorage pie = (PickupItemStorage) event;
		if (!checkItem(pie.getItem())) return false;
		return true;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("item", this.itemStr);
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
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("item:").append(this.itemStr);
		sb.append(")");
		return sb.toString();
	}

	public static PickupItemActivator create(ActivatorBase base, Param param) {
		String item = param.getParam("item", param.toString());
		return new PickupItemActivator(base, item);
	}

	public static PickupItemActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String item = cfg.getString("item", "");
		return new PickupItemActivator(base, item);
	}
}
