package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;

/**
 * Created by MaxDikiy on 2017-10-04.
 */
public class ActionChatMessage extends Action {
    @Override
    public boolean execute(RaContext context, Param params) {
        if(context.getPlayer() != null) {
            String msg = params.getParam("param-line");
            msg = msg.replaceFirst("^[\\s\\/]+", "");
            context.getPlayer().chat(msg);
        }
        return true;
    }
}
