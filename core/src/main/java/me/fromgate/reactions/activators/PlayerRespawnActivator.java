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
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.storage.RespawnStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.simpledata.DeathCause;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class PlayerRespawnActivator extends Activator {

	private final DeathCause deathCause;

	public PlayerRespawnActivator(ActivatorBase base, DeathCause cause) {
		super(base);
		this.deathCause = cause;
	}

	@Override
	public boolean activate(RAStorage event) {
		RespawnStorage pe = (RespawnStorage) event;
		if (this.deathCause != DeathCause.ANY && pe.getDeathCause() != this.deathCause)
			return false;
		Variables.setTempVar("cause", pe.getDeathCause().name());
		if (pe.getKiller() != null) {
			Variables.setTempVar("killer-type", pe.getKiller().getType().name());
			if (pe.getKiller().getType() == EntityType.PLAYER) {
				Variables.setTempVar("targetplayer", pe.getKiller().getName());
				Variables.setTempVar("killer-name", pe.getKiller().getName());
			} else {
				String mobName = pe.getKiller().getCustomName();
				Variables.setTempVar("killer-name", mobName == null || mobName.isEmpty() ? pe.getKiller().getType().name() : mobName);
			}
		}
		return Actions.executeActivator(pe.getPlayer(), getBase());
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("death-cause", deathCause.name());
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.PLAYER_RESPAWN;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append("(").append(this.deathCause.name()).append(")");
		return sb.toString();
	}

	public static PlayerRespawnActivator create(ActivatorBase base, Param param) {
		DeathCause cause = DeathCause.getByName(param.getParam("cause", param.toString()));
		return new PlayerRespawnActivator(base, cause);
	}

	public static PlayerRespawnActivator load(ActivatorBase base, ConfigurationSection cfg) {
		DeathCause cause = DeathCause.getByName(cfg.getString("death-cause", "ANY"));
		return new PlayerRespawnActivator(base, cause);
	}
}