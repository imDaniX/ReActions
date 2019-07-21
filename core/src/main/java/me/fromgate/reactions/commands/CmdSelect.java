package me.fromgate.reactions.commands;

import me.fromgate.reactions.util.Selector;
import me.fromgate.reactions.util.location.Locator;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.entity.Player;

@CmdDefine(command = "react", description = Msg.CMD_SELECT, permission = "reactions.select",
		subCommands = {"select|sel"}, allowConsole = false, shortDescription = "&3/react select")
public class CmdSelect extends Cmd {
	@Override
	public boolean execute(Player player, String[] args) {
		Selector.selectLocation(player, null);
		Msg.CMD_SELECTED.print(player, Locator.locationToStringFormatted(Selector.getSelectedLocation(player)));
		return true;
	}
}
