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

package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;

import java.util.Map;

public class ExecStorage extends Storage {
	@Getter private Map<String, String> tempVars;

	public ExecStorage(Player player) {
		super(player, ActivatorType.EXEC);
		this.tempVars = null;
	}

	public ExecStorage(Player player, Param tempVars) {
		super(player, ActivatorType.EXEC);
		this.tempVars = tempVars.getMap();
	}

	public ExecStorage(Player player, Map<String, String> tempVars) {
		super(player, ActivatorType.EXEC);
		this.tempVars = tempVars;
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		if(this.tempVars != null && !this.tempVars.isEmpty())
			tempVars.putAll(this.tempVars);
	}
}