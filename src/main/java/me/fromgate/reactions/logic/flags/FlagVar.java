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

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

public class FlagVar implements Flag {
    private final Type flagType;
    private final boolean personalVar;

    public FlagVar(Type flagType, boolean personalVar) {
        this.flagType = flagType;
        this.personalVar = personalVar;
    }

    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        Parameters params = new Parameters(param, "param-line");
        String var;
        String value;
        String playerName = this.personalVar && (player != null) ? player.getName() : "";


        if (params.isParamsExists("id")) {
            var = params.getParam("id", "");
            if (var.isEmpty()) return false;
            value = params.getParam("value", "");
            playerName = params.getParam("player", playerName);
        } else {
            String[] ln = params.getParam("param-line", "").split("/", 2);
            if (ln.length == 0) return false;
            var = ln[0];
            value = (ln.length > 1) ? ln[1] : "";
        }
        if (playerName.isEmpty() && this.personalVar) return false;
        switch (this.flagType) {
            case EXIST: // VAR_EXIST
                return Variables.existVar(playerName, var);
            case COMPARE: // VAR_COMPARE
                return Variables.compareVariable(playerName, var, value);
            case GREATER: // VAR_GREATER
                return Variables.cmpGreaterVar(playerName, var, value);
            case LOWER: // VAR_LOWER
                return Variables.cmpLowerVar(playerName, var, value);
            case MATCH: // VAR_MATCH
                return Variables.matchVar(playerName, var, value);
        }
        return false;
    }

    public enum Type {
        EXIST, COMPARE, GREATER, LOWER, MATCH
    }
}
