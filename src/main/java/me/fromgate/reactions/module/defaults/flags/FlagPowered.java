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

import me.fromgate.reactions.logic.activity.flags.OldFlag;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;

public class FlagPowered implements OldFlag {

    @Override
    public boolean checkFlag(RaContext context, String param) {
        Parameters params = Parameters.fromString(param);
        String locStr = params.contains("loc") ? params.getString("loc", "") : param;
        if (locStr.isEmpty()) return false;
        Location loc = LocationUtils.parseLocation(locStr, null);
        if (loc == null) return false;
        Block b = loc.getBlock();
        BlockData data = b.getBlockData();
        if (data instanceof Powerable)
            return ((Powerable) data).isPowered();
        return b.isBlockIndirectlyPowered();
    }

}
