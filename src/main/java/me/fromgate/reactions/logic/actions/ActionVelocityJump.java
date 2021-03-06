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
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.location.VelocityUtils;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ActionVelocityJump extends Action {

    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        Msg.logOnce("velocity-jump-warning", "&cWarning! VELOCITY_JUMP action is under construction. In next version of plugin it could be changed, renamed or removed!");
        String locStr = params.getString("loc", "");
        if (locStr.isEmpty()) return false;
        Location loc = LocationUtils.parseCoordinates(locStr);
        if (loc == null) return false;
        int jumpHeight = params.getInteger("jump", 5);
        Vector velocity = VelocityUtils.calculateVelocity(player.getLocation(), loc, jumpHeight);
        player.setVelocity(velocity);
        return false;
    }

}
