package me.fromgate.reactions.logic.flags;

import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;

public class FlagCheckOnline implements Flag {
    @Override
    public boolean checkFlag(RaContext context, String param) {
        return Utils.getPlayerExact(param) != null;
    }
}
