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

import me.fromgate.reactions.storages.FactionRelationStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FactionRelationActivator extends Activator {

	private final Set<String> factions;
	private final String oldRelation;
	private final String newRelation;

	public FactionRelationActivator(ActivatorBase base, String faction, String otherFaction, String oldRelation, String newRelation) {
		super(base);
		this.factions = new HashSet<>();
		this.factions.add(faction.toUpperCase());
		this.factions.add(otherFaction.toUpperCase());
		this.oldRelation = oldRelation;
		this.newRelation = newRelation;
	}


	private boolean mustExecute(String faction1, String faction2, String oldRelation, String newRelation) {
		if (!isFactionRelated(faction1, faction2)) return false;
		return isRelation(this.oldRelation, oldRelation) && isRelation(this.newRelation, newRelation);
	}

	private boolean isRelation(String relation1, String relation2) {
		if (relation1.isEmpty()) return true;
		if (relation1.equalsIgnoreCase("any")) return true;
		return relation1.equalsIgnoreCase(relation2);
	}

	private boolean isFactionRelated(String faction1, String faction2) {
		if (factions.isEmpty()) return true;
		if ((factions.size() == 1) && factions.contains("ANY")) return true;
		if (!factions.contains("ANY"))
			return factions.contains(faction1.toUpperCase()) && factions.contains(faction2.toUpperCase());
		return factions.contains(faction1.toUpperCase()) || factions.contains(faction2.toUpperCase());
	}

	@Override
	public boolean activate(Storage event) {
		FactionRelationStorage fe = (FactionRelationStorage) event;
		if (!mustExecute(fe.getFaction(), fe.getOtherFaction(), fe.getOldRelation(), fe.getNewRelation())) return false;
		return true;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		List<String> factionList = new ArrayList<>(this.factions);
		cfg.set("factions", factionList);
		cfg.set("old-relation", this.oldRelation);
		cfg.set("new-relation", this.newRelation);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.FCT_RELATION;
	}

	public static FactionRelationActivator create(ActivatorBase base, Param param) {
		String faction = param.getParam("faction", param.getParam("faction1", "ANY"));
		String otherFaction = param.getParam("faction2", "ANY");
		String oldRelation = param.getParam("oldrelation", "ANY");
		String newRelation = param.getParam("newrelation", "ANY");
		return new FactionRelationActivator(base, faction, otherFaction, oldRelation, newRelation);
	}

	public static FactionRelationActivator load(ActivatorBase base, ConfigurationSection cfg) {
		List<String> factions = cfg.getStringList("factions");
		String oldRelation = cfg.getString("old-relation", "ANY");
		String newRelation = cfg.getString("new-relation", "ANY");
		return new FactionRelationActivator(base, factions.isEmpty()?"UNKNOWN":factions.get(0), factions.size()<2?"UNKNOWN":factions.get(1), oldRelation, newRelation);
	}
}
