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

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.module.defaults.storages.RespawnStorage;
import me.fromgate.reactions.util.enums.DeathCause;
import me.fromgate.reactions.util.mob.EntityUtils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO: Move to LocationHolder
@UtilityClass
public class PlayerRespawner {
    private final Map<UUID, LivingEntity> players = new HashMap<>();
    private final Map<UUID, Location> deathPoints = new HashMap<>();

    public void addPlayerRespawn(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        deathPoints.put(deadPlayer.getUniqueId(), deadPlayer.getLocation());  // это может пригодиться и в других ситуациях
        LivingEntity killer = EntityUtils.getAnyKiller(deadPlayer.getLastDamageCause());
        players.put(deadPlayer.getUniqueId(), killer);
    }

    public Location getLastDeathPoint(Player player) {
        return deathPoints.getOrDefault(player.getUniqueId(), player.getLocation());
    }

    public void triggerPlayerRespawn(Player player, Location respawnLoc) {
        if (!players.containsKey(player.getUniqueId())) return;
        LivingEntity killer = players.remove(player.getUniqueId());
        DeathCause d = killer == null ?
                DeathCause.OTHER :
                killer.getType() == EntityType.PLAYER ? DeathCause.PVP : DeathCause.PVE;
        ReActions.getActivators().activate(new RespawnStorage(player, killer, d, respawnLoc));
    }

}
