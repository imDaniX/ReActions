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
import me.fromgate.reactions.storage.FactionChangeStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.configuration.ConfigurationSection;

public class FactionActivator extends Activator {

	private final String newFaction;
	private final String oldFaction;

	public FactionActivator(ActivatorBase base, String newFaction, String oldFaction) {
		super(base);
		this.newFaction = newFaction;
		this.oldFaction = oldFaction;
	}

	@Override
	public boolean activate(RAStorage event) {
		FactionChangeStorage fe = (FactionChangeStorage) event;
		if (!(newFaction.isEmpty() || newFaction.equalsIgnoreCase("any") || fe.getNewFaction().equalsIgnoreCase(newFaction)))
			return false;
		if (!(oldFaction.isEmpty() || oldFaction.equalsIgnoreCase("any") || fe.getOldFaction().equalsIgnoreCase(oldFaction)))
			return false;
		Variables.setTempVar("newfaction", fe.getNewFaction());
		Variables.setTempVar("oldfaction", fe.getOldFaction());
		return Actions.executeActivator(fe.getPlayer(), getBase());
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("new-faction", newFaction.isEmpty() ? "ANY" : newFaction);
		cfg.set("old-faction", oldFaction.isEmpty() ? "ANY" : oldFaction);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.FCT_CHANGE;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("old faction:").append(this.oldFaction);
		sb.append("; new faction:").append(this.newFaction);
		sb.append(")");
		return sb.toString();
	}

	public static FactionActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String newFaction = cfg.getString("new-faction", "ANY");
		String oldFaction = cfg.getString("old-faction", "ANY");
		return new FactionActivator(base, newFaction, oldFaction);
	}

	public static FactionActivator create(ActivatorBase base, Param param) {
		String newFaction = param.getParam("newfaction", param.getParam("faction", "ANY"));
		String oldFaction = param.getParam("oldfaction", "ANY");
		return new FactionActivator(base, newFaction, oldFaction);
	}
}
