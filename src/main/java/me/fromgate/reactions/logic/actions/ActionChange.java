package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;

public class ActionChange extends Action {
    @Override
    public boolean execute(RaContext context, Parameters params) {
        // TODO: Error message
        return context.setChangeable(params.getString("key", params.getString("id")),
                params.getString("value", ""));
    }
}
