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

import me.fromgate.reactions.storages.MobClickStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.mob.EntityUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

public class MobClickActivator extends Activator implements Locatable {
	private final String mobType;
	private final String mobName;
	private final String mobLocation;

	public MobClickActivator(ActivatorBase base, String type, String name, String location) {
		super(base);
		this.mobType = type;
		this.mobName = name;
		this.mobLocation = location;
	}


	@Override
	public boolean activate(Storage event) {
		MobClickStorage me = (MobClickStorage) event;
		if (mobType.isEmpty()) return false;
		if (me.getEntity() == null) return false;
		if (!isActivatorMob(me.getEntity())) return false;
		return true;
	}

	private boolean checkLocations(LivingEntity mob) {
		if (this.mobLocation.isEmpty()) return true;
		return this.isLocatedAt(mob.getLocation());
	}

	private boolean isActivatorMob(LivingEntity mob) {
		if (!mob.getType().name().equalsIgnoreCase(this.mobType)) return false;
		if (!mobName.isEmpty()) {
			if (!ChatColor.translateAlternateColorCodes('&', mobName).equals(EntityUtil.getMobName(mob)))
				return false;
		} else if (!EntityUtil.getMobName(mob).isEmpty()) return false;
		return checkLocations(mob);
	}

	@Override
	public boolean isLocatedAt(Location l) {
		if (this.mobLocation.isEmpty()) return false;
		Location loc = LocationUtil.parseCoordinates(this.mobLocation);
		if (loc == null) return false;
		return l.getWorld().equals(loc.getWorld()) &&
				l.getBlockX() == loc.getBlockX() &&
				l.getBlockY() == loc.getBlockY() &&
				l.getBlockZ() == loc.getBlockZ();
	}

	@Override
	public boolean isLocatedAt(World world, int x, int y, int z) {
		return isLocatedAt(new Location(world, x, y, z));
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("mob-type", this.mobType);
		cfg.set("mob-name", this.mobName.isEmpty() ? null : this.mobName);
		cfg.set("location", this.mobLocation.isEmpty() ? null : this.mobLocation);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.MOB_CLICK;
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
		sb.append(" name:").append(mobName.isEmpty() ? "-" : mobName);
		sb.append(" loc:").append(mobLocation.isEmpty() ? "-" : mobLocation);
		sb.append(")");
		return sb.toString();
	}

	public static MobClickActivator create(ActivatorBase base, Param param) {
		String type = param.toString();
		String name = "";
		String location = "";
		if (param.isParamsExists("type")) {
			type = param.getParam("type");
			name = param.getParam("name");
			location = param.getParam("loc");
		} else if (param.toString().contains("$")) {
			name = type.substring(0, type.indexOf("$"));
			type = type.substring(name.length() + 1);
		}
		return new MobClickActivator(base, type, name, location);
	}

	public static MobClickActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String type = cfg.getString("mob-type", "");
		String name = cfg.getString("mob-name", "");
		String location = cfg.getString("location", "");
		return new MobClickActivator(base, type, name, location);
	}
}
