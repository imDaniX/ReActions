package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.VariablesManager;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
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
    public boolean execute(RaContext context, Parameters params) {
        String playerName = params.getString("player", "");
        String varID = params.getString("varid", "");
        String varName = params.getString("varname", "");

        UUID uniqueID;
        String uuid;
        String pName;

        if (playerName.isEmpty()) {
            uniqueID = Utils.getUUID(playerName);
            uuid = uniqueID.toString();
            pName = context.getPlayer().getName();
        } else {
            OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerName);
            uuid = Utils.getUUID(offPlayer).toString();
            pName = offPlayer.getName();
        }
        if (pName == null) pName = "";
        if (!Utils.isStringEmpty(varID)) VariablesManager.getInstance().setVar(playerName, varID, uuid);
        if (!Utils.isStringEmpty(varName)) VariablesManager.getInstance().setVar(playerName, varName, pName);
        context.setVariable("playerid", uuid);
        context.setVariable("playername", pName);
        return true;
    }

}
