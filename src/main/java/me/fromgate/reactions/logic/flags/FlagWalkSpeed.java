package me.fromgate.reactions.logic.flags;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.math.NumberUtils;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/4/2017.
 */
public class FlagWalkSpeed implements Flag {
    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        if (!NumberUtils.isInteger(param)) return false;
        long walkSpeed = Math.round(player.getWalkSpeed() * 10);
        context.setVariable("walkspeed", Long.toString(walkSpeed));
        return walkSpeed >= Integer.parseInt(param);

    }
}
