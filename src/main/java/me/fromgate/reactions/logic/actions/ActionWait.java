package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.time.waiter.WaitingManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionWait extends Action /*TODO: implements Stopper*/ {

    @Override
    public boolean execute(RaContext context, Parameters params) {
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
