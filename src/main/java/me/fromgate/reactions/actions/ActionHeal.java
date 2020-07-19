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

package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.mob.EntityUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;

public class ActionHeal extends Action {

    @Override
    public boolean execute(RaContext context, Param params) {
        Player player = context.getPlayer();
        if (params.hasAnyParam("player"))
            player = Util.getPlayerExact(params.getParam("player"));
        if (player == null) return false;
        double hp = params.getParam("hp", 0);
        boolean playHearts = params.getParam("hearts", true);
        if (params.isParamsExists("params")) hp = params.getParam("params", 0);
        double health = player.getHealth();
        double healthMax = EntityUtil.getMaxHealth(player);
        if (health < healthMax && hp >= 0) {
            player.setHealth(hp == 0 ? healthMax : Math.min(hp + health, healthMax));
        }
        setMessageParam(Double.toString(hp));
        return true;
    }
}
