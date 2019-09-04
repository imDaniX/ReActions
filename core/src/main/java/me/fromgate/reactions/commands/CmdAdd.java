package me.fromgate.reactions.commands;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.holders.LocationHolder;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@CmdDefine(command = "react", description = Msg.CMD_ADD, permission = "reactions.config",
		subCommands = {"add"}, allowConsole = true,
		shortDescription = "&3/react add <Id> [f <flag> <param> | <a|r> <action> <param>")
public class CmdAdd extends Cmd {

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length == 1) return false;
		Player player = (sender instanceof Player) ? (Player) sender : null;
		String arg1 = args[1];
		String arg2 = args.length >= 3 ? args[2] : "";
		String arg3 = args.length >= 4 ? args[3] : "";
		StringBuilder arg4 = new StringBuilder(args.length >= 5 ? args[4] : "");
		if (args.length > 5) {
			for (int i = 5; i < args.length; i++)
				arg4.append(" ").append(args[i]);
			arg4 = new StringBuilder(arg4.toString().trim());
		}
		if (ActivatorsManager.contains(arg1)) {
			String param = replaceStandardLocations(player, arg4.toString()); // используется в addActions
			if (arg2.equalsIgnoreCase("a") || arg2.equalsIgnoreCase("action")) {
				if (addAction(arg1, arg3, param)) {
					ActivatorsManager.saveActivators();
					Msg.CMD_ACTADDED.print(sender, arg3 + " (" + param + ")"); //TODO~
					return true;
				} else {
					Msg.CMD_ACTNOTADDED.print(sender, arg3 + " (" + param + ")");
				}
			} else if (arg2.equalsIgnoreCase("r") || arg2.equalsIgnoreCase("reaction")) {
				if (addReAction(arg1, arg3, param)) {
					ActivatorsManager.saveActivators();
					return Msg.CMD_REACTADDED.print(sender, arg3 + " (" + param + ")");
				} else {
					Msg.CMD_REACTADDED.print(sender, arg3 + " (" + param + ")");
				}
			} else if (arg2.equalsIgnoreCase("f") || arg2.equalsIgnoreCase("flag")) {
				if (addFlag(arg1, arg3, param)) {
					ActivatorsManager.saveActivators();
					return Msg.CMD_FLAGADDED.print(sender, arg3 + " (" + param + ")");
				} else {
					Msg.CMD_FLAGNOTADDED.print(sender, arg3 + " (" + arg4 + ")");
				}
			} else {
				Msg.CMD_UNKNOWNBUTTON.print(sender, arg2);
			}
		} else {
			Msg.CMD_UNKNOWNADD.print(sender, 'c');
		}
		return true;
	}

	private boolean addAction(String clicker, String flag, String param) {
		if (Actions.isValid(flag)) {
			ActivatorsManager.addAction(clicker, flag, param);
			return true;
		}
		return false;
	}

	private boolean addReAction(String clicker, String flag, String param) {
		if (Actions.isValid(flag)) {
			ActivatorsManager.addReaction(clicker, flag, param);
			return true;
		}
		return false;
	}

	private boolean addFlag(String clicker, String fl, String param) {
		String flag = fl.replaceFirst("!", "");
		boolean not = fl.startsWith("!");
		if (Flags.isValid(flag)) {
			// TODO: все эти проверки вынести в соответствующие классы
			ActivatorsManager.addFlag(clicker, flag, param, not);
			return true;
		}
		return false;
	}

	private static String replaceStandardLocations(Player p, String param) {
		if (p == null) return param;
		Location targetBlock = null;
		try {
			targetBlock = p.getTargetBlock(null, 100).getLocation();
		} catch (Exception ignored) {
		}
		Map<String, Location> locs = new HashMap<>();
		locs.put("%here%", p.getLocation());
		locs.put("%eye%", p.getEyeLocation());
		locs.put("%head%", p.getEyeLocation());
		locs.put("%viewpoint%", targetBlock);
		locs.put("%view%", targetBlock);
		locs.put("%selection%", LocationHolder.getHeld(p));
		locs.put("%select%", LocationHolder.getHeld(p));
		locs.put("%sel%", LocationHolder.getHeld(p));
		String newparam = param;
		for (String key : locs.keySet()) {
			Location l = locs.get(key);
			if (l == null) continue;
			newparam = newparam.replace(key, LocationUtil.locationToString(l));
		}
		return newparam;
	}
}
