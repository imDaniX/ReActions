package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;

public class FlagCheckOnline implements Flag {
	@Override
	public boolean checkFlag(Player player, String param) {
		return Util.getPlayerExact(param) != null;
	}
}
