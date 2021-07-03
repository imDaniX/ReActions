package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.module.defaults.actions.Actions;
import me.fromgate.reactions.module.defaults.activators.OldActivatorType;
import me.fromgate.reactions.module.defaults.flags.Flags;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = Msg.HLP_THISHELP, permission = "reactions.config",
        subCommands = {"help|hlp"}, allowConsole = true, shortDescription = "&3/react help [command]")
public class CmdHelp extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String arg1 = "help";
        int page = 1;

        if (args.length > 1)
            for (int i = 1; i < Math.min(args.length, 3); i++) {
                if (NumberUtils.isNonzeroInteger(args[i])) page = Integer.parseInt(args[i]);
                else arg1 = args[i];
            }
        if (arg1.equalsIgnoreCase("flag") || arg1.equalsIgnoreCase("flags")) {
            Flags.listFlags(sender, page);
        } else if (arg1.equalsIgnoreCase("action") || arg1.equalsIgnoreCase("actions")) {
            Actions.listActions(sender, page);
        } else if (arg1.equalsIgnoreCase("activator") || arg1.equalsIgnoreCase("activators")) {
            OldActivatorType.listActivators(sender, page);
        } else if (arg1.equalsIgnoreCase("placeholder") || arg1.equalsIgnoreCase("placeholders")) {
            ReActions.getPlaceholders().listPlaceholders(sender, page);
        } else {
            if (!arg1.equalsIgnoreCase("help")) page = 1;
            Commander.printHelp(sender, page);
        }
        return true;
    }
}
