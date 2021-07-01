package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.logic.activity.actions.OldAction;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * Created by MaxDikiy on 5/6/2017.
 */
public class ActionPlayerId extends OldAction {

    @SuppressWarnings("deprecation")
    @Override
    protected boolean execute(RaContext context, Parameters params) {
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
                ReActions.getVariables().setVariable(playerParam, varID, uuid);
            String varName = params.getString("varname");
            if (!Utils.isStringEmpty(varName))
                ReActions.getVariables().setVariable(playerParam, varName, pName);
        }

        context.setVariable("playerid", uuid);
        context.setVariable("playername", pName);
        return true;
    }

}
