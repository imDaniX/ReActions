package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.logic.actions.Action;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/8/2017.
 */
public class ActionGlide extends Action {
    @Override
    public boolean execute(RaContext context, Parameters params) {
        Player player = context.getPlayer();
        if (params.contains("player"))
            player = Utils.getPlayerExact(params.getString("player"));
        boolean isGlide = params.getBoolean("glide", true);
        return glidePlayer(player, isGlide);
    }

    private boolean glidePlayer(Player player, boolean isGlide) {
        if (player == null || player.isDead() || !player.isOnline()) return false;
        player.setGliding(isGlide);
        return true;
    }
}
