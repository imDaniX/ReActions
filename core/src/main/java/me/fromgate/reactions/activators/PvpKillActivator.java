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

package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.PvpKillStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.configuration.ConfigurationSection;

public class PvpKillActivator extends Activator {
	public PvpKillActivator(ActivatorBase base) {
		super(base);
	}

	@Override
	public boolean activate(RAStorage event) {
		PvpKillStorage pe = (PvpKillStorage) event;
		Variables.setTempVar("targetplayer", pe.getKilledPlayer().getName());
		return Actions.executeActivator(pe.getPlayer(), this);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.PVP_KILL;
	}

	public static PvpKillActivator create(ActivatorBase base, Param ignore) {
		return new PvpKillActivator(base);
	}

	public static PvpKillActivator load(ActivatorBase base, ConfigurationSection cfg) {
		return new PvpKillActivator(base);
	}
}
