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
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.externals.worldguard.WGBridge;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.storage.RegionEnterStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class RegionEnterActivator extends Activator implements Locatable {

	@Getter private final String region;

	public RegionEnterActivator(ActivatorBase base, String region) {
		super(base);
		this.region = region;
	}

	@Override
	public boolean activate(RAStorage event) {
		RegionEnterStorage be = (RegionEnterStorage) event;
		if (!be.getRegion().equalsIgnoreCase(WGBridge.getFullRegionName(this.region))) return false;
		return Actions.executeActivator(be.getPlayer(), this);
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
		return ActivatorType.REGION_ENTER;
	}

	@Override
	public boolean isValid() {
		return !Util.emptyString(region);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (");
		sb.append("region:").append(this.region);
		sb.append(")");
		return sb.toString();
	}

	public static RegionEnterActivator create(ActivatorBase base, Param param) {
		String region = param.getParam("region", param.getParam("param-line"));
		return new RegionEnterActivator(base, region);
	}

	public static RegionEnterActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String region = cfg.getString("region", "region");
		return new RegionEnterActivator(base, region);
	}
}
