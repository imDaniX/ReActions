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

package me.fromgate.reactions.event;

import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.PlayerDeathActivator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class DeathEvent extends RAEvent {
	private LivingEntity killer;
	private PlayerDeathActivator.DeathCause source;

	public DeathEvent(LivingEntity killer, Player player, PlayerDeathActivator.DeathCause deathCause) {
		super(player, ActivatorType.PLAYER_DEATH);
		this.killer = killer;
		this.source = killer != null ? deathCause : PlayerDeathActivator.DeathCause.OTHER;
	}

	@Override
	public Player getPlayer() {
		return this.player;
	}

	public LivingEntity getKiller() {
		return killer;
	}

	public PlayerDeathActivator.DeathCause getDeathCause() {
		return this.source;
	}

}