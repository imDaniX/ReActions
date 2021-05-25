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

package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.logic.activity.actions.OldAction;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;

public class ActionExecUnstop extends OldAction {

    @Override
    public boolean execute(RaContext context, Parameters params) {
        // TODO Custom ActivatorType to handle exec stopping
        Msg.logOnce("execunstopnotworking", "Sorry, but action EXEC_UNSTOP doesn't work yet.");
        return true;
        /*Player p = context.getPlayer();
        String player = params.getString("player", (p == null ? "" : p.getName()));
        if (player.isEmpty()) return false;
        String activator = params.getString("activator", "");
        if (activator.isEmpty()) return false;
        setMessageParam(activator);
        return ReActions.getActivators().isStopped(player, activator, true);*/
    }

}
