package me.fromgate.reactions.util;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;


public class TemporaryOp {

	private static Set<String> tempOps = new HashSet<>();

	public static void setTempOp(CommandSender sender) {
		if (sender instanceof Player && !sender.isOp()) {
			tempOps.add(sender.getName());
			sender.setOp(true);
		}
	}

	public static void removeTempOp(CommandSender sender) {
		if (sender instanceof Player && tempOps.remove(sender.getName()))
			sender.setOp(false);
	}

}
