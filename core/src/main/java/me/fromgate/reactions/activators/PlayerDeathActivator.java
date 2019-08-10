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
import me.fromgate.reactions.storage.DeathStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.simpledata.DeathCause;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class PlayerDeathActivator extends Activator {

	private final DeathCause deathCause;

	public PlayerDeathActivator(ActivatorBase base, DeathCause cause) {
		super(base);
		this.deathCause = cause;
	}

	@Override
	public boolean activate(RAStorage event) {
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
		return Actions.executeActivator(de.getPlayer(), this);
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("death-cause", this.deathCause != null ? this.deathCause.name() : "PVP");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.PLAYER_DEATH;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append("(").append(this.deathCause.name()).append(")");
		return sb.toString();
	}

	public static PlayerDeathActivator create(ActivatorBase base, Param param) {
		DeathCause cause = DeathCause.getByName(param.getParam("cause", param.toString()));
		return new PlayerDeathActivator(base, cause);
	}

	public static PlayerDeathActivator load(ActivatorBase base, ConfigurationSection cfg) {
		DeathCause cause = DeathCause.getByName(cfg.getString("death-cause"));
		return new PlayerDeathActivator(base, cause);
	}
}
