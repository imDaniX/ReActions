package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.ItemHeldEvent;
import me.fromgate.reactions.event.RAEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-11-11.
 */
public class ItemHeldActivator extends Activator {
	private int previousSlot;
	private int newSlot;
	private String itemNewStr;
	private String itemPrevStr;

	public ItemHeldActivator(String name, String param) {
		super(name, "activators");
		Param params = new Param(param);
		itemNewStr = params.getParam("itemnew", "");
		itemPrevStr = params.getParam("itemprev", "");
		newSlot = params.getParam("slotnew", 0) - 1;
		previousSlot = params.getParam("slotprev", 0) - 1;
	}

	public ItemHeldActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	@Override
	public boolean activate(RAEvent event) {
		if (!(event instanceof ItemHeldEvent)) return false;
		ItemHeldEvent ihe = (ItemHeldEvent) event;
		ItemStack itemNew = ihe.getNewItem();
		ItemStack itemPrev = ihe.getPreviousItem();
		if (!this.itemNewStr.isEmpty() && (!ItemUtil.compareItemStr(itemNew, this.itemNewStr)))
			return false;
		if (!this.itemPrevStr.isEmpty() && (!ItemUtil.compareItemStr(itemPrev, this.itemPrevStr)))
			return false;
		if (newSlot > -1 && newSlot != ihe.getNewSlot()) return false;
		if (previousSlot > -1 && previousSlot != ihe.getPreviousSlot()) return false;
		if (itemNew != null) {
			VirtualItem vi = ItemUtil.itemFromItemStack(itemNew);
			if (vi != null) {
				Variables.setTempVar("itemnew", vi.toString());
				Variables.setTempVar("itemnew-str", vi.toDisplayString());
			}
		}
		if (itemPrev != null) {
			VirtualItem vi = ItemUtil.itemFromItemStack(itemPrev);
			if (vi != null) {
				Variables.setTempVar("itemprev", vi.toString());
				Variables.setTempVar("itemprev-str", vi.toDisplayString());
			}
		}
		Variables.setTempVar("slotNew", Integer.toString(ihe.getNewSlot() + 1));
		Variables.setTempVar("slotPrev", Integer.toString(ihe.getPreviousSlot() + 1));
		return Actions.executeActivator(ihe.getPlayer(), this);
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return false;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("item-new", this.itemNewStr);
		cfg.set("item-prev", this.itemPrevStr);
		cfg.set("slot-new", this.newSlot + 1);
		cfg.set("slot-prev", this.previousSlot + 1);
	}

	@Override
	public void load(ConfigurationSection cfg) {
		this.itemNewStr = cfg.getString("item-new");
		this.itemPrevStr = cfg.getString("item-prev");
		this.newSlot = ((cfg.getString("slot-new").isEmpty()) ? 0 : Integer.parseInt(cfg.getString("slot-new"))) - 1;
		this.previousSlot = ((cfg.getString("slot-prev").isEmpty()) ? 0 : Integer.parseInt(cfg.getString("slot-prev"))) - 1;
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
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (");
		sb.append("itemnew:").append(itemNewStr.isEmpty() ? "-" : itemNewStr);
		sb.append(" itemprev:").append(itemPrevStr.isEmpty() ? "-" : itemPrevStr);
		sb.append(" slotnew:").append(newSlot + 1);
		sb.append(" slotprev:").append(previousSlot + 1);
		sb.append(")");
		return sb.toString();
	}
}
