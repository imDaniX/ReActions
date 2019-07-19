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
import me.fromgate.reactions.event.RAEvent;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.actions.StoredAction;
import me.fromgate.reactions.flags.StoredFlag;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Activators {

	private static HashMap<ActivatorType, Set<Activator>> activatorsMap;
	private static Set<Activator> activators;
	private static Set<String> stopexec;

	/**
	 * Initialize variables
	 */
	public static void init() {
		activatorsMap = new HashMap<>();
		for(ActivatorType type : ActivatorType.values())
			activatorsMap.put(type, new HashSet<>());
		activators = new HashSet<>();
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
		Timers.updateIngameTimers();
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
		return getActivatorInLocation(new Location(world, x, y, z));
	}

	/**
	 * Get all activators in specific location
	 * @param loc Location to check
	 * @return Set of activators in location
	 */
	public static Set<Activator> getActivatorInLocation(Location loc) {
		Set<Activator> found = new HashSet<>();
		for (ActivatorType type : ActivatorType.values())
			if(type.isLocated())
				activatorsMap.get(type).stream().filter(act -> act.isLocatedAt(loc)).forEach(found::add);
		return found;
	}

	/**
	 * Add activator if activator with it's name don't exist
	 * @param act Activator to add
	 * @return Was activator added or not
	 */
	public static boolean add(Activator act) {
		if (contains(act.name)) return false;
		addActivator(act);
		return true;
	}

	/**
	 * Just adding activator
	 * @param act Activator to add
	 */
	private static void addActivator(Activator act) {
		activatorsMap.get(act.getType()).add(act);
		activators.add(act);
	}

	/**
	 * Remove activator by name
	 * @param name Name of activator to remove
	 */
	public static void removeActivator(String name) {
		for(Set<Activator> acts : activatorsMap.values()) {
			Iterator<Activator> iterator = acts.iterator();
			while(iterator.hasNext())
				if(iterator.next().getName().equals(name)) {
					iterator.remove();
					break;
				}
		}
	}

	/**
	 * Check existing of activator by name
	 * @param name Name of activator to search
	 * @return Does activator with this name exist
	 */
	public static boolean contains(String name) {
		for(Activator act : activators)
			if(act.getName().equals(name))
				return true;
		return false;
	}

	/**
	 * Get activator by name
	 * @param name Name of activator to search
	 * @return Activator or null
	 */
	public static Activator get(String name) {
		for(Activator act : activators)
			if(act.getName().equals(name))
				return act;
		return null;
	}

	/**
	 * Clear flags of activator
	 * @param name Name of activator
	 * @return Does activator with this name exist
	 */
	public static boolean clearFlags(String name) {
		Activator a = get(name);
		if (a == null) return false;
		a.clearFlags();
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
		a.clearActions();
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
		a.clearReactions();
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
		a.addFlag(flag, param, not);
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
		a.addAction(action, param);
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
		a.addReaction(action, param);
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
		for (Activator act : activators)
			grps.add(act.getGroup());
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
		for (Activator a : activators) {
			if (a.group.equalsIgnoreCase(group)) a.saveActivator(cfg);
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
		File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separatorChar + group + ".yml");
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

				Activator a = createActivator(type, name, group, cfg);
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
	private static Activator createActivator(ActivatorType type, String name, String group, YamlConfiguration cfg) {
		try {
			// I don't like it
			return type.getActivatorClass().getDeclaredConstructor(String.class, String.class, YamlConfiguration.class).newInstance(name, group, cfg);
		} catch (Exception e) {
			Msg.logOnce("cannotcreate" + name, "Failed to create new activator. Name: " + name);
			e.printStackTrace();
		}
		return null;
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
				if(!act.getName().equals(name))
					continue;
				activators.remove(act);
				iter.remove();
			}
		}
	}

	public static Set<String> getActivatorsSet() {
		Set<String> set = new HashSet<>();
		activators.forEach(act -> set.add("&a" + act.toString()));
		return set;
	}

	public static Set<String> getActivatorsSet(String sType) {
		Set<String> set = new HashSet<>();
		ActivatorType type = ActivatorType.getByName(sType);
		if(type != null)
			activatorsMap.get(type).forEach(act -> set.add("&a" + act.toString()));
		return set;
	}

	public static Set<String> getActivatorsSetGroup(String group) {
		Set<String> set = new HashSet<>();
		activators.stream().filter(act -> act.getName().equalsIgnoreCase(group)).forEach(act -> set.add(act.toString()));
		return set;
	}

	public static boolean activate(RAEvent event) {
		boolean cancelParentEvent = false;
		for (Activator act : activatorsMap.get(event.getType())) {
			if (act.executeActivator(event)) cancelParentEvent = true;
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
		ato.clearActions();
		if (!afrom.getActions().isEmpty()) {
			for (StoredAction action : afrom.getActions())
				ato.addAction(action.flag, action.value);
		}
		return true;
	}

	public static boolean copyReactions(String actFrom, String actTo) {
		if (!contains(actFrom)) return false;
		if (!contains(actTo)) return false;
		Activator afrom = get(actFrom);
		Activator ato = get(actTo);
		ato.clearReactions();
		if (!afrom.getReactions().isEmpty()) {
			for (StoredAction action : afrom.getReactions())
				ato.addReaction(action.flag, action.value);
		}
		return true;
	}

	public static boolean copyFlags(String actFrom, String actTo) {
		if (!contains(actFrom)) return false;
		if (!contains(actTo)) return false;
		Activator afrom = get(actFrom);
		Activator ato = get(actTo);
		ato.clearFlags();
		if (!afrom.getFlags().isEmpty()) {
			for (StoredFlag flag : afrom.getFlags())
				ato.addFlag(flag.flag, flag.value, flag.not);
		}
		return true;
	}

	public static boolean setGroup(String activator, String group) {
		if (!contains(activator)) return false;
		get(activator).setGroup(group);
		return true;
	}

	@SuppressWarnings("unused")
	public static String getGroup(String activator) {
		if (!contains(activator)) return "activator";
		return get(activator).getGroup();
	}

	public static Set<Activator> getActivators(ActivatorType type) {
		return activatorsMap.get(type);
	}

	/* public static List<TimeIngameActivator> getTimeIngameActivators() {
		List<TimeIngameActivator> timeIngameAct= new ArrayList<TimeIngameActivator>();
		for (Activator a : act)
			if (a.getType() == ActivatorType.TIME_INGAME) timeIngameAct.add((TimeIngameActivator) a);
		return timeIngameAct;
	}

	public static List<TimeServerActivator> getTimeServerActivators() {
		List<TimeServerActivator> timeServerAct= new ArrayList<TimeServerActivator>();
		for (Activator a : act)
			if (a.getType() == ActivatorType.TIME_SERVER) timeServerAct.add((TimeServerActivator) a);
		return timeServerAct;
	} */

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
