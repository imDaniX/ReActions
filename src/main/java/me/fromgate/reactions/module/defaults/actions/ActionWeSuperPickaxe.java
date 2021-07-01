package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.logic.activity.actions.OldAction;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 18/10/2017.
 */
public class ActionWeSuperPickaxe extends OldAction {
    @Override
    protected boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        boolean isSP = params.getBoolean("value", params.getBoolean("param-line", false));
        if (params.contains("player"))
            player = Utils.getPlayerExact(params.getString("player"));
        if (isSP) RaWorldEdit.getSession(player).enableSuperPickAxe();
        else RaWorldEdit.getSession(player).disableSuperPickAxe();
        return true;

    }
}
