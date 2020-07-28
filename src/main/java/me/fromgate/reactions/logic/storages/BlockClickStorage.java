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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.location.LocationUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Map;

@Getter
@FieldDefaults(makeFinal=true,level=AccessLevel.PRIVATE)
public class BlockClickStorage extends Storage {

    Block block;
    boolean leftClick;

    public BlockClickStorage(Player p, Block block, boolean leftClick) {
        super(p, ActivatorType.BLOCK_CLICK);
        this.block = block;
        this.leftClick = leftClick;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("blocklocation", LocationUtils.locationToString(block.getLocation()));
        tempVars.put("blocktype", block.getType().name());
        tempVars.put("block", ItemUtils.itemFromBlock(block).toString());
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
    }
}
