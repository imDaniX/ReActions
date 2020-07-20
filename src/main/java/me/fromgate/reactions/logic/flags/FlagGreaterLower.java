package me.fromgate.reactions.logic.flags;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;

/**
 * Created by MaxDikiy on 10/1/2017.
 */
public class FlagGreaterLower implements Flag {
    private final byte flagType;

    public FlagGreaterLower(byte flagType) {
        this.flagType = flagType;
    }

    @Override
    public boolean checkFlag(RaContext context, String param) {
        Parameters params = new Parameters(param, "unknown");
        float paramValue = params.getParam("param", 0);
        float value = params.getParam("value", 0);
        if (flagType == 0) {
            context.setTempVariable("gparam", Double.toString(paramValue));
            return paramValue > value;
        } else if (flagType == 1) {
            context.setTempVariable("lparam", Double.toString(paramValue));
            return paramValue < value;
        }
        return false;
    }
}
