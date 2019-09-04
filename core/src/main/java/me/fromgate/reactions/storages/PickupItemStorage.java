package me.fromgate.reactions.storages;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-09-04.
 */
public class PickupItemStorage extends Storage {
	@Getter @Setter private ItemStack item;
	@Getter @Setter private int pickupDelay;

	public PickupItemStorage(Player p, Item item, int pickupDelay) {
		super(p, ActivatorType.PICKUP_ITEM);
		this.item = item.getItemStack();
		this.pickupDelay = pickupDelay;
	}
}
