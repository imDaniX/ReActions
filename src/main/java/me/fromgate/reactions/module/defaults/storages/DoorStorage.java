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

package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.DoorActivator;
import me.fromgate.reactions.util.BlockUtils;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DoorStorage extends Storage {
    public static final String DOOR_LOCATION = "door_loc";

    private final Block doorBlock;

    public DoorStorage(Player p, Block block) {
        super(p);
        this.doorBlock = block;
    }

    public boolean isDoorOpened() {
        return BlockUtils.isOpen(doorBlock);
    }

    public Location getDoorLocation() {
        return doorBlock.getLocation();
    }

    @Override
    public Class<? extends Activator> getType() {
        return DoorActivator.class;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        tempVars.put(DOOR_LOCATION, LocationUtils.locationToString(doorBlock));
        return tempVars;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return MapBuilder.single(CANCEL_EVENT, new BooleanValue(false));
    }
}
