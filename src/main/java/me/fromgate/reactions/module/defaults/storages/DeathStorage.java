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

package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.DeathActivator;
import me.fromgate.reactions.util.enums.DeathCause;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeathStorage extends Storage {

    private final LivingEntity killer;
    private final DeathCause cause;

    public DeathStorage(LivingEntity killer, Player player, DeathCause deathCause) {
        super(player);
        this.killer = killer;
        this.cause = killer != null ? deathCause : DeathCause.OTHER;
    }

    @Override
    public Class<? extends Activator> getType() {
        return DeathActivator.class;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        tempVars.put("cause", cause.name());
        if (killer != null) {
            tempVars.put("killer-type", killer.getType().name());
            if (killer.getType() == EntityType.PLAYER) {
                tempVars.put("killer-name", killer.getName());
                tempVars.put("targetplayer", killer.getName());
            } else {
                String mobName = killer.getCustomName();
                tempVars.put("killer-name", mobName == null || mobName.isEmpty() ? killer.getType().name() : mobName);
            }
        }
        return tempVars;
    }
}
