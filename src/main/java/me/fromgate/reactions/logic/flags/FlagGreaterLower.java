package me.fromgate.reactions.logic.flags;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;

/**
 * Created by MaxDikiy on 10/1/2017.
 */
public class FlagGreaterLower implements Flag {
    private final boolean greater;

    public FlagGreaterLower(boolean greater) {
        this.greater = greater;
    }

    @Override
    public boolean checkFlag(RaContext context, String param) {
        Parameters params = Parameters.fromString(param, "unknown");
        double paramValue = params.getDouble("param", 0d);
        double value = params.getDouble("value", 0d);
        if (greater) {
            context.setVariable("gparam", Double.toString(paramValue));
            return paramValue > value;
        } else {
            context.setVariable("lparam", Double.toString(paramValue));
            return paramValue < value;
        }
    }
}
