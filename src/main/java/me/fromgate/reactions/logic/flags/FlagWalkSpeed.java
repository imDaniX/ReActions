package me.fromgate.reactions.logic.flags;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/4/2017.
 */
public class FlagWalkSpeed implements Flag {
    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        if (!Util.isInteger(param)) return false;
        long walkSpeed = Math.round(player.getWalkSpeed() * 10);
        context.setTempVariable("walkspeed", Long.toString(walkSpeed));
        return walkSpeed >= Integer.parseInt(param);

    }
}
