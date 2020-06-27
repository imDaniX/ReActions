package me.fromgate.reactions.commands;

import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = Msg.CMD_CLEAR, permission = "reactions.config",
		subCommands = {"clear"}, allowConsole = true, shortDescription = "&3/react clear <id> [f|a|r]")
public class CmdClear extends Cmd {
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		String activatorId = args.length >= 2 ? args[1] : "";
		if (activatorId.isEmpty()) return false;
		String arg2 = args.length >= 3 ? args[2] : "";
		if (ActivatorsManager.contains(activatorId)) {
			if (arg2.equalsIgnoreCase("f") || arg2.equalsIgnoreCase("flag")) {
				ActivatorsManager.clearFlags(activatorId);
				Msg.MSG_CLEARFLAG.print(sender, activatorId);
			} else if (arg2.equalsIgnoreCase("a") || arg2.equalsIgnoreCase("action")) {
				ActivatorsManager.clearActions(activatorId);
				Msg.MSG_CLEARACT.print(sender, activatorId);
			} else if (arg2.equalsIgnoreCase("r") || arg2.equalsIgnoreCase("reaction")) {
				ActivatorsManager.clearReactions(activatorId);
				Msg.MSG_CLEARREACT.print(sender, activatorId);
			}
			// TODO: Save just one group
			ActivatorsManager.saveActivators();
		} else return Msg.CMD_UNKNOWNBUTTON.print(sender, activatorId);
		return false;
	}
}
