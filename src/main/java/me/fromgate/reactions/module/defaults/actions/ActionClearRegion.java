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

package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.logic.activity.actions.Action;
import me.fromgate.reactions.util.Alias;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.mob.EntityUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Alias("RGCLEAR")
public class ActionClearRegion extends Action {
    // TODO: Too weird. Optimize, simplify

    @Override
    protected boolean execute(RaContext context, Parameters params) {
        String region = params.getString("region", "");
        String type = params.getString("type", "all");
        if (region.isEmpty()) return false;
        if (!RaWorldGuard.isConnected()) return false;
        List<Location> locs = RaWorldGuard.getRegionMinMaxLocations(region);
        if (locs.size() != 2) return false;
        Collection<Entity> en = EntityUtils.getEntities(locs.get(0), locs.get(1));
        int count = 0;
        for (Entity e : en) {
            if (e.getType() == EntityType.PLAYER) continue;
            if (isEntityIsTypeOf(e, type)) {
                e.remove();
                count++;
            }
        }
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "REGION_CLEAR";
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }


    private boolean isEntityIsTypeOf(Entity e, String type) {
        if (e == null) return false;
        if (type.isEmpty()) return true;
        if (type.equalsIgnoreCase("all")) return true;
        if (e instanceof LivingEntity) {
            if (type.equalsIgnoreCase("mob") || type.equalsIgnoreCase("mobs")) return true;
        } else {
            if (type.equalsIgnoreCase("item") || type.equalsIgnoreCase("items")) return true;
        }
        return (Utils.isWordInList(e.getType().name().toLowerCase(Locale.ENGLISH), type.toLowerCase(Locale.ENGLISH)));
    }
}
