package me.fromgate.reactions.activators;

import me.fromgate.reactions.storages.ItemHeldStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-11-11.
 */
public class ItemHeldActivator extends Activator {
	private final int previousSlot;
	private final int newSlot;
	private final String itemNewStr;
	private final String itemPrevStr;

	public ItemHeldActivator(ActivatorBase base, String itemPrevStr, String itemNewStr, int previousSlot, int newSlot) {
		super(base);
		this.itemNewStr = itemNewStr;
		this.itemPrevStr = itemPrevStr;
		this.previousSlot = previousSlot;
		this.newSlot = newSlot;
	}

	@Override
	public boolean activate(Storage event) {
		ItemHeldStorage ihe = (ItemHeldStorage) event;
		ItemStack itemNew = ihe.getNewItem();
		ItemStack itemPrev = ihe.getPreviousItem();
		if (!this.itemNewStr.isEmpty() && (!ItemUtil.compareItemStr(itemNew, this.itemNewStr)))
			return false;
		if (!this.itemPrevStr.isEmpty() && (!ItemUtil.compareItemStr(itemPrev, this.itemPrevStr)))
			return false;
		if (newSlot > -1 && newSlot != ihe.getNewSlot()) return false;
		if (previousSlot > -1 && previousSlot != ihe.getPreviousSlot()) return false;
		return true;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("item-new", this.itemNewStr);
		cfg.set("item-prev", this.itemPrevStr);
		cfg.set("slot-new", this.newSlot + 1);
		cfg.set("slot-prev", this.previousSlot + 1);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.ITEM_HELD;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("itemnew:").append(itemNewStr.isEmpty() ? "-" : itemNewStr);
		sb.append(" itemprev:").append(itemPrevStr.isEmpty() ? "-" : itemPrevStr);
		sb.append(" slotnew:").append(newSlot + 1);
		sb.append(" slotprev:").append(previousSlot + 1);
		sb.append(")");
		return sb.toString();
	}

	public static ItemHeldActivator create(ActivatorBase base, Param param) {
		String itemNewStr = param.getParam("itemnew", "");
		String itemPrevStr = param.getParam("itemprev", "");
		int newSlot = param.getParam("slotnew", 1);
		int previousSlot = param.getParam("slotprev", 1);
		return new ItemHeldActivator(base, itemPrevStr, itemNewStr, --newSlot, --previousSlot);
	}

	public static ItemHeldActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String itemNewStr = cfg.getString("item-new");
		String itemPrevStr = cfg.getString("item-prev");
		int newSlot = cfg.getInt("slot-new", 1);
		int previousSlot = cfg.getInt("slot-prev", 1);
		return new ItemHeldActivator(base, itemPrevStr, itemNewStr, --newSlot, --previousSlot);
	}
}
