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

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.DeathStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.enums.DeathCause;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class DeathActivator extends Activator {

	private final DeathCause deathCause;

	public DeathActivator(ActivatorBase base, DeathCause cause) {
		super(base);
		this.deathCause = cause;
	}

	@Override
	public boolean activate(Storage event) {
		DeathStorage de = (DeathStorage) event;
		if (this.deathCause != DeathCause.ANY && de.getCause() != this.deathCause) return false;
		Variables.setTempVar("cause", de.getCause().name());
		if (de.getKiller() != null) {
			Variables.setTempVar("killer-type", de.getKiller().getType().name());
			if (de.getKiller().getType() == EntityType.PLAYER) {
				Variables.setTempVar("killer-name", de.getKiller().getName());
				Variables.setTempVar("targetplayer", de.getKiller().getName());
			} else {
				String mobName = de.getKiller().getCustomName();
				Variables.setTempVar("killer-name", mobName == null || mobName.isEmpty() ? de.getKiller().getType().name() : mobName);
			}
		}
		return Actions.executeActivator(de.getPlayer(), getBase());
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("death-cause", this.deathCause != null ? this.deathCause.name() : "PVP");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.DEATH;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append("(").append(this.deathCause.name()).append(")");
		return sb.toString();
	}

	public static DeathActivator create(ActivatorBase base, Param param) {
		DeathCause cause = DeathCause.getByName(param.getParam("cause", param.toString()));
		return new DeathActivator(base, cause);
	}

	public static DeathActivator load(ActivatorBase base, ConfigurationSection cfg) {
		DeathCause cause = DeathCause.getByName(cfg.getString("death-cause"));
		return new DeathActivator(base, cause);
	}
}
