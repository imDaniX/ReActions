package me.fromgate.reactions.flags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlagCheckOnline implements Flag {
	@SuppressWarnings("deprecation")
	@Override
	public boolean checkFlag(Player player, String param) {
		return Bukkit.getPlayerExact(param) != null;
	}
}
