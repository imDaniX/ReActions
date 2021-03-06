package me.fromgate.reactions.logic.flags;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;

public class FlagRegex implements Flag {
    @Override
    public boolean checkFlag(RaContext context, String param) {
        Parameters params = Parameters.fromString(param, "unknown");
        String regex = params.getString("regex", "");
        if (regex.isEmpty()) return false;
        String value = params.getString("value", "");
        if (value.isEmpty()) return false;
        return value.matches(regex);
    }
}
