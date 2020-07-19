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

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlagItem implements Flag {
    private final byte flagType;

    public FlagItem(byte flagType) {
        this.flagType = flagType;
    }

    @Override
    public boolean checkFlag(RaContext context, String itemStr) {
        Player player = context.getPlayer();
        switch (flagType) {
            case 0:
                ItemStack inHand = player.getInventory().getItemInMainHand();
                context.setTempVariable("item_amount", inHand == null ? "0" : String.valueOf(inHand.getAmount()));
                return ItemUtil.compareItemStr(inHand, itemStr, true);
            case 1:
                return hasItemInInventory(context, itemStr);
            case 2:
                return isItemWeared(player, itemStr);
            case 3:
                ItemStack inOffhand = player.getInventory().getItemInOffHand();
                context.setTempVariable("item_amount", inOffhand == null ? "0" : String.valueOf(inOffhand.getAmount()));
                return ItemUtil.compareItemStr(inOffhand, itemStr, true);
        }
        return false;
    }

    private boolean isItemWeared(Player player, String itemStr) {
        for (ItemStack armour : player.getInventory().getArmorContents())
            if (ItemUtil.compareItemStr(armour, itemStr)) return true;
        return false;
    }

    private boolean hasItemInInventory(RaContext context, String itemStr) {
        Player player = context.getPlayer();
        Param params = new Param(itemStr);

        if (!params.isParamsExists("slot", "item")) {
            int countAmount = ItemUtil.countItemsInInventory(player.getInventory(), itemStr);
            context.setTempVariable("item_amount", countAmount == 0 ? "0" : String.valueOf(countAmount));
            int amount = ItemUtil.getAmount(itemStr);
            return countAmount >= amount;
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
