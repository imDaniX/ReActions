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
import org.bukkit.entity.Player;

public class ActionExecStop extends OldAction {

    @Override
    protected boolean execute(RaContext context, Parameters params) {
        Player p = context.getPlayer();
        String player = params.getString("player", (p == null ? "" : p.getName()));
        if (player.isEmpty()) return false;
        String activator = params.getString("activator", "");
        if (activator.isEmpty()) return false;
        // TODO Custom ActivatorType to handle exec stopping
        //ReActions.getActivators().stopExec(player, activator);
        Msg.logOnce("execstopnotworking", "Sorry, but action EXEC_STOP doesn't work yet.");
        setMessageParam(activator);
        return true;
    }

}
