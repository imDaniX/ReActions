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

package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.ActivatorType;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.DoubleValue;
import me.fromgate.reactions.util.location.LocationUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Map;

public class MobDamageStorage extends Storage {
    public static final String DAMAGE = "damage";

    @Getter
    private final LivingEntity entity;
    @Getter
    private final DamageCause cause;
    @Getter
    private double damage;

    public MobDamageStorage(LivingEntity entity, Player damager, double damage, DamageCause cause) {
        super(damager, ActivatorType.MOB_DAMAGE);
        this.entity = entity;
        this.damage = damage;
        this.cause = cause;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("moblocation", LocationUtil.locationToString(entity.getLocation()));
        tempVars.put("mobdamager", getPlayer() == null ? "" : getPlayer().getName());
        tempVars.put("mobtype", entity.getType().name());
        String mobName = entity instanceof Player ? entity.getName() : entity.getCustomName();
        tempVars.put("mobname", Util.isStringEmpty(mobName) ? entity.getType().name() : mobName);
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
        changeables.put(DAMAGE, new DoubleValue(damage));
    }
}
