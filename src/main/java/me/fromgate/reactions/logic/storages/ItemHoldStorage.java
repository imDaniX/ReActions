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

import lombok.Getter;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ItemHoldStorage extends Storage {

    private final boolean mainHand;
    private final ItemStack item;

    public ItemHoldStorage(Player p, ItemStack item, boolean mainHand) {
        super(p, ActivatorType.ITEM_HOLD);
        this.item = item;
        this.mainHand = mainHand;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        VirtualItem vItem = VirtualItem.fromItemStack(item);
        if (item != null) {
            tempVars.put("item", vItem.toString());
            tempVars.put("item-str", vItem.toDisplayString());
        }
        return tempVars;
    }
}