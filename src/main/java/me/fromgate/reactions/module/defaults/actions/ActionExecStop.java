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

import me.fromgate.reactions.logic.activity.actions.Action;
import me.fromgate.reactions.util.Alias;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Alias("STOP")
public class ActionExecStop extends Action {

    @Override
    protected boolean execute(RaContext context, Parameters params) {
        // TODO Custom ActivatorType to handle exec stopping
        Msg.logOnce("execstopnotworking", "Sorry, but action EXEC_STOP doesn't work yet.");
        if (true) return true;
        Player p = context.getPlayer();
        String player = params.getString("player", (p == null ? "" : p.getName()));
        if (player.isEmpty()) return false;
        String activator = params.getString("activator", "");
        if (activator.isEmpty()) return false;
        //ReActions.getActivators().stopExec(player, activator);
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "EXEC_STOP";
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

}
