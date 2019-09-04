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

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.MobKillStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class MobKillActivator extends Activator {
	private final String mobType;
	private final String mobName;

	public MobKillActivator(ActivatorBase base, String type, String name) {
		super(base);
		this.mobType = type;
		this.mobName = name;
	}

	@Override
	public boolean activate(Storage event) {
		MobKillStorage me = (MobKillStorage) event;
		if (mobType.isEmpty()) return false;
		if (me.getEntity() == null) return false;
		if (!isActivatorMob(me.getEntity())) return false;
		Variables.setTempVar("moblocation", LocationUtil.locationToString(me.getEntity().getLocation()));
		Variables.setTempVar("mobkiller", me.getPlayer() == null ? "" : me.getPlayer().getName());
		Variables.setTempVar("mobtype", me.getEntity().getType().name());
		LivingEntity mob = me.getEntity();
		Player player = mob instanceof Player ? (Player) mob : null;
		String mobName = (player == null) ? me.getEntity().getCustomName() : player.getName();
		Variables.setTempVar("mobname", mobName != null && !mobName.isEmpty() ? mobName : me.getEntity().getType().name());
		return Actions.executeActivator(me.getPlayer(), getBase());
	}


	private boolean isActivatorMob(LivingEntity mob) {
		if (!mobName.isEmpty()) {
			if (!ChatColor.translateAlternateColorCodes('&', mobName.replace("_", " ")).equals(getMobName(mob)))
				return false;
		} else if (!getMobName(mob).isEmpty()) return false;
		return mob.getType().name().equalsIgnoreCase(this.mobType);
	}


	private String getMobName(LivingEntity mob) {
		if (mob.getCustomName() == null) return "";
		return mob.getCustomName();
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("mob-type", this.mobType);
		cfg.set("mob-name", this.mobName);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.MOB_KILL;
	}

	@Override
	public boolean isValid() {
		return !Util.isStringEmpty(mobType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("type:").append(mobType.isEmpty() ? "-" : mobType.toUpperCase());
		sb.append(" name:").append(mobName.isEmpty() ? "-" : mobName.isEmpty());
		sb.append(")");
		return sb.toString();
	}

	public static MobKillActivator create(ActivatorBase base, Param param) {
		String type = param.toString();
		String name = "";
		if (param.isParamsExists("type")) {
			type = param.getParam("type");
			name = param.getParam("name");
		} else if (param.toString().contains("$")) {
			name = type.substring(0, type.indexOf("$"));
			type = type.substring(name.length() + 1);
		}
		return new MobKillActivator(base, type, name);
	}

	public static MobKillActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String type = cfg.getString("mob-type", "");
		String name = cfg.getString("mob-name", "");
		return new MobKillActivator(base, type, name);
	}
}
