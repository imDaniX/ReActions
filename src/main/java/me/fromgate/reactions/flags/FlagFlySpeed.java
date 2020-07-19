package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/4/2017.
 */
public class FlagFlySpeed implements Flag {
    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        if (!Util.isInteger(param)) return false;
        long flySpeed = Math.round(player.getFlySpeed() * 10);
        context.setTempVariable("flyspeed", Integer.toString((int) flySpeed));
        return flySpeed >= Integer.parseInt(param);
    }
}
