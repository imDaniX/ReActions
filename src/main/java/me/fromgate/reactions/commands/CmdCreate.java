package me.fromgate.reactions.commands;

import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.holders.LocationHolder;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.time.TimersManager;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.BlockParam;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CmdDefine(command = "react", description = Msg.CMD_CREATE, permission = "reactions.config",
		subCommands = {"create"}, allowConsole = true,
		shortDescription = "&3/react create <loc|timer|menu|activatorType> <id> [param]")
public class CmdCreate extends Cmd {
	// TODO: Commands creation
	// TODO: Cuboids creation
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length < 3) return false;
		Player player = (sender instanceof Player) ? (Player) sender : null;
		String type = args[1].toLowerCase();
		String id = args[2];
		StringBuilder param = new StringBuilder(args.length >= 4 ? args[3] : "");
		if (args.length > 4) {
			for (int i = 4; i < args.length; i++)
				param.append(" ").append(args[i]);
			param = new StringBuilder(param.toString().trim());
		}
		switch(type) {
			case "loc":
				if (player == null) return false;
				if (!LocationHolder.addTpLoc(id, player.getLocation())) return false;
				LocationHolder.saveLocs();
				Msg.CMD_ADDTPADDED.print(sender, id);
				return true;
			case "timer":
				if (param.length() == 0) return false;
				return TimersManager.addTimer(sender, id, Param.parseParams(param.toString()), true);
			case "menu":
				// TODO: Create menu from chest
				if (param.length() == 0) return false;
				String arg3 = args[3];
				if (InventoryMenu.add(id,
						Util.isInteger(arg3) ? Integer.parseInt(arg3) : 9,
						(param.length() == 1) ? "" : param.toString().substring(arg3.length()))) {
					Msg.CMD_ADDMENUADDED.print(sender, id);
					return true;
				}
				Msg.CMD_ADDMENUADDFAIL.print(sender, id);
				return false;
			default:
				return addActivator(sender, type, id, param.toString());
		}
	}

	private boolean addActivator(CommandSender sender, String type, String name, String param) {
		ActivatorType at = ActivatorType.getByName(type);
		if(at == null) return false;
		Param params;
		if(sender instanceof Player) {
			Player player = (Player) sender;
			param = LocationUtil.replaceStandardLocations(player, param);
			if(at.isNeedBlock())
				params = new BlockParam(param, player.getTargetBlock(null, 100));
			else
				params = new Param(param);
		} else {
			if(at.isNeedBlock()) return false;
			params = new Param(param);
		}
		Activator activator = at.create(name, "activators", params);
		if(activator == null || !activator.isValid()) {
			Msg.CMD_NOTADDBADDEDSYNTAX.print(sender, name, type);
			return true;
		}
		if(ActivatorsManager.add(activator)) {
			ActivatorsManager.saveActivators();
			Msg.CMD_ADDBADDED.print(sender, activator.toString());
		} else {
			Msg.CMD_NOTADDBADDED.print(sender, activator.toString());
		}
		return true;
	}
}
