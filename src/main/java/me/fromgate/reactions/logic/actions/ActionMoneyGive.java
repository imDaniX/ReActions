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

import me.fromgate.reactions.externals.RaEconomics;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.math.Rng;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActionMoneyGive extends Action {

    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        if (!RaEconomics.isEconomyFound()) return false;
        if (params.isEmpty()) return false;
        if (params.size() <= 2) params = parseOldFormat(player, params.getString("param-line"));
        String amountStr = params.getString("amount", "");
        if (amountStr.isEmpty()) return false;
        String worldName = params.getString("world", "");
        String target = params.getString("target", params.getString("player", (player == null ? "" : player.getName())));
        if (target.isEmpty()) return false;
        String source = params.getString("source", "");
        String message = RaEconomics.creditAccount(target, source, amountStr, worldName);
        if (message.isEmpty()) return false;
        setMessageParam(message);
        return true;
    }

    private Parameters parseOldFormat(Player p, String mstr) {
        Map<String, String> newParams = new HashMap<>();
        if (p != null) newParams.put("target", p.getName());
        if (mstr.contains("/")) {
            String[] m = mstr.split("/");
            if (m.length >= 2) {
                newParams.put("amount", m[0].contains("-") ? Integer.toString(Rng.nextIntFromString(m[0])) : m[0]);
                newParams.put("sourse", m[1]);
            }
        } else newParams.put("amount", mstr.contains("-") ? Integer.toString(Rng.nextIntFromString(mstr)) : mstr);
        return Parameters.fromMap(newParams);
    }
}
