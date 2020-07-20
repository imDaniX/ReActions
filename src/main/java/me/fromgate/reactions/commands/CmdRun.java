package me.fromgate.reactions.commands;

import me.fromgate.reactions.logic.StoragesManager;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = Msg.CMD_RUN, permission = "reactions.run",
        subCommands = {"run"}, allowConsole = true, shortDescription = "&3/react run <ExecActivator> [TargetPlayer] [Delay]")
public class CmdRun extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String param = String.join(" ", args); //sb.toString();
        if (StoragesManager.raiseExecActivator(sender, param)) {
            Msg.CMD_RUNPLAYER.print(sender, param);
        } else {
            Msg.CMD_RUNPLAYERFAIL.print(sender, 'c', '6', param);
        }
        return true;
    }
}
