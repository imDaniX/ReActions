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
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.LocationValue;
import me.fromgate.reactions.util.enums.DeathCause;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class RespawnStorage extends Storage {
    public static final String RESPAWN_LOCATION = "respawn_loc";

    @Getter
    private final DeathCause deathCause;
    @Getter
    private final LivingEntity killer;
    private final Location respawnLoc;

    public RespawnStorage(Player player, LivingEntity killer, DeathCause cause, Location respawnLoc) {
        super(player, ActivatorType.RESPAWN);
        this.killer = killer;
        this.deathCause = cause;
        this.respawnLoc = respawnLoc;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("cause", deathCause.name());
        if (killer != null) {
            tempVars.put("killer-type", killer.getType().name());
            if (killer.getType() == EntityType.PLAYER) {
                tempVars.put("targetplayer", killer.getName());
                tempVars.put("killer-name", killer.getName());
            } else {
                String mobName = killer.getCustomName();
                tempVars.put("killer-name", Util.isStringEmpty(mobName) ? killer.getType().name() : mobName);
            }
        }
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(RESPAWN_LOCATION, new LocationValue(respawnLoc));
    }
}
