package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.entity.Player;

public class FlagHeldSlot implements Flag {
    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        return Util.INT_POSITIVE.matcher(param).matches() && player.getInventory().getHeldItemSlot() == Integer.parseInt(param);
    }
}
