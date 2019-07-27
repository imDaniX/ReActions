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

package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.PlayerDeathActivator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RespawnStorage extends RAStorage {
	@Getter private final PlayerDeathActivator.DeathCause deathCause;
	@Getter private final LivingEntity killer;

	public RespawnStorage(Player player, LivingEntity killer, PlayerDeathActivator.DeathCause cause) {
		super(player, ActivatorType.PLAYER_RESPAWN);
		this.killer = killer;
		this.deathCause = cause;
	}
}
