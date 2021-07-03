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

package me.fromgate.reactions.module.defaults.flags;

import lombok.AllArgsConstructor;
import me.fromgate.reactions.logic.activity.flags.OldFlag;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

@AllArgsConstructor
public class FlagItem implements OldFlag {
    private final Type flagType;

    @Override
    public boolean checkFlag(RaContext context, String itemStr) {
        Player player = context.getPlayer();
        switch (flagType) {
            case HAND:
                ItemStack inHand = player.getInventory().getItemInMainHand();
                context.setVariable("item_amount", String.valueOf(inHand.getAmount()));
                return ItemUtils.compareItemStr(inHand, itemStr, true);
            case INVENTORY:
                return hasItemInInventory(context, itemStr);
            case WEAR:
                return isItemWeared(player, itemStr);
            case OFFHAND:
                ItemStack inOffhand = player.getInventory().getItemInOffHand();
                context.setVariable("item_amount", String.valueOf(inOffhand.getAmount()));
                return ItemUtils.compareItemStr(inOffhand, itemStr, true);
        }
        return false;
    }

    private boolean isItemWeared(Player player, String itemStr) {
        for (ItemStack armour : player.getInventory().getArmorContents())
            if (ItemUtils.compareItemStr(armour, itemStr)) return true;
        return false;
    }

    private boolean hasItemInInventory(RaContext context, String itemStr) {
        Player player = context.getPlayer();
        Parameters params = Parameters.fromString(itemStr);

        if (!params.containsEvery("slot", "item")) {
            int countAmount = ItemUtils.countItemsInInventory(player.getInventory(), itemStr);
            context.setVariable("item_amount", countAmount == 0 ? "0" : String.valueOf(countAmount));
            int amount = ItemUtils.getAmount(itemStr);
            return countAmount >= amount;
        }

        String slotStr = params.getString("slot", "");
        if (slotStr.isEmpty()) return false;
        int slotNum = NumberUtils.isInteger(slotStr) ? Integer.parseInt(slotStr) : -1;
        if (slotNum >= player.getInventory().getSize()) return false;

        VirtualItem vi = null;

        if (slotNum < 0) {
            switch (slotStr.toLowerCase(Locale.ENGLISH)) {
                case "helm", "helmet" -> vi = VirtualItem.fromItemStack(player.getInventory().getHelmet());
                case "chest", "chestplate" -> vi = VirtualItem.fromItemStack(player.getInventory().getChestplate());
                case "legs", "leggings" -> vi = VirtualItem.fromItemStack(player.getInventory().getLeggings());
                case "boot", "boots" -> vi = VirtualItem.fromItemStack(player.getInventory().getBoots());
            }
        } else vi = VirtualItem.fromItemStack(player.getInventory().getItem(slotNum));

        // vi = VirtualItem.fromItemStack(player.getInventoryType().getItem(slotNum));

        if (vi == null) return false;

        return vi.compare(itemStr);
    }

    public enum Type {
        HAND, INVENTORY, WEAR, OFFHAND
    }
}
