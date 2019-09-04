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
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Storages are used to transfer some data to activators
 */
public abstract class Storage {

	// Default temporary placeholders
	private Map<String, String> tempVars;
	private Map<String, DataValue> changeables;

	@Getter protected final Player player;
	@Getter private final ActivatorType type;
	// TODO: Move to changeables
	@Getter @Setter private boolean cancelled = false;

	public Storage(Player player, ActivatorType type) {
		this.player = player;
		this.type = type;
	}

	final void setDefaults() {
		setDefaultTempVariables(getTempVariables());
		setDefaultChangeables(getChangeables());
	}

	Map<String, String> getTempVariables() {
		return Collections.emptyMap();
	}
	Map<String, DataValue> getChangeables() {
		return Collections.emptyMap();
	}

	private void setDefaultTempVariables(Map<String, String> tempVars) {
		if(this.tempVars != null) return;
		this.tempVars = Collections.unmodifiableMap(tempVars);
	}

	private void setDefaultChangeables(Map<String, DataValue> changeables) {
		if(this.changeables != null) return;
		this.changeables = new HashMap<>(changeables);
	}

	public final RaContext generateContext() {
		return new RaContext(tempVars, changeables, player);
	}

}
