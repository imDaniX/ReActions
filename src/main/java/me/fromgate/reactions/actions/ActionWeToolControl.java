package me.fromgate.reactions.actions;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 18/10/2017.
 */
public class ActionWeToolControl extends Action {
    @Override
    public boolean execute(RaContext context, Param params) {
        Player player = context.getPlayer();
        boolean isToolControl = params.getParam("value", params.getParam("param-line", false));
        if(params.hasAnyParam("player"))
            player = Util.getPlayerExact(params.getParam("player"));

        RaWorldEdit.getSession(player).setToolControl(isToolControl);
        return true;
    }
}
