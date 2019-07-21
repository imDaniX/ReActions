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

import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.activators.PlayerDeathActivator;
import me.fromgate.reactions.storage.RespawnStorage;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRespawner {
	private static Map<UUID, LivingEntity> players = new HashMap<>();
	private static Map<UUID, Location> deathPoints = new HashMap<>();

	public static void addPlayerRespawn(PlayerDeathEvent event) {
		Player deadPlayer = event.getEntity();
		deathPoints.put(deadPlayer.getUniqueId(), deadPlayer.getLocation());  // это может пригодиться и в других ситуациях
		LivingEntity killer = Util.getAnyKiller(deadPlayer.getLastDamageCause());
		players.put(deadPlayer.getUniqueId(), killer);
	}

	public static Location getLastDeathPoint(Player player) {
		if (deathPoints.containsKey(player.getUniqueId())) return deathPoints.get(player.getUniqueId());
		return player.getLocation();
	}

	private static LivingEntity getLastKiller(Player player) {
		if (players.containsKey(player.getUniqueId()))
			return players.get(player.getUniqueId());
		return null;
	}

	public static void raisePlayerRespawnEvent(Player player) {
		if (!players.containsKey(player.getUniqueId())) return;
		LivingEntity killer = getLastKiller(player);
		players.remove(player.getUniqueId());
		PlayerDeathActivator.DeathCause d = PlayerDeathActivator.DeathCause.OTHER;
		if (killer != null && killer.getType() == EntityType.PLAYER) d = PlayerDeathActivator.DeathCause.PVP;
		else if (killer instanceof LivingEntity) d = PlayerDeathActivator.DeathCause.PVE;
		Activators.activate(new RespawnStorage(player, killer, d));
	}

}
