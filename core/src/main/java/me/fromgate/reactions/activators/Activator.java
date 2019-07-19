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
import me.fromgate.reactions.event.RAEvent;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.actions.StoredAction;
import me.fromgate.reactions.util.Cfg;
import me.fromgate.reactions.flags.StoredFlag;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public abstract class Activator {

	String name;
	String group;

	public Activator(String name, String group) {
		this.name = name;
		this.group = group;
	}

	public Activator(String name, String group, YamlConfiguration cfg) {
		this.name = name;
		this.loadActivator(cfg);
		this.group = group;
	}

	private List<StoredFlag> flags = new ArrayList<>();
	private List<StoredAction> actions = new ArrayList<>();
	private List<StoredAction> reactions = new ArrayList<>();

	public void addFlag(String flag, String param, boolean not) {
		flags.add(new StoredFlag(Flags.getValidName(flag), param, not));
	}

	public boolean removeFlag(int index) {
		if (flags.size() <= index) return false;
		flags.remove(index);
		return true;
	}

	public List<StoredFlag> getFlags() {
		return flags;
	}

	public void addAction(String action, String param) {
		actions.add(new StoredAction(Actions.getValidName(action), param));
	}

	public boolean removeAction(int index) {
		if (actions.size() <= index) return false;
		actions.remove(index);
		return true;
	}

	public void addReaction(String action, String param) {
		reactions.add(new StoredAction(Actions.getValidName(action), param));
	}

	public boolean removeReaction(int index) {
		if (reactions.size() <= index) return false;
		reactions.remove(index);
		return true;
	}

	public List<StoredAction> getActions() {
		return actions;
	}

	public List<StoredAction> getReactions() {
		return reactions;
	}


	public void clearFlags() {
		flags.clear();
	}

	public void clearActions() {
		actions.clear();
	}

	public void clearReactions() {
		reactions.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		return sb.toString();
	}


	@Override
	public int hashCode() {
		 return group.hashCode()*31 + name.hashCode()*7;
	}

	public boolean equals(String name) {
		if (name == null) return false;
		if (name.isEmpty()) return false;
		return this.name.equalsIgnoreCase(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Activator))
			return false;
		Activator other = (Activator) obj;
		if (name == null)
			return other.name == null;
		if (group == null)
			return other.group == null;
		return this.name.equals(other.name) && this.group.equals(other.group);
	}

	public void saveActivator(YamlConfiguration cfg) {
		String key = getType() + "." + this.name;
		save(cfg.getConfigurationSection(key));
		List<String> flg = new ArrayList<>();
		for (StoredFlag f : flags) flg.add(f.toString());
		cfg.set(key + ".flags", flg.isEmpty() && !Cfg.saveEmptySections ? null : flg);
		flg = new ArrayList<>();
		for (StoredAction a : actions) flg.add(a.toString());
		cfg.set(key + ".actions", flg.isEmpty() && !Cfg.saveEmptySections ? null : flg);
		flg = new ArrayList<>();
		for (StoredAction a : reactions) flg.add(a.toString());
		cfg.set(key + ".reactions", flg.isEmpty() && !Cfg.saveEmptySections ? null : flg);
	}

	public void loadActivator(YamlConfiguration cfg) {
		String key = getType().name() + "." + this.name;
		load(cfg.getConfigurationSection(key));
		List<String> flg = cfg.getStringList(key + ".flags");
		for (String flgstr : flg) {
			String flag = flgstr;
			String param = "";
			boolean not = false;
			if (flgstr.contains("=")) {
				flag = flgstr.substring(0, flgstr.indexOf("="));
				if (flgstr.indexOf("=") < flgstr.length())
					param = flgstr.substring(flgstr.indexOf("=") + 1);
			}
			if (flag.startsWith("!")) {
				flag = flag.replaceFirst("!", "");
				not = true;
			}
			addFlag(flag, param, not);
		}

		flg = cfg.getStringList(key + ".actions");
		for (String flgstr : flg) {
			String flag = flgstr;
			String param = "";
			if (flgstr.contains("=")) {
				flag = flgstr.substring(0, flgstr.indexOf("="));
				param = flgstr.substring(flgstr.indexOf("=") + 1);
			}
			addAction(flag, param);
		}

		flg = cfg.getStringList(key + ".reactions");
		for (String flgstr : flg) {
			String flag = flgstr;
			String param = "";
			if (flgstr.contains("=")) {
				flag = flgstr.substring(0, flgstr.indexOf("="));
				param = flgstr.substring(flgstr.indexOf("=") + 1);
			}
			addReaction(flag, param);
		}
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return this.group;
	}

	public String getName() {
		return this.name;
	}


	public boolean executeActivator(RAEvent event) {
		boolean result = activate(event);
		Variables.clearAllTempVar();
		return result;
	}


	public abstract boolean activate(RAEvent event); // Наверное всё-таки так

	public abstract boolean isLocatedAt(Location loc);

	public abstract void save(ConfigurationSection cfg);

	public abstract void load(ConfigurationSection cfg);

	public abstract ActivatorType getType();

	public abstract boolean isValid();

}
