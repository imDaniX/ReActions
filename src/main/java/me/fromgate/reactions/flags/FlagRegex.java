package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;

public class FlagRegex implements Flag {
    @Override
    public boolean checkFlag(RaContext context, String param) {
        Param params = new Param(param, "unknown");
        String regex = params.getParam("regex", "");
        if (regex.isEmpty()) return false;
        String value = params.getParam("value", "");
        if (value.isEmpty()) return false;
        return value.matches(regex);
    }
}
