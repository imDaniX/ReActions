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

import lombok.Getter;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.externals.worldguard.WGBridge;
import me.fromgate.reactions.storages.RegionStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

// TODO: Maybe it should work like Cuboid activator instead of using different activators just for one check
public class RegionActivator extends Activator implements Locatable {

	@Getter private final String region;

	private RegionActivator(ActivatorBase base, String region) {
		super(base);
		this.region = region;
	}

	@Override
	public boolean activate(Storage event) {
		RegionStorage be = (RegionStorage) event;
		if (!be.getRegion().equalsIgnoreCase(WGBridge.getFullRegionName(this.region))) return false;
		return true;
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		if (!RaWorldGuard.isConnected()) return false;
		return RaWorldGuard.isLocationInRegion(loc, this.region);
	}

	@Override
	public boolean isLocatedAt(World world, int x, int y, int z) {
		return isLocatedAt(new Location(world, x, y, z));
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("region", this.region);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.REGION;
	}

	@Override
	public boolean isValid() {
		return !Util.isStringEmpty(region);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("region:").append(this.region);
		sb.append(")");
		return sb.toString();
	}

	public static RegionActivator create(ActivatorBase base, Param param) {
		String region = param.getParam("region", param.getParam("param-line"));
		RaWorldGuard.updateRegionCache();
		return new RegionActivator(base, region);
	}

	public static RegionActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String region = cfg.getString("region", "region");
		return new RegionActivator(base, region);
	}
}
