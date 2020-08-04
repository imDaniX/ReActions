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

package me.fromgate.reactions.activators.actions;

import me.fromgate.reactions.VariablesManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

public class ActionVar extends Action {
    private final Type actType;
    private final boolean personalVar;

    public ActionVar(Type actType, boolean personalVar) {
        this.actType = actType;
        this.personalVar = personalVar;
    }

    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player p = context.getPlayer();

        String player = (p != null && this.personalVar) ? p.getName() : "";

        String var;
        String value;

        if (params.contains("id")) {
            var = params.getString("id", "");
            value = params.getString("value", "");
            player = params.getString("player", player);
            if (var.isEmpty()) return false;
        } else {
            String[] ln = params.getString("param-line", "").split("/", 2);
            if (ln.length == 0) return false;
            var = ln[0];
            value = (ln.length > 1) ? ln[1] : "";
        }

        if (this.personalVar && player.isEmpty()) return false;

        switch (this.actType) {
            case SET: //VAR_SET, VAR_PLAYER_SET
                VariablesManager.getInstance().setVar(player, var, value);
                return true;
            case CLEAR: //VAR_CLEAR, VAR_PLAYER_CLEAR
                VariablesManager.getInstance().clearVar(player, var);
                return true;
            case INCREASE: //VAR_INC, VAR_PLAYER_INC
                int incValue = value.isEmpty() || !(NumberUtils.isInteger(value)) ? 1 : Integer.parseInt(value);
                return VariablesManager.getInstance().incVar(player, var, incValue);
            case DECREASE: //VAR_DEC, VAR_PLAYER_DEC
                int decValue = value.isEmpty() || !(NumberUtils.isInteger(value)) ? 1 : Integer.parseInt(value);
                return VariablesManager.getInstance().decVar(player, var, decValue);
            case TEMPORARY_SET:  //VAR_TEMP_SET
                context.setVariable(var, value);
                return true;
        }
        return false;
    }

    public enum Type {
        SET, CLEAR, INCREASE, DECREASE, TEMPORARY_SET
    }
}
