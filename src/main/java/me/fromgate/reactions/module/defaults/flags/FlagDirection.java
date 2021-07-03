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

import me.fromgate.reactions.logic.activity.flags.OldFlag;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.entity.Player;

public class FlagDirection implements OldFlag {

    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        return isPlayerDirected(player, param);
    }

    private boolean isPlayerDirected(Player p, String dirstr) {
        Direction d1 = Direction.getByName(dirstr);
        if (d1 == null) return false;
        Direction d2 = Direction.getByYaw(p);
        if (d2 == null) return false;
        return (d1 == d2);
    }


    private enum Direction {
        SOUTH,
        SOUTHWEST,
        WEST,
        NORTHWEST,
        NORTH,
        NORTHEAST,
        EAST,
        SOUTHEAST;

        public static Direction getByName(String dirstr) {
            for (Direction d : Direction.values())
                if (d.name().equalsIgnoreCase(dirstr)) return d;
            return null;

        }

        public static Direction getByYaw(Player p) {
            double angle = (p.getLocation().getYaw() < 0) ? (360 + p.getLocation().getYaw()) : p.getLocation().getYaw();
            int sector = (int) (angle - ((angle + 22.5) % 45.0) + 22.5);
            return switch (sector) {
                case 45 -> SOUTHWEST;
                case 90 -> WEST;
                case 135 -> NORTHWEST;
                case 180 -> NORTH;
                case 225 -> NORTHEAST;
                case 270 -> EAST;
                case 315 -> SOUTHEAST;
                default -> SOUTH;
            };
        }
    }
}
