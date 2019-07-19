package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-11-11.
 */
public class ItemHeldStorage extends RAStorage {
	private int newSlot;
	private int previousSlot;

	public ItemHeldStorage(Player player, int newSlot, int previousSlot) {
		super(player, ActivatorType.ITEM_HELD);
		this.newSlot = newSlot;
		this.previousSlot = previousSlot;
	}

	public int getNewSlot() {
		return newSlot;
	}

	public int getPreviousSlot() {
		return previousSlot;
	}

	public ItemStack getNewItem() {
		return ItemUtil.itemFromItemStack(this.getPlayer().getInventory().getItem(getNewSlot()));
	}

	public ItemStack getPreviousItem() {
		return ItemUtil.itemFromItemStack(this.getPlayer().getInventory().getItem(getPreviousSlot()));
	}
}
