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

package me.fromgate.reactions.logic.storages;

import me.fromgate.reactions.logic.ActivatorType;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemWearStorage extends Storage {

    public ItemWearStorage(Player p) {
        super(p, ActivatorType.ITEM_WEAR);
    }

    public boolean isItemWeared(String itemStr) {
        for (ItemStack armour : getPlayer().getInventory().getArmorContents())
            if (ItemUtil.compareItemStr(armour, itemStr)) return true;
        return false;
    }

}