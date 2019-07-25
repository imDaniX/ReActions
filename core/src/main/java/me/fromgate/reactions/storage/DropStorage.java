package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-05-01.
 */
public class DropStorage extends RAStorage {
	private ItemStack itemStack;
	private int pickupDelay;

	public DropStorage(Player p, Item item, int pickupDelay) {
		super(p, ActivatorType.DROP);
		this.itemStack = item.getItemStack();
		this.pickupDelay = pickupDelay;
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public int getPickupDelay() {
		return this.pickupDelay;
	}

	public void setPickupDelay(int pickupDelay) {
		this.pickupDelay = pickupDelay;
	}

}
