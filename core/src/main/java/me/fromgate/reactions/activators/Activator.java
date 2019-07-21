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
import me.fromgate.reactions.actions.StoredAction;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.flags.StoredFlag;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Cfg;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.message.Msg;
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

	/**
	 * Add flag to activator
	 * @param flag Name of flag to add
	 * @param param Parameters of flag
	 * @param not Is indentation needed
	 */
	public void addFlag(String flag, String param, boolean not) {
		addFlag(Flags.getByName(flag), param, not);
	}

	/**
	 * Add flag to activator
	 * @param flag Flag to add
	 * @param param Parameters of flag
	 * @param not Is indentation needed
	 */
	public void addFlag(Flags flag, String param, boolean not) {
		StoredFlag flg = new StoredFlag(flag, param, not);
		if(flg.getFlag() == null)
			Msg.logOnce("wrongflagname"+flags.size()+name, "Flag for activator "+ name +" with this name does not exist.");
		else
			flags.add(flg);
	}

	/**
	 * Remove flag from activator
	 * @param index Index of flag
	 * @return Is there flag with this index
	 */
	public boolean removeFlag(int index) {
		if (flags.size() <= index) return false;
		flags.remove(index);
		return true;
	}

	/**
	 * Get list of all flags
	 * @return List of flags
	 */
	public List<StoredFlag> getFlags() {
		return flags;
	}

	/**
	 * Add action to activator
	 * @param action Name of action to add
	 * @param param Parameters of action
	 */
	public void addAction(String action, String param) {
		addAction(Actions.getByName(action), param);
	}

	/**
	 * Add action to activator
	 * @param action Action to add
	 * @param param Parameters of action
	 */
	public void addAction(Actions action, String param) {
		StoredAction act = new StoredAction(action, param);
		if(act.getAction() == null)
			Msg.logOnce("wrongactopmname"+actions.size()+name, "Flag for activator "+ name +" with this name does not exist.");
		else
			actions.add(act);
	}

	/**
	 * Remove action from activator
	 * @param index Index of action
	 * @return Is there action with this index
	 */
	public boolean removeAction(int index) {
		if (actions.size() <= index) return false;
		actions.remove(index);
		return true;
	}

	/**
	 * Add reaction to activator
	 * @param action Name of action to add
	 * @param param Parameters of action
	 */
	public void addReaction(String action, String param) {
		addReaction(Actions.getByName(action), param);
	}

	/**
	 * Add reaction to activator
	 * @param action Action to add
	 * @param param Parameters of action
	 */
	public void addReaction(Actions action, String param) {
		reactions.add(new StoredAction(action, param));
	}

	/**
	 * Remove reaction from activator
	 * @param index Index of action
	 * @return Is there action with this index
	 */
	public boolean removeReaction(int index) {
		if (reactions.size() <= index) return false;
		reactions.remove(index);
		return true;
	}

	/**
	 * Get list of all actions
	 * @return List of actions
	 */
	public List<StoredAction> getActions() {
		return actions;
	}

	/**
	 * Get list of all reactions
	 * @return List of actions
	 */
	public List<StoredAction> getReactions() {
		return reactions;
	}

	/**
	 * Clear flags of activator
	 */
	public void clearFlags() {
		flags.clear();
	}

	/**
	 * Clear actions of activator
	 */
	public void clearActions() {
		actions.clear();
	}

	/**
	 * Clear reactions of activator
	 */
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

	/**
	 * Save activator to config
	 * @param cfg Config for activator
	 */
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

	/**
	 * Load activator from config
	 * @param cfg Config for activator
	 */
	public void loadActivator(YamlConfiguration cfg) {
		String key = getType().name() + "." + this.name;
		load(cfg.getConfigurationSection(key));
		List<String> data = cfg.getStringList(key + ".flags");
		for (String flgstr : data) {
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

		data = cfg.getStringList(key + ".actions");
		for (String actstr : data) {
			String flag = actstr;
			String param = "";
			if (actstr.contains("=")) {
				flag = actstr.substring(0, actstr.indexOf("="));
				param = actstr.substring(actstr.indexOf("=") + 1);
			}
			addAction(flag, param);
		}

		data = cfg.getStringList(key + ".reactions");
		for (String rctstr : data) {
			String flag = rctstr;
			String param = "";
			if (rctstr.contains("=")) {
				flag = rctstr.substring(0, rctstr.indexOf("="));
				param = rctstr.substring(rctstr.indexOf("=") + 1);
			}
			addReaction(flag, param);
		}
	}

	/**
	 * Set group of activator
	 * @param group Group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * Get group of activator
	 * @return Group of activator
	 */
	public String getGroup() {
		return this.group;
	}

	/**
	 * Get name of activator
	 * @return Name of activator
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Execution of activator
	 * @param storage Storage with data for activator
	 * @return Cancel original event or not
	 */
	public boolean executeActivator(RAStorage storage) {
		boolean result = activate(storage);
		Variables.clearAllTempVar();
		return result;
	}

	/**
	 * Execution of activator
	 * @param storage Storage with data for activator
	 * @return Cancel original event or not
	 */
	public abstract boolean activate(RAStorage storage); // Наверное всё-таки так

	/**
	 * Save activator to config
	 * @param cfg Section of activator
	 */
	public abstract void save(ConfigurationSection cfg);

	/**
	 * Load activator from config
	 * @param cfg Section of activator
	 */
	public abstract void load(ConfigurationSection cfg);

	/**
	 * Get type of activator
	 * @return Type of activator
	 */
	public abstract ActivatorType getType();

	/**
	 * Check if activator is valid
	 * @return Is activator valid
	 */
	public abstract boolean isValid();

	@Override
	public int hashCode() {
		return group.hashCode()*1291 + name.hashCode()/20;
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
		if (!(obj instanceof Activator))
			return false;
		Activator other = (Activator) obj;
		if (name == null)
			return other.name == null;
		if (group == null)
			return other.group == null;
		return this.name.equals(other.name) && this.group.equals(other.group);
	}
}
