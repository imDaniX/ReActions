package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Cfg;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.FakeCmd;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;

import java.io.File;

@CmdDefine(command = "react", description = Msg.CMD_RELOAD, permission = "reactions.config",
        subCommands = {"reload"}, allowConsole = true, shortDescription = "&3/react reload [galcdvtm] [groupId]")
public class CmdReload extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] params) {
        if(params.length >= 2) {
            String check = params[1].toLowerCase();
            if(check.contains("g") && params.length > 2) {
                Activators.loadActivators(params[2].replace('/', File.separatorChar).replace('\\', File.separatorChar));
            } else
            if(check.contains("a")) {
                Activators.clear();
                Activators.loadActivators();
                FakeCmd.updateAllCommands();
            }
            if(check.contains("l"))
                Locator.loadLocs();
            if(check.contains("c")) {
                ReActions.getPlugin().reloadConfig();
                Cfg.load();
            }
            if(check.contains("d"))
                Delayer.load();
            if(check.contains("v")) {
                if (!Cfg.playerSelfVarFile) Variables.load();
                else Variables.loadVars();
            }
            if(check.contains("t"))
                Timers.init();
            if(check.contains("m"))
                InventoryMenu.load();

        } else {
            Activators.clear();
            Activators.loadActivators();
            Locator.loadLocs();
            ReActions.getPlugin().reloadConfig();
            Cfg.load();
            Delayer.load();
            if (!Cfg.playerSelfVarFile) Variables.load();
            else Variables.loadVars();
            Timers.init();
            InventoryMenu.load();
            FakeCmd.updateAllCommands();
        }
        Msg.MSG_CMDRELOAD.print(sender, Activators.size(), Locator.sizeTpLoc());
        return true;
    }

}
