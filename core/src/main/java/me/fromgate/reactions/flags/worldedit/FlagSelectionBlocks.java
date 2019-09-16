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

package me.fromgate.reactions.flags.worldedit;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.flags.Flag;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FlagSelectionBlocks implements Flag {
	@Override
	public boolean checkFlag(RaContext context, String param) {
		Player player = context.getPlayer();
		int selectionBlocks = RaWorldEdit.getArea(player);
		Vector minPoint = RaWorldEdit.getMinimumPoint(player);
		Vector maxPoint = RaWorldEdit.getMaximumPoint(player);
		context.setTempVariable("minpoint", (minPoint == null) ? "" : minPoint.toString());
		context.setTempVariable("minX", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockX()));
		context.setTempVariable("minY", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockY()));
		context.setTempVariable("minZ", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockZ()));
		context.setTempVariable("maxpoint", (maxPoint == null) ? "" : maxPoint.toString());
		context.setTempVariable("maxX", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockX()));
		context.setTempVariable("maxY", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockY()));
		context.setTempVariable("maxZ", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockZ()));
		context.setTempVariable("selblocks", Integer.toString(selectionBlocks));
		return Util.isInteger(param) && selectionBlocks <= Integer.parseInt(param);
	}
}
