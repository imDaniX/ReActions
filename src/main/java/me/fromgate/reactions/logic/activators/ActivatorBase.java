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

package me.fromgate.reactions.logic.activators;

import lombok.Getter;
import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.logic.actions.Actions;
import me.fromgate.reactions.logic.actions.StoredAction;
import me.fromgate.reactions.logic.flags.Flags;
import me.fromgate.reactions.logic.flags.StoredFlag;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ActivatorBase {
    private int hash;

    @Getter
    private String name;
    @Getter
    private String group;
    @Getter
    private List<StoredFlag> flags = new ArrayList<>();
    @Getter
    private List<StoredAction> actions = new ArrayList<>();
    @Getter
    private List<StoredAction> reactions = new ArrayList<>();

    public ActivatorBase(String name, String group) {
        this.name = name;
        this.group = Utils.isStringEmpty(group) ? "activators" : group;
    }

    public ActivatorBase(String name, String group, ConfigurationSection cfg) {
        this.name = name;
        this.group = group == null ? "activators" : group;
        loadData(cfg.getStringList("flags"), this::addFlag);
        loadData(cfg.getStringList("actions"), this::addAction);
        loadData(cfg.getStringList("reactions"), this::addReaction);
    }

    private static void loadData(List<String> data, BiConsumer<String, String> loader) {
        if (data == null) return;
        for (String str : data) {
            String param = str;
            String value = "";
            int index = str.indexOf("=");
            if (index != -1) {
                param = str.substring(0, index);
                value = str.substring(index + 1);
            }
            loader.accept(param, value);
        }
    }

    public void setName(String name) {
        this.name = name;
        calcHash();
    }

    public void setGroup(String group) {
        this.group = group;
        calcHash();
    }

    /**
     * Add flag to activator
     *
     * @param flag  Name of flag to add
     * @param param Parameters of flag
     */
    public void addFlag(String flag, String param) {
        boolean not = flag.startsWith("!");
        addFlag(Flags.getByName(not ? flag.substring(1) : flag), param, not);
    }

    /**
     * Add flag to activator
     *
     * @param flag  Flag to add
     * @param param Parameters of flag
     * @param not   Is indentation needed
     */
    public void addFlag(Flags flag, String param, boolean not) {
        StoredFlag flg = new StoredFlag(flag, param, not);
        if (flg.getFlag() == null)
            Msg.logOnce("wrongflagname" + flags.size() + name, "Flag for activator " + name + " with this name does not exist.");
        else
            flags.add(flg);
    }

    /**
     * Remove flag from activator
     *
     * @param index Index of flag
     * @return Is there flag with this index
     */
    public boolean removeFlag(int index) {
        if (flags.size() <= index) return false;
        flags.remove(index);
        return true;
    }

    /**
     * Add action to activator
     *
     * @param action Name of action to add
     * @param param  Parameters of action
     */
    public void addAction(String action, String param) {
        addAction(Actions.getByName(action), param);
    }

    /**
     * Add action to activator
     *
     * @param action Action to add
     * @param param  Parameters of action
     */
    public void addAction(Actions action, String param) {
        StoredAction act = new StoredAction(action, param);
        if (act.getAction() == null)
            Msg.logOnce("wrongactname" + actions.size() + name, "Flag for activator " + name + " with this name does not exist.");
        else
            actions.add(act);
    }

    /**
     * Remove action from activator
     *
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
     *
     * @param action Name of action to add
     * @param param  Parameters of action
     */
    public void addReaction(String action, String param) {
        addReaction(Actions.getByName(action), param);
    }

    /**
     * Add reaction to activator
     *
     * @param action Action to add
     * @param param  Parameters of action
     */
    public void addReaction(Actions action, String param) {
        StoredAction act = new StoredAction(action, param);
        if (act.getAction() == null)
            Msg.logOnce("wrongactname" + actions.size() + name, "Flag for activator " + name + " with this name does not exist.");
        else
            reactions.add(act);
    }

    /**
     * Remove reaction from activator
     *
     * @param index Index of action
     * @return Is there action with this index
     */
    public boolean removeReaction(int index) {
        if (reactions.size() <= index) return false;
        reactions.remove(index);
        return true;
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

    /**
     * Save activator to config
     *
     * @param cfg Config for activator
     */
    public void saveBase(ConfigurationSection cfg) {
        List<String> flg = new ArrayList<>();
        for (StoredFlag f : flags) flg.add(f.toString());
        cfg.set("flags", flg.isEmpty() && !Cfg.saveEmptySections ? null : flg);
        flg = new ArrayList<>();
        for (StoredAction a : actions) flg.add(a.toString());
        cfg.set("actions", flg.isEmpty() && !Cfg.saveEmptySections ? null : flg);
        flg = new ArrayList<>();
        for (StoredAction a : reactions) flg.add(a.toString());
        cfg.set("reactions", flg.isEmpty() && !Cfg.saveEmptySections ? null : flg);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof String)
            return name.equalsIgnoreCase((String) obj);
        if (!(obj instanceof ActivatorBase))
            return false;
        ActivatorBase other = (ActivatorBase) obj;
        if (name == null)
            return other.name == null;
        if (group == null)
            return other.group == null;
        return this.name.equals(other.name) && this.group.equals(other.group);
    }

    private void calcHash() {
        this.hash = group.hashCode() * 31 + name.hashCode() / 37;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        return sb.toString();
    }
}
