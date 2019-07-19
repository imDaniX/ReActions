package me.fromgate.reactions.actions;

import me.fromgate.reactions.timer.Time;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.waiter.ActionsWaiter;
import org.bukkit.entity.Player;

public class ActionDelayed extends Action {

	@Override
	public boolean execute(Player p, Param params) {
		long delay = Time.parseTime(params.getParam("time", "0"));
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
		ActionsWaiter.executeDelayed(p, av, this.isAction(), delay);
		return false;
	}

}
