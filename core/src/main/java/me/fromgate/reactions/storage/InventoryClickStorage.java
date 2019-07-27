/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.storage;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;


public class InventoryClickStorage extends RAStorage {
	@Getter @Setter private ItemStack item;
	@Getter private InventoryAction action;
	@Getter private ClickType clickType;
	@Getter private SlotType slotType;
	@Getter private InventoryType inventoryType;
	@Getter private int numberKey;
	@Getter private int slot;
	@Getter private String inventoryName;
	private InventoryView inventoryView;

	public InventoryClickStorage(Player p, InventoryAction action, ClickType clickType, Inventory inventory, SlotType
									slotType, ItemStack item, int numberKey, InventoryView inventoryView, int slot) {
		super(p, ActivatorType.INVENTORY_CLICK);
		this.inventoryName = inventoryView.getTitle();
		this.action = action;
		this.clickType = clickType;
		this.inventoryType = inventory.getType();
		this.slotType = slotType;
		this.item = item;
		this.numberKey = numberKey;
		this.slot = slot;
		this.inventoryView = inventoryView;
	}

	public Inventory getBottomInventory() {
		return this.inventoryView.getBottomInventory();
	}

}
