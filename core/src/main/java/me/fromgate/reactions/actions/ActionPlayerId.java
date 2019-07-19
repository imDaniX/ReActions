package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by MaxDikiy on 5/6/2017.
 */
public class ActionPlayerId extends Action {
	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(Player p, Param params) {
		String playerName = params.getParam("player", "");
		String varID = params.getParam("varid", "");
		String varName = params.getParam("varname", "");

		UUID uniqueID;
		String uuid;
		String pName;

		if (playerName.isEmpty()) {
			uniqueID = Util.getUUID(p);
			uuid = uniqueID.toString();
			pName = p.getName();
		} else {
			OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerName);
			uuid = Util.getUUID(offPlayer).toString();
			pName = offPlayer.getName();
		}
		if (pName == null) pName = "";
		Variables.setVar(playerName, varID, uuid);
		Variables.setVar(playerName, varName, pName);
		Variables.setTempVar("playerid", uuid);
		Variables.setTempVar("playername", pName);
		return true;
	}

}
