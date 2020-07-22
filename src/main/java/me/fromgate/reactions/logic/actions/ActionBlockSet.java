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

package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class ActionBlockSet extends Action {

    @Override
    public boolean execute(RaContext context, Parameters params) {
        //String istr = params.getParam("block", "");
        boolean phys = params.getParam("physics", false);
        boolean drop = params.getParam("drop", false);
        Parameters itemParam = new Parameters(params.getParam("block", "AIR"), "type");
        ItemStack item = null;
        if (!itemParam.getParam("type", "AIR").equalsIgnoreCase("air")) {
            item = VirtualItem.fromMap(itemParam.getMap());
            if ((item == null) || ((!item.getType().isBlock()))) {
                Msg.logOnce("wrongblock" + params.getParam("block"), "Failed to execute action BLOCK_FILL. Wrong block " + params.getParam("block"));
                return false;
            }
        }

        Location loc = LocationUtils.parseLocation(params.getParam("loc", ""), null);
        if (loc == null) return false;
        Block b = loc.getBlock();

        if (b.getType() != Material.AIR && drop) b.breakNaturally();

        if (item != null) {
            b.setType(item.getType());
            //b.setBlockData(item.getData(),phys);
            //b.setTypeIdAndData(item.getTypeId(), item.getData().getData(), phys);
        } else b.setType(Material.AIR, phys);
        setMessageParam(ItemUtils.itemToString(item));
        return true;
    }

}
