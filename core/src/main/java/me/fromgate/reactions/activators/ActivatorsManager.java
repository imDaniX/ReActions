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

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.StoredAction;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.flags.StoredFlag;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.time.TimersManager;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ActivatorsManager {

	private static HashMap<ActivatorType, Set<Activator>> activatorsMap;
	private static HashMap<String,Activator> activators;
	private static Set<String> stopexec;

	/**
	 * Initialize variables
	 */
	public static void init() {
		activatorsMap = new HashMap<>();
		for(ActivatorType type : ActivatorType.values())
			activatorsMap.put(type, new HashSet<>());
		activators = new HashMap<>();
		stopexec = new HashSet<>();
		loadActivators();
	}

	/**
	 * Load activators
	 */
	public static void loadActivators() {
		Set<String> groups = findGroupsInDirs("");
		if (!groups.isEmpty())
			for (String group : groups)
				loadActivators(group, false);
		TimersManager.updateIngameTimers();
		RaWorldGuard.updateRegionCache();
	}

	/**
	 * Get file names of activator groups
	 * @param dir Name of folder
	 * @return Set of file names
	 */
	private static Set<String> findGroupsInDirs(String dir) {
		dir = ((dir.isEmpty()) ? "" : dir + File.separator);
		Set<String> grps = new HashSet<>();
		File dirs = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separator + dir);
		if (!dirs.exists()) dirs.mkdirs();
		for (File f : dirs.listFiles()) {
			if (f.isDirectory()) {
				grps.addAll(findGroupsInDirs(dir + f.getName()));
			} else {
				String fstr = f.getName();
				if (fstr.endsWith(".yml")) {
					grps.add(dir + fstr.substring(0, fstr.length() - 4));
				}
			}
		}
		return grps;
	}

	/**
	 * Get total count of activators
	 * @return Activators count
	 */
	public static int size() {
		return activators.size();
	}

	/**
	 * Clear all the activators
	 */
	public static void clear() {
		for(Set<Activator> acts : activatorsMap.values())
			acts.clear();
		activators.clear();
	}

	/**
	 * Get all activators in specific location
	 * @param world World to check
	 * @param x Coordinate x to check
	 * @param y Coordinate y to check
	 * @param z Coordinate z to check
	 * @return Set of activators in location
	 */
	public static Set<Activator> getActivatorInLocation(World world, int x, int y, int z) {
		Set<Activator> found = new HashSet<>();
		for (ActivatorType type : ActivatorType.values())
			if(type.isLocated())
				activatorsMap.get(type).stream().filter(act -> ((Locatable)act).isLocatedAt(world, x, y, z)).forEach(found::add);
		return found;
	}

	/**
	 * Get all activators in specific location
	 * @param loc Location to check
	 * @return Set of activators in location
	 */
	@SuppressWarnings("unused")
	public static Set<Activator> getActivatorInLocation(Location loc) {
		Set<Activator> found = new HashSet<>();
		for (ActivatorType type : ActivatorType.values())
			if(type.isLocated())
				activatorsMap.get(type).stream().filter(act -> ((Locatable)act).isLocatedAt(loc)).forEach(found::add);
		return found;
	}

	/**
	 * Add activator if activator with it's name don't exist
	 * @param act Activator to add
	 * @return Was activator added or not
	 */
	public static boolean add(Activator act) {
		if (contains(act.getBase().getName())) return false;
		activatorsMap.get(act.getType()).add(act);
		activators.put(act.getBase().getName(), act);
		return true;
	}

	/**
	 * Remove activator by name
	 * @param name Name of activator to remove
	 */
	public static void removeActivator(String name) {
		for(Set<Activator> acts : activatorsMap.values()) {
			Iterator<Activator> iterator = acts.iterator();
			while(iterator.hasNext())
				if(iterator.next().getBase().getName().equals(name)) {
					iterator.remove();
					break;
				}
		}
		activators.remove(name);
	}

	/**
	 * Check existing of activator by name
	 * @param name Name of activator to search
	 * @return Does activator with this name exist
	 */
	public static boolean contains(String name) {
		return activators.containsKey(name);
	}

	/**
	 * Get activator by name
	 * @param name Name of activator to search
	 * @return Activator or null
	 */
	public static Activator get(String name) {
		return activators.get(name);
	}

	/**
	 * Clear flags of activator
	 * @param name Name of activator
	 * @return Does activator with this name exist
	 */
	public static boolean clearFlags(String name) {
		Activator a = get(name);
		if (a == null) return false;
		a.getBase().clearFlags();
		return true;
	}

	/**
	 * Clear actions of activator
	 * @param name Name of activator
	 * @return Does activator with this name exist
	 */
	public static boolean clearActions(String name) {
		Activator a = get(name);
		if (a == null) return false;
		a.getBase().clearActions();
		return true;
	}

	/**
	 * Clear reactions of activator
	 * @param name Name of activator
	 * @return Does activator with this name exist
	 */
	public static boolean clearReactions(String name) {
		Activator a = get(name);
		if (a == null) return false;
		a.getBase().clearReactions();
		return true;
	}

	/**
	 * Add flag to activator
	 * @param activator Name of activator
	 * @param flag Name of flag
	 * @param param Parameters of flag
	 * @param not Invert flag
	 * @return Does activator with this name exist
	 */
	public static boolean addFlag(String activator, String flag, String param, boolean not) {
		Activator a = get(activator);
		if (a == null) return false;
		a.getBase().addFlag(flag, param, not);
		return true;
	}

	/**
	 * Add action to activator
	 * @param activator Name of activator
	 * @param action Name of action
	 * @param param Parameters of action
	 * @return Does activator with this name exist
	 */
	public static boolean addAction(String activator, String action, String param) {
		Activator a = get(activator);
		if (a == null) return false;
		a.getBase().addAction(action, param);
		return true;
	}
	/**
	 * Add reaction to activator
	 * @param activator Name of activator
	 * @param action Name of action
	 * @param param Parameters of action
	 * @return Does activator with this name exist
	 */
	public static boolean addReaction(String activator, String action, String param) {
		Activator a = get(activator);
		if (a == null) return false;
		a.getBase().addReaction(action, param);
		return true;
	}

	/**
	 * Delete files in direction (?)
	 * @param dir Name of direction
	 */
	private static void delFiles(String dir) {
		dir += dir.isEmpty() ? "" : File.separator;
		File dirs = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separator + dir);
		for (File f : dirs.listFiles())
			if (f.isDirectory()) {
				delFiles(dir + f.getName());
				f.delete();
			} else {
				f.delete();
			}
	}

	/**
	 * Save activators
	 */
	public static void saveActivators() {
		delFiles("");
		for (String group : findGroupsFromActivators())
			saveActivators(group);
	}

	/**
	 * Get groups of loaded activators
	 * @return Set of groups
	 */
	private static Set<String> findGroupsFromActivators() {
		Set<String> grps = new HashSet<>();
		for (Activator act : activators.values())
			grps.add(act.getBase().getGroup());
		return grps;
	}

	/**
	 * Save activators of specific group
	 * @param group Name of group
	 */
	private static void saveActivators(String group) {
		String g = implode(group.split("/"));

		File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separator + g + ".yml");
		File dir = new File(f.getPath());
		if (!dir.exists()) dir.mkdirs();

		try {
			if (f.exists()) f.delete();
			f.createNewFile();
		} catch (Exception e) {
			Msg.logMessage("Failed to create configuration to file " + f.getAbsolutePath());
			e.printStackTrace();
			return;
		}
		YamlConfiguration cfg = new YamlConfiguration();
		for (Activator a : activators.values()) {
			if (a.getBase().getGroup().equalsIgnoreCase(group)) a.saveActivator(cfg.createSection(a.getType()+"."+a.getBase().getName()));
		}

		try {
			cfg.save(f);
		} catch (Exception e) {
			Msg.logMessage("Failed to save configuration to file " + f.getAbsolutePath());
			e.printStackTrace();
		}
	}

	/**
	 * Idk yet
	 */
	private static String implode(String... data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length - 1; i++) {
			//data.length - 1 => to not add separator at the end
			if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
				sb.append(data[i]);
				sb.append(File.separator);
			}
		}
		sb.append(data[data.length - 1].trim());
		return sb.toString();
	}

	/**
	 * Load activators from files
	 * @param group Name of group
	 * @param clear Clear group before load
	 */
	public static void loadActivators(String group, boolean clear) {
		File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separator + group + ".yml");
		if (!f.exists()) return;
		YamlConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(f);
		} catch (Exception e) {
			Msg.logMessage("Failed to load configuration from file " + f.getAbsolutePath());
			e.printStackTrace();
			return;
		}

		if(clear)
			clearGroup(group);

		for (String sType : cfg.getKeys(false)) {
			ActivatorType type = ActivatorType.getByName(sType);
			ConfigurationSection section = cfg.getConfigurationSection(sType);
			if (section == null) continue;
			for (String name : section.getKeys(false)) {
				if (type == null) {
					Msg.logOnce("cannotcreate" + sType + name, "Failed to create new activator. Type: " + sType + " Name: " + name);
					continue;
				}

				Activator a = loadActivator(type, name, group, cfg);
				if (a == null) {
					Msg.logOnce("cannotcreate2" + sType + name, "Failed to create new activator. Type: " + sType + " Name: " + name);
					continue;
				}
				if (!add(a))
					Msg.logOnce("cannotcreate3", "Failed to create new activator. Type: " + sType + " Name: " + name);
			}
		}
	}

	/**
	 * Create new activator
	 * @param type Type of activator
	 * @param name Name of activator
	 * @param group Name of group
	 * @param cfg Config-file of group
	 * @return New activator
	 */
	private static Activator loadActivator(ActivatorType type, String name, String group, YamlConfiguration cfg) {
		return type.load(name, group, cfg.getConfigurationSection(type + "." + name));
	}

	/**
	 * Clear whole group from activators
	 * @param name Name of group
	 */
	private static void clearGroup(String name) {
		for(ActivatorType type : activatorsMap.keySet()) {
			Iterator<Activator> iter = activatorsMap.get(type).iterator();
			while(iter.hasNext()) {
				Activator act = iter.next();
				if(!act.getBase().getName().equals(name))
					continue;
				activators.remove(name);
				iter.remove();
			}
		}
	}

	public static List<String> getActivatorsList() {
		List<String> list = new ArrayList<>();
		activators.values().forEach(act -> list.add("&a" + act.toString()));
		return list;
	}

	public static List<String> getActivatorsList(String sType) {
		List<String> list = new ArrayList<>();
		ActivatorType type = ActivatorType.getByName(sType);
		if(type != null)
			activatorsMap.get(type).forEach(act -> list.add("&a" + act.toString()));
		return list;
	}

	public static List<String> getActivatorsListGroup(String group) {
		List<String> list = new ArrayList<>();
		activators.values().stream().filter(act -> act.getBase().getGroup().equalsIgnoreCase(group)).forEach(act -> list.add(act.toString()));
		return list;
	}

	public static boolean activate(Activator act, RAStorage storage) {
		return act.getType() == storage.getType() && act.executeActivator(storage);
	}

	public static boolean activate(RAStorage storage) {
		boolean cancelParentEvent = false;
		for (Activator act : activatorsMap.get(storage.getType())) {
			if (act.executeActivator(storage)) cancelParentEvent = true;
		}
		return cancelParentEvent;
	}

	public static boolean copyAll(String actFrom, String actTo) {
		if (!contains(actFrom)) return false;
		if (!contains(actTo)) return false;
		copyActions(actFrom, actTo);
		copyReactions(actFrom, actTo);
		copyFlags(actFrom, actTo);
		return true;
	}

	public static boolean copyActions(String actFrom, String actTo) {
		if (!contains(actFrom)) return false;
		if (!contains(actTo)) return false;
		Activator afrom = get(actFrom);
		Activator ato = get(actTo);
		ato.getBase().clearActions();
		if (!afrom.getBase().getActions().isEmpty()) {
			for (StoredAction action : afrom.getBase().getActions())
				ato.getBase().addAction(action.getAction(), action.getValue());
		}
		return true;
	}

	public static boolean copyReactions(String actFrom, String actTo) {
		if (!contains(actFrom)) return false;
		if (!contains(actTo)) return false;
		Activator afrom = get(actFrom);
		Activator ato = get(actTo);
		ato.getBase().clearReactions();
		if (!afrom.getBase().getReactions().isEmpty()) {
			for (StoredAction action : afrom.getBase().getReactions())
				ato.getBase().addReaction(action.getAction(), action.getValue());
		}
		return true;
	}

	public static boolean copyFlags(String actFrom, String actTo) {
		if (!contains(actFrom)) return false;
		if (!contains(actTo)) return false;
		Activator afrom = get(actFrom);
		Activator ato = get(actTo);
		ato.getBase().clearFlags();
		if (!afrom.getBase().getFlags().isEmpty()) {
			for (StoredFlag flag : afrom.getBase().getFlags())
				ato.getBase().addFlag(flag.getFlag(), flag.getValue(), flag.isInverted());
		}
		return true;
	}

	public static boolean setGroup(String activator, String group) {
		if (!contains(activator)) return false;
		get(activator).getBase().setGroup(group);
		return true;
	}

	@SuppressWarnings("unused")
	public static String getGroup(String activator) {
		if (!contains(activator)) return "activator";
		return get(activator).getBase().getGroup();
	}

	public static Set<Activator> getActivators(ActivatorType type) {
		return activatorsMap.get(type);
	}

	@SuppressWarnings("unused")
	public static boolean stopExec(Player player, String actName) {
		return stopExec(player == null ? "" : player.getName(), actName);
	}

	public static boolean stopExec(String pstr, String actName) {
		stopexec.add(pstr + "#" + actName);
		return true;
	}


	public static boolean isStopped(Player player, String actName, boolean unstop) {
		return isStopped((player == null ? "" : player.getName()), actName, unstop);
	}

	public static boolean isStopped(String pstr, String actName, boolean unstop) {
		String id = pstr + "#" + actName;
		if (!stopexec.contains(id)) return false;
		if (unstop) stopexec.remove(id);
		return true;
	}


}
