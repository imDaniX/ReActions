package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionResponse extends Action {
	@Override
	public boolean execute(Player p, Param params) {
		if(p == null)
			Bukkit.getConsoleSender().sendMessage(params.toString());
		else
			if(p.isOnline()) p.sendMessage(params.toString());
		return true;
	}
}
