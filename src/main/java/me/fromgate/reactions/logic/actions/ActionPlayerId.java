package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.VariablesManager;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * Created by MaxDikiy on 5/6/2017.
 */
public class ActionPlayerId extends Action {

    @SuppressWarnings("deprecation")
    @Override
    public boolean execute(RaContext context, Parameters params) {
        String uuid;
        String pName;

        String playerParam = params.getString("player");

        if (Utils.isStringEmpty(playerParam)) {
            uuid = context.getPlayer().getUniqueId().toString();
            pName = context.getPlayer().getName();
        } else {
            OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerParam);
            uuid = Utils.getUUID(offPlayer).toString();
            pName = offPlayer.getName();
            if (pName == null)
                pName = "";

            String varID = params.getString("varid");
            if (!Utils.isStringEmpty(varID))
                VariablesManager.getInstance().setVar(playerParam, varID, uuid);
            String varName = params.getString("varname");
            if (!Utils.isStringEmpty(varName))
                VariablesManager.getInstance().setVar(playerParam, varName, pName);
        }

        context.setVariable("playerid", uuid);
        context.setVariable("playername", pName);
        return true;
    }

}
