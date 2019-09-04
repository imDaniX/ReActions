package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-11-11.
 */
public class ItemHeldStorage extends Storage {
	@Getter private final int newSlot;
	@Getter private final int previousSlot;
	@Getter private final ItemStack newItem;
	@Getter private final ItemStack previousItem;

	public ItemHeldStorage(Player player, int newSlot, int previousSlot) {
		super(player, ActivatorType.ITEM_HELD);
		this.newSlot = newSlot;
		this.previousSlot = previousSlot;
		this.newItem = this.getPlayer().getInventory().getItem(newSlot);
		this.previousItem = this.getPlayer().getInventory().getItem(previousSlot);
	}
}
