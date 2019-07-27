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

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.PlateStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlateActivator extends Activator implements Locatable {
	private String world;
	private int x;
	private int y;
	private int z;

	PlateActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	PlateActivator(String name, Block targetBlock) {
		super(name, "activators");
		if (targetBlock != null && (targetBlock.getType().name().endsWith("PRESSURE_PLATE"))) {
			this.world = targetBlock.getWorld().getName();
			this.x = targetBlock.getX();
			this.y = targetBlock.getY();
			this.z = targetBlock.getZ();
		}
	}

	public PlateActivator(String name, Block targetBlock, String param) {
		this(name, targetBlock);
	}


	/*
	 * Очередная залипуха, надо будет потом переделать
	 */
	@Override
	public boolean activate(RAStorage event) {
		PlateStorage be = (PlateStorage) event;
		if (!isLocatedAt(be.getLocation())) return false;
		return Actions.executeActivator(be.getPlayer(), this);
	}

	@Override
	public boolean isLocatedAt(Location l) {
		if (l == null) return false;
		if (!world.equals(l.getWorld().getName())) return false;
		if (x != l.getBlockX()) return false;
		if (y != l.getBlockY()) return false;
		return (z == l.getBlockZ());
	}

	@Override
	public boolean isLocatedAt(World world, int x, int y, int z) {
		return this.world.equals(world.getName()) &&
				this.x == x &&
				this.y == y &&
				this.z == z;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("world", this.world);
		cfg.set("x", x);
		cfg.set("y", y);
		cfg.set("z", z);
	}

	@Override
	public void load(ConfigurationSection cfg) {
		world = cfg.getString("world");
		x = cfg.getInt("x");
		y = cfg.getInt("y");
		z = cfg.getInt("z");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.PLATE;
	}

	@Override
	public boolean isValid() {
		return !Util.emptySting(world);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (").append(world).append(", ").append(x).append(", ").append(y).append(", ").append(z).append(")");
		return sb.toString();
	}

}
