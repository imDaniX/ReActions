package me.fromgate.reactions.commands;

import com.google.common.base.Joiner;
import me.fromgate.reactions.storage.StorageManager;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;


@CmdDefine(command = "exec", description = Msg.CMD_EXEC, permission = "reactions.run",
		subCommands = {}, allowConsole = true,
		shortDescription = "&3/exec <activator> [player:<PlayerSelector>] [delay:<Time>]")
public class CmdExec extends Cmd {

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length == 0) return false;
		String commandLine = Joiner.on(" ").join(args);
		Param param = new Param(commandLine, "activator");
		if (StorageManager.raiseExecEvent(sender, param)) {
			Msg.printMSG(sender, "cmd_runplayer", commandLine);
		} else Msg.printMSG(sender, "cmd_runplayerfail", 'c', '6', commandLine);
		return true;
	}

}
