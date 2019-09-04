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

package me.fromgate.reactions.actions;

import lombok.Setter;
import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;


public abstract class Action {
	private Actions type = null;
	@Setter private String messageParam = "";
	private boolean actionExecuting = true;

	public void init(Actions at) {
		this.type = at;
	}

	public boolean isAction() {
		return this.actionExecuting;
	}

	public final boolean executeAction(Player player, boolean action, Param params) {
		this.actionExecuting = action;
		//this.activator = a;
		if (!params.hasAnyParam("param-line")) params.set("param-line", "");
		setMessageParam(params.getParam("param-line"));
		boolean actionFailed = (!execute(player, params));
		if ((player != null) && (printAction())) {
			Msg msg = Msg.getByName(("ACT_" + type.name() + (actionFailed ? "FAIL" : "")).toUpperCase());
			if (msg == null) {
				Msg.LNG_FAIL_ACTION_MSG.print(type.name());
			} else {
				msg.print(player, messageParam);
			}
		}
		//Залипухи, но похоже по другому - никак...
		//if (a==null) return true;
		//if ((a.getType() == ActivatorType.COMMAND)&&(!((CommandActivator) a).isCommandRegistered())) return true;
		return (this.type == Actions.CANCEL_EVENT) && (!actionFailed);
	}

	private boolean printAction() {
		return (Util.isWordInList(this.type.name(), Cfg.actionMsg) || Util.isWordInList(this.type.getAlias(), Cfg.actionMsg));
	}

	public abstract boolean execute(Player p, Param params);
}
