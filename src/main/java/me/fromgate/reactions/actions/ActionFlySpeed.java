package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class ActionFlySpeed extends Action {
	@Override
	public boolean execute(RaContext context, Param params) {
		Player player = context.getPlayer();
		double speed = params.getParam("speed", params.getParam("param-line", 0));
		if(params.hasAnyParam("player"))
			player = Util.getPlayerExact(params.getParam("player"));
		return flySpeedPlayer(player, speed / 10);
	}

	private boolean flySpeedPlayer(Player player, double speed) {
		if (player == null || player.isDead() || !player.isOnline()) return false;
		if (speed > 1) speed = 1;
		if (speed < 0) speed = 0;
		player.setFlySpeed((float) speed);
		return true;
	}
}