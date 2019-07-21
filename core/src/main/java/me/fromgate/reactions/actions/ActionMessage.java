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

import com.google.common.base.Joiner;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.timer.Time;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.playerselector.SelectorsManager;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;

public class ActionMessage extends Action {

	@Override
	public boolean execute(Player p, Param params) {
		sendMessage(p, params);
		return true;
	}

	private String removeParams(String message) {
		String sb = "(?i)(" + Joiner.on("|").join(SelectorsManager.getAllKeys()) +
				"|hide):(\\{.*\\}|\\S+)\\s{0,1}";
		return message.replaceAll(sb, "");
		//String message = params.getParam("text", params.getParam("param-line").replaceAll("(?i)(region|loc|radius|rgplayer|player|world|faction|group|perm):(\\{.*\\}|\\S+)\\s{0,1}", ""));

	}

	private void sendMessage(Player player, Param params) {
		Set<Player> players = new HashSet<>();
		if (params.hasAnyParam(SelectorsManager.getAllKeys())) {
			players.addAll(SelectorsManager.getPlayerList(params));
			if (players.isEmpty() && params.isParamsExists("player")) {
				players.addAll(SelectorsManager.getPlayerList(new Param(params.getParam("player"))));
			}
		} else if (player != null) {
			players.add(player);
		}
		if (players.isEmpty()) return;

		String message = params.getParam("text", removeParams(params.getParam("param-line")));
		if (message.isEmpty()) return;
		String annoymentTime = params.getParam("hide");
		for (Player p : players) {
			if (showMessage(p, message, annoymentTime)) {
				Msg.printMessage(p, message);
			}
		}
	}


	private boolean showMessage(Player player, String message, String annoymentTime) {
		if (annoymentTime.isEmpty()) return true;
		long time = Time.parseTime(annoymentTime);
		if (time == 0) return false;
		String key = "reactions-msg-" +/*.append(this.getActivatorName())*/message.hashCode() + (this.isAction() ? "act" : "react");
		if (player.hasMetadata(key)) {
			if ((player.getMetadata(key).get(0).asLong() - System.currentTimeMillis()) > 0)
				return false;
		}
		player.setMetadata(key, new FixedMetadataValue(ReActions.getPlugin(), System.currentTimeMillis() + time));
		return true;
	}
}
