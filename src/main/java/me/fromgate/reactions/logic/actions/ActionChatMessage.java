package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;

/**
 * Created by MaxDikiy on 2017-10-04.
 */
public class ActionChatMessage extends Action {
    @Override
    public boolean execute(RaContext context, Parameters params) {
        if (context.getPlayer() != null) {
            String msg = params.getParam("param-line");
            msg = msg.replaceFirst("^[\\s/]+", "");
            context.getPlayer().chat(msg);
        }
        return true;
    }
}
