package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;

public class FlagHeldSlot implements Flag {
	@Override
	public boolean checkFlag(Player player, String param) {
		return Util.INT_POSITIVE.matcher(param).matches() && player.getInventory().getHeldItemSlot() == Integer.parseInt(param);
	}
}
