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

package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;

public class FactionRelationStorage extends RAStorage {
	@Getter private final String faction;
	@Getter private final String otherFaction;
	@Getter private final String oldRelation;
	@Getter private final String newRelation;

	public FactionRelationStorage(String faction, String otherFaction, String oldRelation, String newRelation) {
		super(null, ActivatorType.FCT_RELATION);
		this.oldRelation = oldRelation;
		this.newRelation = newRelation;
		this.faction = faction;
		this.otherFaction = otherFaction;
	}
}
