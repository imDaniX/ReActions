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

package me.fromgate.reactions.util.location;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Holder {
	private static Map<String, Location> locs = new HashMap<>();

	public static void holdLocation(Player p, Location loc) {
		if (p == null) return;
		if (loc == null) loc = p.getTargetBlock(null, 100).getLocation();
		locs.put(p.getName(), loc);
	}

	public static Location getHeldLocation(Player p) {
		return locs.get(p.getName());
	}

	@SuppressWarnings("unused")
	public static String getHeldStrLoc(Player p) {
		Location loc = getHeldLocation(p);
		return loc == null ? "" : Locator.locationToString(loc);
	}

}
