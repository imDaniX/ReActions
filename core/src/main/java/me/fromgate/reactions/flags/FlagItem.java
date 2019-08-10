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

package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlagItem implements Flag {
	private final byte flagType;

	public FlagItem(byte flagType) {
		this.flagType = flagType;
	}

	@Override
	public boolean checkFlag(Player player, String itemStr) {
		switch (flagType) {
			case 0:
				ItemStack inHand = player.getInventory().getItemInMainHand();
				Variables.setTempVar("item_amount", inHand == null ? "0" : String.valueOf(inHand.getAmount()));
				return ItemUtil.compareItemStr(inHand, itemStr, true);
			case 1:
				return hasItemInInventory(player, itemStr);
			case 2:
				return isItemWeared(player, itemStr);
			case 3:
				ItemStack inOffhand = player.getInventory().getItemInOffHand();
				Variables.setTempVar("item_amount", inOffhand == null ? "0" : String.valueOf(inOffhand.getAmount()));
				return ItemUtil.compareItemStr(inOffhand, itemStr, true);
		}
		return false;
	}

	private boolean isItemWeared(Player player, String itemStr) {
		for (ItemStack armour : player.getInventory().getArmorContents())
			if (ItemUtil.compareItemStr(armour, itemStr)) return true;
		return false;
	}

	private boolean hasItemInInventory(Player player, String itemStr) {
		Param params = new Param(itemStr);

		if (!params.isParamsExists("slot", "item")) {
			return ItemUtil.hasItemInInventory(player, itemStr);
		}

		String slotStr = params.getParam("slot", "");
		if (slotStr.isEmpty()) return false;
		int slotNum = Util.isInteger(slotStr) ? Integer.parseInt(slotStr) : -1;
		if (slotNum >= player.getInventory().getSize()) return false;

		VirtualItem vi = null;

		if (slotNum < 0) {
			if (slotStr.equalsIgnoreCase("helm") || slotStr.equalsIgnoreCase("helmet"))
				vi = VirtualItem.fromItemStack(player.getInventory().getHelmet());
			else if (slotStr.equalsIgnoreCase("chestplate") || slotStr.equalsIgnoreCase("chest"))
				vi = VirtualItem.fromItemStack(player.getInventory().getChestplate());
			else if (slotStr.equalsIgnoreCase("Leggings") || slotStr.equalsIgnoreCase("Leg"))
				vi = VirtualItem.fromItemStack(player.getInventory().getLeggings());
			else if (slotStr.equalsIgnoreCase("boot") || slotStr.equalsIgnoreCase("boots"))
				VirtualItem.fromItemStack(player.getInventory().getBoots());
		} else vi = VirtualItem.fromItemStack(player.getInventory().getItem(slotNum));

		// vi = VirtualItem.fromItemStack(player.getInventoryType().getItem(slotNum));

		if (vi == null) return false;

		return vi.compare(itemStr);
	}

}
