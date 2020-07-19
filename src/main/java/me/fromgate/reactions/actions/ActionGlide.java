package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/8/2017.
 */
public class ActionGlide extends Action {
    @Override
    public boolean execute(RaContext context, Param params) {
        Player player = context.getPlayer();
        if(params.hasAnyParam("player"))
            player = Util.getPlayerExact(params.getParam("player"));
        boolean isGlide = params.getParam("glide", true);
        return glidePlayer(player, isGlide);
    }

    private boolean glidePlayer(Player player, boolean isGlide) {
        if(player == null || player.isDead() || !player.isOnline()) return false;
        player.setGliding(isGlide);
        return true;
    }
}
