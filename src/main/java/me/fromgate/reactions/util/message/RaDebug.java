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

package me.fromgate.reactions.util.message;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class RaDebug {
    private final Map<UUID, Boolean> debug = new HashMap<>();

    public void setPlayerDebug(Player p, boolean debugMode) {
        debug.put(p.getUniqueId(), debugMode);
    }

    public void offPlayerDebug(Player p) {
        debug.remove(p.getUniqueId());
    }

    public boolean checkFlagAndDebug(Player p, boolean flag) {
        if ((p != null) && debug.containsKey(p.getUniqueId())) return (debug.get(p.getUniqueId()));
        return flag;
    }

}
