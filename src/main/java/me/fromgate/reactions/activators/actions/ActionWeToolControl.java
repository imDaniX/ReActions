package me.fromgate.reactions.activators.actions;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 18/10/2017.
 */
public class ActionWeToolControl extends Action {
    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        boolean isToolControl = params.getBoolean("value", params.getBoolean("param-line", false));
        if (params.contains("player"))
            player = Utils.getPlayerExact(params.getString("player"));

        RaWorldEdit.getSession(player).setToolControl(isToolControl);
        return true;
    }
}
