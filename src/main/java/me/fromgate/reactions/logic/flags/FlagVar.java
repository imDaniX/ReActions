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

package me.fromgate.reactions.logic.flags;

import lombok.AllArgsConstructor;
import me.fromgate.reactions.VariablesManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class FlagVar implements Flag {
    private final Type flagType;
    private final boolean personalVar;

    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        Parameters params = Parameters.fromString(param, "param-line");
        String var;
        String value;
        String playerName = this.personalVar && (player != null) ? player.getName() : "";


        if (params.contains("id")) {
            var = params.getString("id", "");
            if (var.isEmpty()) return false;
            value = params.getString("value", "");
            playerName = params.getString("player", playerName);
        } else {
            String[] ln = params.getString("param-line", "").split("/", 2);
            if (ln.length == 0) return false;
            var = ln[0];
            value = (ln.length > 1) ? ln[1] : "";
        }
        if (playerName.isEmpty() && this.personalVar) return false;
        switch (this.flagType) {
            case EXIST: // VAR_EXIST
                return VariablesManager.getInstance().existVar(playerName, var);
            case COMPARE: // VAR_COMPARE
                return VariablesManager.getInstance().compareVariable(playerName, var, value);
            case GREATER: // VAR_GREATER
                return VariablesManager.getInstance().cmpGreaterVar(playerName, var, value);
            case LOWER: // VAR_LOWER
                return VariablesManager.getInstance().cmpLowerVar(playerName, var, value);
            case MATCH: // VAR_MATCH
                return VariablesManager.getInstance().matchVar(playerName, var, value);
        }
        return false;
    }

    public enum Type {
        EXIST, COMPARE, GREATER, LOWER, MATCH
    }
}
