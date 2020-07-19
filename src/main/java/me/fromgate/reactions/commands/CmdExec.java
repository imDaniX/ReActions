package me.fromgate.reactions.commands;

import me.fromgate.reactions.storages.StoragesManager;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.command.CommandSender;


@CmdDefine(command = "exec", description = Msg.CMD_EXEC, permission = "reactions.run",
        subCommands = {}, allowConsole = true,
        shortDescription = "&3/exec <activator> [player:<PlayerSelector>] [delay:<Time>]")
public class CmdExec extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 0) return false;
        String commandLine = String.join(" ", args);
        Param param = new Param(commandLine, "activator");
        if(StoragesManager.raiseExecActivator(sender, param)) {
            Msg.printMSG(sender, "cmd_runplayer", commandLine);
        } else Msg.printMSG(sender, "cmd_runplayerfail", 'c', '6', commandLine);
        return true;
    }

}
