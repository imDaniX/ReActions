package me.fromgate.reactions.actions;

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * Created by MaxDikiy on 5/6/2017.
 */
public class ActionPlayerId extends Action {
    // TODO: Refactoring

    @SuppressWarnings("deprecation")
    @Override
    public boolean execute(RaContext context, Param params) {
        String playerName = params.getParam("player", "");
        String varID = params.getParam("varid", "");
        String varName = params.getParam("varname", "");

        UUID uniqueID;
        String uuid;
        String pName;

        if (playerName.isEmpty()) {
            uniqueID = Util.getUUID(playerName);
            uuid = uniqueID.toString();
            pName = context.getPlayer().getName();
        } else {
            OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerName);
            uuid = Util.getUUID(offPlayer).toString();
            pName = offPlayer.getName();
        }
        if (pName == null) pName = "";
        if (!Util.isStringEmpty(varID)) Variables.setVar(playerName, varID, uuid);
        if (!Util.isStringEmpty(varName)) Variables.setVar(playerName, varName, pName);
        context.setTempVariable("playerid", uuid);
        context.setTempVariable("playername", pName);
        return true;
    }

}
