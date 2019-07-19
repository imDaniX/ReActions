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
import me.fromgate.reactions.storage.DoorStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class DoorActivator extends Activator implements Locatable {
	private String state; //open, close
	//координаты нижнего блока двери
	private String world;
	private int x;
	private int y;
	private int z;

	public DoorActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	public DoorActivator(String name, Block b, String param) {
		super(name, "activators");
		this.state = "ANY";
		if (param.equalsIgnoreCase("open")) state = "OPEN";
		if (param.equalsIgnoreCase("close")) state = "CLOSE";
		this.world = b.getWorld().getName();
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
	}

	@Override
	public boolean activate(RAStorage event) {
		DoorStorage de = (DoorStorage) event;
		if (de.getDoorBlock() == null) return false;
		if (!isLocatedAt(de.getDoorLocation())) return false;
		if (this.state.equalsIgnoreCase("open") && de.isDoorOpened()) return false;
		if (this.state.equalsIgnoreCase("close") && (!de.isDoorOpened())) return false;
		return Actions.executeActivator(de.getPlayer(), this);
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
	public void save(ConfigurationSection cfg) {
		cfg.set("world", this.world);
		cfg.set("x", x);
		cfg.set("y", y);
		cfg.set("z", z);
		cfg.set("state", state);
		cfg.set("lever-state", null);
	}

	@Override
	public void load(ConfigurationSection cfg) {
		world = cfg.getString("world");
		x = cfg.getInt("x");
		y = cfg.getInt("y");
		z = cfg.getInt("z");
		this.state = cfg.getString("state", cfg.getString("lever-state", "ANY"));
		if ((!this.state.equalsIgnoreCase("open")) && (!state.equalsIgnoreCase("close"))) state = "ANY";
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.DOOR;
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
		sb.append(" (").append(world).append(", ").append(x).append(", ").append(y).append(", ").append(z);
		sb.append(" state:").append(this.state.toUpperCase()).append(")");
		return sb.toString();
	}


}
