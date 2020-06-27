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

package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemClickStorage extends Storage {
	@Getter private final boolean mainHand;
	@Getter private final ItemStack item;

	public ItemClickStorage(Player p, ItemStack item, boolean mainHand) {
		super(p, ActivatorType.ITEM_CLICK);
		this.item = item;
		this.mainHand = mainHand;
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		VirtualItem vItem = VirtualItem.fromItemStack(item);
		if(item != null) {
			tempVars.put("item", vItem.toString());
			tempVars.put("item-str", vItem.toDisplayString());
		}
		tempVars.put("hand", mainHand ? "MAIN" : "OFF");
	}

	@Override
	void defaultChangeables(Map<String, DataValue> changeables) {
		changeables.put(Storage.CANCEL_EVENT, new BooleanValue(false));
	}
}
