package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/8/2017.
 */
public class ActionGlide extends Action {
	@Override
	public boolean execute(Player p, Param params) {
		Player player;
		String playerName = params.getParam("player", p != null ? p.getName() : "");
		player = playerName.isEmpty() ? null : Util.getPlayerExact(playerName);
		boolean isGlide = params.getParam("glide", true);
		return glidePlayer(player, isGlide);
	}

	private boolean glidePlayer(Player player, boolean isGlide) {
		if (player == null || player.isDead() || !player.isOnline()) return false;
		player.setGliding(isGlide);
		return true;
	}
}
