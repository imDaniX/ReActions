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

import me.fromgate.reactions.time.Delayer;
import me.fromgate.reactions.util.TimeUtils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

public class ActionDelay extends Action {

    private final boolean globalDelay;

    public ActionDelay(boolean globalDelay) {
        this.globalDelay = globalDelay;
    }

    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        String timeStr = "";
        String playerName = this.globalDelay ? "" : (player != null ? player.getName() : "");
        String variableId = "";
        boolean add = false;
        if (params.containsEvery("id", "delay") || params.containsEvery("id", "time")) {
            variableId = params.getString("id", "");
            playerName = params.getString("player", playerName);
            timeStr = params.getString("delay", params.getString("time", ""));
            add = params.getBoolean("add", false);
        } else {
            String oldFormat = params.getString("param-line", "");
            if (oldFormat.contains("/")) {
                String[] m = oldFormat.split("/");
                if (m.length >= 2) {
                    timeStr = m[0];
                    variableId = m[1];
                }
            } else timeStr = oldFormat;
        }

        if (timeStr.isEmpty()) return false;
        if (variableId.isEmpty()) return false;
        setDelay(playerName, variableId, TimeUtils.parseTime(timeStr), add);
        Delayer.setTempPlaceholders(context, playerName, variableId);
        setMessageParam(context.getVariable("delay-left-hms", timeStr));
        return true;
    }

    private void setDelay(String playerName, String variableId, long delayTime, boolean add) {
        if (playerName.isEmpty()) Delayer.setDelay(variableId, delayTime, add);
        else Delayer.setPersonalDelay(playerName, variableId, delayTime, add);
    }

}
