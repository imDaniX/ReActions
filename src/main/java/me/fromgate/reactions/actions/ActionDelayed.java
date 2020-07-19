package me.fromgate.reactions.actions;

import me.fromgate.reactions.time.TimeUtil;
import me.fromgate.reactions.time.waiter.WaitingManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Param;

public class ActionDelayed extends Action {

    @Override
    public boolean execute(RaContext context, Param params) {
        long delay = TimeUtil.parseTime(params.getParam("time", "0"));
        if (delay == 0) return false;

        String actionSource = params.getParam("action", "");
        if (actionSource.isEmpty()) return false;
        String actionStr;
        String paramStr = "";
        if (!actionSource.contains(" ")) actionStr = actionSource;
        else {
            actionStr = actionSource.substring(0, actionSource.indexOf(" "));
            paramStr = actionSource.substring(actionSource.indexOf(" ") + 1);
        }

        if (!Actions.isValid(actionStr)) {
            Msg.logOnce(actionSource, "Failed to execute delayed action: " + actionSource);
            return false;
        }

        StoredAction av = new StoredAction(actionStr, paramStr);
        WaitingManager.executeDelayed(context.getPlayer(), av, this.isAction(), delay);
        return false;
    }

}
