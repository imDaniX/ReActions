package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemConsumeStorage extends RAStorage {

	@Getter private ItemStack item;
	@Getter private final boolean mainHand;

	public ItemConsumeStorage(Player p, ItemStack item, boolean mainHand) {
		super(p, ActivatorType.ITEM_CONSUME);
		this.item = item;
		this.mainHand = mainHand;
	}
}
