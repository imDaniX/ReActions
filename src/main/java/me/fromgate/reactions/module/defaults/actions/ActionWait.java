package me.fromgate.reactions.module.defaults.actions;

import me.fromgate.reactions.logic.activity.actions.OldAction;
import me.fromgate.reactions.logic.activity.actions.StoredAction;
import me.fromgate.reactions.time.waiter.WaitingManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionWait extends OldAction /*TODO: implements Stopper*/ {

    @Override
    protected boolean execute(RaContext context, Parameters params) {
        return false;
    }

    public void executeDelayed(Player player, final List<StoredAction> actions, final boolean isAction, long time) {
        if (actions.isEmpty()) return;
        WaitingManager.executeDelayed(player, actions, isAction, time);

		/*final String playerStr = player!=null? player.getName() : null;
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), new Runnable(){
			@Override
			public void run() {
				@SuppressWarnings("deprecation")
				Player p = playerStr==null ? null : Util.getPlayerExact(playerStr);
				if (p==null&& playerStr!=null) return;
				Actions.executeActions(p, actions, isAction);
			}
		}, time); */

    }

}
