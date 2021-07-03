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

import lombok.AllArgsConstructor;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.logic.activity.flags.OldFlag;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class FlagVar implements OldFlag {
    private final Type flagType;
    private final boolean personalVar;

    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        Parameters params = Parameters.fromString(param, "param-line");
        String variableId;
        String value;
        String playerName = this.personalVar && (player != null) ? player.getName() : "";


        if (params.contains("id")) {
            variableId = params.getString("id", "");
            if (variableId.isEmpty()) return false;
            value = params.getString("value", "");
            playerName = params.getString("player", playerName);
        } else {
            String[] ln = params.getString("param-line", "").split("/", 2);
            if (ln.length == 0) return false;
            variableId = ln[0];
            value = (ln.length > 1) ? ln[1] : "";
        }
        if (playerName.isEmpty() && this.personalVar) return false;

        String variable = ReActions.getVariables().getVariable(playerName, variableId);
        if (variable == null)
            return false;

        switch (this.flagType) {
            case EXIST: // VAR_EXIST
                return true;

            case COMPARE: // VAR_COMPARE
                if (NumberUtils.isNumber(variable, value))
                    return Double.parseDouble(variable) == Double.parseDouble(value);
                return variable.equalsIgnoreCase(value);

            case GREATER: // VAR_GREATER
                return NumberUtils.isNumber(variable, value) && Double.parseDouble(variable) > Double.parseDouble(value);

            case LOWER: // VAR_LOWER
                return NumberUtils.isNumber(variable, value) && Double.parseDouble(variable) < Double.parseDouble(value);

            case MATCH: // VAR_MATCH
                return variable.matches(value);
        }
        return false;
    }

    public enum Type {
        EXIST, COMPARE, GREATER, LOWER, MATCH
    }
}
