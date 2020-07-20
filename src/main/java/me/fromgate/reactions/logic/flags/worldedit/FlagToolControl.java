package me.fromgate.reactions.logic.flags.worldedit;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.logic.flags.Flag;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 11/10/2017.
 */
public class FlagToolControl implements Flag {
    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        return Boolean.parseBoolean(param) == RaWorldEdit.isToolControl(player);
    }
}
