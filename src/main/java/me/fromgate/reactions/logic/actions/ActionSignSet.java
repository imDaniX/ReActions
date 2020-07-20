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

import me.fromgate.reactions.util.BlockUtil;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class ActionSignSet extends Action {

    @Override
    public boolean execute(RaContext context, Parameters params) {
        // loc:world,x,y,z line1:text line2:text line3:text line4:text clear:1,2,3,4
        String locStr = params.getParam("loc", context.getTempVariable("sign_loc"));
        if (Util.isStringEmpty(locStr)) return false;
        Location loc = LocationUtil.parseCoordinates(locStr);
        if (loc == null) return false;
        boolean chunkLoad = params.getParam("loadchunk", false);
        if (!chunkLoad && !loc.getChunk().isLoaded()) return false;
        Block block = loc.getBlock();
        if (!BlockUtil.isSign(block)) return false;
        Sign sign = (Sign) block.getState();
        for (int i = 1; i <= 4; i++) {
            String line = params.getParam("line" + i, "");
            if (line.isEmpty()) continue;
            if (line.length() > 15) line = line.substring(0, 15);
            sign.setLine(i - 1, ChatColor.translateAlternateColorCodes('&', line));
        }

        String clear = params.getParam("clear", "");
        if (!clear.isEmpty()) {
            String[] ln = clear.split(",");
            for (String cl : ln) {
                if (!Util.isInteger(cl)) continue;
                int num = Integer.parseInt(cl) - 1;
                if (num < 0) continue;
                if (num >= 4) continue;
                sign.setLine(num, "");
            }
        }
        sign.update(true);
        return true;
    }

}
