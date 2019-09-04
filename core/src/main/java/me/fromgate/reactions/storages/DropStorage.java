package me.fromgate.reactions.storages;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-05-01.
 */
public class DropStorage extends Storage {
	@Getter @Setter private ItemStack itemStack;
	@Getter @Setter private int pickupDelay;

	public DropStorage(Player p, Item item, int pickupDelay) {
		super(p, ActivatorType.DROP);
		this.itemStack = item.getItemStack();
		this.pickupDelay = pickupDelay;
	}
}
