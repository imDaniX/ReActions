package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemConsumeStorage extends RAStorage {

	private ItemStack item;
	private final boolean mainHand;

	public ItemConsumeStorage(Player p, ItemStack item, boolean mainHand) {
		super(p, ActivatorType.ITEM_CONSUME);
		this.item = item;
		this.mainHand = mainHand;
	}

	public ItemStack getItem() {
		return item;
	}

	public boolean isMainHand() {
		return mainHand;
	}

}
