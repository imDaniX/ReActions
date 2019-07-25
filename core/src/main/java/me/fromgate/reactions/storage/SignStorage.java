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


import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.location.Locator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SignStorage extends RAStorage {

	private String[] signLines;
	private final Location loc;
	private final boolean leftClick;

	public SignStorage(Player player, String[] signLines, Location loc, boolean leftClick) {
		super(player, ActivatorType.SIGN);
		this.signLines = signLines;
		this.loc = loc;
		this.leftClick = leftClick;
	}

	public String[] getSignLines() {
		return this.signLines;
	}

	public String getSignLocation() {
		return Locator.locationToString(this.loc);
	}

	public boolean isLeftClicked() {
		return leftClick;
	}


}
