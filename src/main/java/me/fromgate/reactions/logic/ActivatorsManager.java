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

package me.fromgate.reactions.logic;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.logic.actions.StoredAction;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.ActivatorLogic;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.logic.activators.Locatable;
import me.fromgate.reactions.logic.flags.Flags;
import me.fromgate.reactions.logic.flags.StoredFlag;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.FileUtils;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ActivatorsManager {

    private Map<ActivatorType, Set<Activator>> typeActivators;
    // Names in lower case
    private Map<String, Activator> activators;
    private Set<String> stopexec;
    
    public ActivatorsManager() {
        typeActivators = new EnumMap<>(ActivatorType.class);
        for (ActivatorType type : ActivatorType.values())
            typeActivators.put(type, new HashSet<>());
        activators = new HashMap<>();
        stopexec = new HashSet<>();
    }

    /**
     * Load activators
     */
    public void loadActivators() {
        Set<String> groups = findGroups("");
        if (!groups.isEmpty())
            for (String group : groups)
                loadGroup(group, false);
        RaWorldGuard.updateRegionCache();
    }

    /**
     * Load activators from group
     */
    public void loadActivators(String group) {
        loadGroup(group, true);
        RaWorldGuard.updateRegionCache();
    }

    /**
     * Get file names of activator groups
     *
     * @param dir Name of folder
     * @return Set of file names
     */
    private Set<String> findGroups(String dir) {
        dir = ((dir.isEmpty()) ? "" : dir + File.separator);
        Set<String> grps = new HashSet<>();
        File dirs = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separator + dir);
        if (!dirs.exists()) dirs.mkdirs();
        for (File f : dirs.listFiles()) {
            if (f.isDirectory()) {
                grps.addAll(findGroups(dir + f.getName()));
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
     *
     * @return Activators count
     */
    public int size() {
        return activators.size();
    }

    /**
     * Clear all the activators
     */
    public void clear() {
        typeActivators.values().forEach(Set::clear);
        activators.clear();
    }

    /**
     * Get all activators in specific location
     *
     * @param world World to check
     * @param x     Coordinate x to check
     * @param y     Coordinate y to check
     * @param z     Coordinate z to check
     * @return List of activators in location
     */
    public List<Activator> getActivatorInLocation(World world, int x, int y, int z) {
        List<Activator> found = new ArrayList<>();
        for (ActivatorType type : ActivatorType.values())
            if (type.isLocatable())
                typeActivators.get(type).stream().filter(act -> ((Locatable) act).isLocatedAt(world, x, y, z)).forEach(found::add);
        return found;
    }

    /**
     * Add activator if activator with it's name don't exist
     *
     * @param act Activator to add
     * @return Was activator added or not
     */
    public boolean addActivator(Activator act) {
        if (containsActivator(act.getLogic().getName().toLowerCase(Locale.ENGLISH))) return false;
        typeActivators.get(act.getType()).add(act);
        activators.put(act.getLogic().getName().toLowerCase(Locale.ENGLISH), act);
        return true;
    }

    /**
     * Remove activator by name
     *
     * @param name Name of activator to remove
     */
    public void removeActivator(String name) {
        Activator act = activators.remove(name.toLowerCase(Locale.ENGLISH));
        if (act != null)
            typeActivators.get(act.getType()).remove(act);
    }

    /**
     * Check existing of activator by name
     *
     * @param name Name of activator to search
     * @return Does activator with this name exist
     */
    public boolean containsActivator(String name) {
        return activators.containsKey(name.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Get activator by name
     *
     * @param name Name of activator to search
     * @return Activator or null
     */
    public Activator getActivator(String name) {
        return activators.get(name.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Clear flags of activator
     *
     * @param name Name of activator
     * @return Does activator with this name exist
     */
    public boolean clearFlags(String name) {
        Activator a = getActivator(name);
        if (a == null) return false;
        a.getLogic().clearFlags();
        return true;
    }

    /**
     * Clear actions of activator
     *
     * @param name Name of activator
     * @return Does activator with this name exist
     */
    public boolean clearActions(String name) {
        Activator a = getActivator(name);
        if (a == null) return false;
        a.getLogic().clearActions();
        return true;
    }

    /**
     * Clear reactions of activator
     *
     * @param name Name of activator
     * @return Does activator with this name exist
     */
    public boolean clearReactions(String name) {
        Activator a = getActivator(name);
        if (a == null) return false;
        a.getLogic().clearReactions();
        return true;
    }

    /**
     * Add flag to activator
     *
     * @param activator Name of activator
     * @param flag      Name of flag
     * @param param     Parameters of flag
     * @param not       Invert flag
     * @return Does activator with this name exist
     */
    public boolean addFlag(String activator, String flag, String param, boolean not) {
        Activator a = getActivator(activator);
        if (a == null) return false;
        a.getLogic().addFlag(Flags.getByName(not ? flag.substring(1) : flag), param, not);
        return true;
    }

    /**
     * Add action to activator
     *
     * @param activator Name of activator
     * @param action    Name of action
     * @param param     Parameters of action
     * @return Does activator with this name exist
     */
    public boolean addAction(String activator, String action, String param) {
        Activator a = getActivator(activator);
        if (a == null) return false;
        a.getLogic().addAction(action, param);
        return true;
    }

    /**
     * Add reaction to activator
     *
     * @param activator Name of activator
     * @param action    Name of action
     * @param param     Parameters of action
     * @return Does activator with this name exist
     */
    public boolean addReaction(String activator, String action, String param) {
        Activator a = getActivator(activator);
        if (a == null) return false;
        a.getLogic().addReaction(action, param);
        return true;
    }

    /**
     * Delete files in direction (?)
     *
     * @param dir Name of direction
     */
    private void deleteFiles(String dir) {
        dir += dir.isEmpty() ? "" : File.separator;
        File dirs = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separator + dir);
        for (File f : dirs.listFiles()) {
            if (f.isDirectory())
                deleteFiles(dir + f.getName());
            f.delete();
        }
    }

    /**
     * Save activators
     */
    public void saveActivators() {
        deleteFiles("");
        for (Activator act : activators.values())
            saveActivators(act.getLogic().getGroup());
    }

    /**
     * Save activators of specific group
     *
     * @param group Name of group
     */
    public void saveActivators(String group) {
        String g = Utils.implode(group.split("/"));

        File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separator + g + ".yml");
        File dir = f.getParentFile();
        if (!dir.exists()) dir.mkdirs();

        YamlConfiguration cfg = new YamlConfiguration();
        for (Activator a : activators.values()) {
            if (a.getLogic().getGroup().equalsIgnoreCase(group))
                a.saveActivator(cfg.createSection(a.getType().name() + "." + a.getLogic().getName()));
        }

        FileUtils.saveCfg(cfg, f, "Failed to save activators to file " + f.getAbsolutePath());
    }

    /**
     * Load activators from file
     *
     * @param group Path/name of group
     * @param clear Clear group before load
     */
    private void loadGroup(String group, boolean clear) {
        File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "Activators" + File.separator + group + ".yml");
        if (!f.exists()) return;
        YamlConfiguration cfg = new YamlConfiguration();
        FileUtils.loadCfg(cfg, f, "Failed to load configuration from file " + f.getAbsolutePath());

        if (clear)
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
                if (!addActivator(a))
                    Msg.logOnce("cannotcreate3", "Failed to create new activator. Type: " + sType + " Name: " + name);
            }
        }
    }

    /**
     * Create new activator
     *
     * @param type  Type of activator
     * @param name  Name of activator
     * @param group Name of group
     * @param cfg   Config-file of group
     * @return New activator
     */
    private Activator loadActivator(ActivatorType type, String name, String group, YamlConfiguration cfg) {
        return type.load(name, group, cfg.getConfigurationSection(type.name() + "." + name));
    }

    /**
     * Clear whole group from activators
     *
     * @param name Name of group
     */
    private void clearGroup(String name) {
        for (ActivatorType type : typeActivators.keySet()) {
            Iterator<Activator> iter = typeActivators.get(type).iterator();
            while (iter.hasNext()) {
                ActivatorLogic base = iter.next().getLogic();
                if (!base.getGroup().equalsIgnoreCase(name))
                    continue;
                activators.remove(base.getName().toLowerCase(Locale.ENGLISH));
                iter.remove();
            }
        }
    }

    /**
     * Get names of all the activators
     *
     * @return List of names
     */
    public List<String> getNames() {
        List<String> list = new ArrayList<>();
        activators.values().forEach(act -> list.add("&a" + act.toString()));
        return list;
    }

    /**
     * Get names of activators of specific type
     *
     * @param sType Name of type
     * @return List of names
     */
    public List<String> getNamesByType(String sType) {
        List<String> list = new ArrayList<>();
        ActivatorType type = ActivatorType.getByName(sType);
        if (type != null)
            typeActivators.get(type).forEach(act -> list.add("&a" + act.toString()));
        return list;
    }

    /**
     * Get names of activator from specific group
     *
     * @param group Name of group
     * @return List of names
     */
    public List<String> getNamesByGroup(String group) {
        List<String> list = new ArrayList<>();
        activators.values().stream().filter(act -> act.getLogic().getGroup().equalsIgnoreCase(group)).forEach(act -> list.add(act.toString()));
        return list;
    }

    /**
     * Activate specific activator by it's id(name)
     *
     * @param storage Data storage for activator
     * @param id      Name of activator
     */
    public void activate(Storage storage, String id) {
        Activator activator = activators.get(id.toLowerCase(Locale.ENGLISH));
        activate(storage, activator);
    }

    /**
     * Activate specific activator
     *
     * @param storage Data storage for activator
     * @param act     Activator to activate
     */
    public void activate(Storage storage, Activator act) {
        storage.init();
        if (act.getType() == storage.getType())
            act.executeActivator(storage);
    }

    /**
     * Activate all the activator of storage's type
     *
     * @param storage Data storage for activators
     */
    public void activate(Storage storage) {
        storage.init();
        typeActivators.get(storage.getType()).forEach(a -> a.executeActivator(storage));
    }

    /**
     * Copy all actions, reactions and flags
     *
     * @param actFrom Activator to copy
     * @param actTo   Activator where to copy
     * @return Does these activators exist
     */
    public boolean copyAll(String actFrom, String actTo) {
        // TODO: Small optimization
        if (!containsActivator(actFrom)) return false;
        if (!containsActivator(actTo)) return false;
        copyActions(actFrom, actTo);
        copyReactions(actFrom, actTo);
        copyFlags(actFrom, actTo);
        return true;
    }

    /**
     * Copy all actions
     *
     * @param actFrom Activator to copy
     * @param actTo   Activator where to copy
     * @return Does these activators exist
     */
    public boolean copyActions(String actFrom, String actTo) {
        if (!containsActivator(actFrom)) return false;
        if (!containsActivator(actTo)) return false;
        Activator afrom = getActivator(actFrom);
        Activator ato = getActivator(actTo);
        ato.getLogic().clearActions();
        if (!afrom.getLogic().getActions().isEmpty()) {
            for (StoredAction action : afrom.getLogic().getActions())
                ato.getLogic().addAction(action.getAction(), action.getValue());
        }
        return true;
    }

    /**
     * Copy all reactions
     *
     * @param actFrom Activator to copy
     * @param actTo   Activator where to copy
     * @return Does these activators exist
     */
    public boolean copyReactions(String actFrom, String actTo) {
        if (!containsActivator(actFrom)) return false;
        if (!containsActivator(actTo)) return false;
        Activator afrom = getActivator(actFrom);
        Activator ato = getActivator(actTo);
        ato.getLogic().clearReactions();
        if (!afrom.getLogic().getReactions().isEmpty()) {
            for (StoredAction action : afrom.getLogic().getReactions())
                ato.getLogic().addReaction(action.getAction(), action.getValue());
        }
        return true;
    }

    /**
     * Copy all flags
     *
     * @param actFrom Activator to copy
     * @param actTo   Activator where to copy
     * @return Does these activators exist
     */
    public boolean copyFlags(String actFrom, String actTo) {
        if (!containsActivator(actFrom)) return false;
        if (!containsActivator(actTo)) return false;
        Activator afrom = getActivator(actFrom);
        Activator ato = getActivator(actTo);
        ato.getLogic().clearFlags();
        if (!afrom.getLogic().getFlags().isEmpty()) {
            for (StoredFlag flag : afrom.getLogic().getFlags())
                ato.getLogic().addFlag(flag.getFlag(), flag.getValue(), flag.isInverted());
        }
        return true;
    }

    /**
     * Set group of activator
     *
     * @param activator Name of activator to edit
     * @param group     Name of group
     * @return Does activator with this name exists
     */
    public boolean setGroup(String activator, String group) {
        Activator a = getActivator(activator);
        if (a == null) return false;
        a.getLogic().setGroup(group);
        return true;
    }

    public String getGroup(String activator) {
        if (!containsActivator(activator)) return "activator";
        return getActivator(activator).getLogic().getGroup();
    }

    /**
     * Get set activators by type
     *
     * @param type Type of activators
     * @return Set of activators
     */
    public Set<Activator> getActivators(ActivatorType type) {
        return typeActivators.get(type);
    }

    public boolean stopExec(String pstr, String actName) {
        stopexec.add(pstr + "#" + actName);
        return true;
    }


    public boolean isStopped(Player player, String actName, boolean unstop) {
        return isStopped((player == null ? "" : player.getName()), actName, unstop);
    }

    public boolean isStopped(String pstr, String actName, boolean unstop) {
        String id = pstr + "#" + actName;
        if (!stopexec.contains(id)) return false;
        if (unstop) stopexec.remove(id);
        return true;
    }


}
