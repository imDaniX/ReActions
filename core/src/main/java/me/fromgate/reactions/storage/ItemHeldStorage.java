package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-11-11.
 */
public class ItemHeldStorage extends RAStorage {
	private final int newSlot;
	private final int previousSlot;
	private final ItemStack newItem;
	private final ItemStack previousItem;


	public ItemHeldStorage(Player player, int newSlot, int previousSlot) {
		super(player, ActivatorType.ITEM_HELD);
		this.newSlot = newSlot;
		this.previousSlot = previousSlot;
		this.newItem = this.getPlayer().getInventory().getItem(newSlot);
		this.previousItem = this.getPlayer().getInventory().getItem(previousSlot);
	}

	public int getNewSlot() {
		return newSlot;
	}

	public int getPreviousSlot() {
		return previousSlot;
	}

	public ItemStack getNewItem() {
		return newItem;
	}

	public ItemStack getPreviousItem() {
		return previousItem;
	}
}
