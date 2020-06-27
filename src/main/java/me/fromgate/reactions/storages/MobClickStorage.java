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
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.location.LocationUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Map;


public class MobClickStorage extends Storage {
	@Getter private final LivingEntity entity;

	public MobClickStorage(Player p, LivingEntity entity) {
		super(p, ActivatorType.MOB_CLICK);
		this.entity = entity;
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		tempVars.put("moblocation", LocationUtil.locationToString(entity.getLocation()));
		tempVars.put("mobtype", entity.getType().name());
		String mobName = entity instanceof Player ? entity.getCustomName() : entity.getName();
		tempVars.put("mobname", Util.isStringEmpty(mobName) ? entity.getType().name() : mobName);
	}

	@Override
	void defaultChangeables(Map<String, DataValue> changeables) {
		changeables.put(Storage.CANCEL_EVENT, new BooleanValue(false));
	}
}
