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

import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.location.Teleporter;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ActionTp extends Action {

    @Override
    public boolean execute(RaContext context, Parameters params) {
        Location loc = teleportPlayer(context, params);
        if (loc != null) this.setMessageParam(LocationUtils.locationToStringFormatted(loc));
        return (loc != null);
    }

    private Location teleportPlayer(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        Location loc;
        int radius = 0;
        if (params.isEmpty()) return null;
        if (params.containsEvery("param")) {
            loc = LocationUtils.parseLocation(params.getString("param", ""), player.getLocation());
        } else {
            loc = LocationUtils.parseLocation(params.getString("loc", ""), player.getLocation());
            radius = params.getInteger("radius", 0);
        }
        boolean land = params.getBoolean("land", true);

        if (loc != null) {
            if (radius > 0) loc = LocationUtils.getRadiusLocation(loc, radius, land);
            if (Cfg.centerTpCoords) {
                loc.setX(loc.getBlockX() + 0.5);
                loc.setZ(loc.getBlockZ() + 0.5);
            }
            try {
                while (!loc.getChunk().isLoaded()) loc.getChunk().load();
            } catch (Exception ignore) {
            }

            context.setVariable("loc-from", LocationUtils.locationToString(player.getLocation()));
            context.setVariable("loc-from-str", LocationUtils.locationToStringFormatted(player.getLocation()));
            context.setVariable("loc-to", LocationUtils.locationToString(loc));
            context.setVariable("loc-to-str", LocationUtils.locationToStringFormatted(loc));
            Teleporter.teleport(player, loc);
            String playeffect = params.getString("effect", "");
            if (!playeffect.isEmpty()) {
                if (playeffect.equalsIgnoreCase("smoke") && (!params.containsEvery("wind"))) params.put("wind", "all");
            }
        }
        return loc;
    }

}
