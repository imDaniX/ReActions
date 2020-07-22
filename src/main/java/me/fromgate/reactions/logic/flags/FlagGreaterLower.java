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
        Parameters params = new Parameters(param, "unknown");
        double paramValue = params.getParam("param", 0d);
        double value = params.getParam("value", 0d);
        if (greater) {
            context.setTempVariable("gparam", Double.toString(paramValue));
            return paramValue > value;
        } else {
            context.setTempVariable("lparam", Double.toString(paramValue));
            return paramValue < value;
        }
    }
}
