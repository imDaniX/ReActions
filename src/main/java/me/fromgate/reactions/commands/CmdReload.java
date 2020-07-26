package me.fromgate.reactions.commands;

import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.ReActionsPlugin;
import me.fromgate.reactions.VariablesManager;
import me.fromgate.reactions.commands.custom.FakeCommander;
import me.fromgate.reactions.holders.LocationHolder;
import me.fromgate.reactions.logic.ActivatorsManager;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.time.Delayer;
import me.fromgate.reactions.time.TimersManager;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Locale;

@CmdDefine(command = "react", description = Msg.CMD_RELOAD, permission = "reactions.config",
        subCommands = {"reload"}, allowConsole = true, shortDescription = "&3/react reload [galcdvtm] [groupId]")
public class CmdReload extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] params) {
        if (params.length >= 2) {
            String check = params[1].toLowerCase(Locale.ENGLISH);
            if (check.contains("g") && params.length > 2) {
                ActivatorsManager.getInstance().loadActivators(params[2].replaceAll("[/\\\\]", File.separator));
            } else if (check.contains("a")) {
                ActivatorsManager.getInstance().clear();
                ActivatorsManager.getInstance().loadActivators();
            }
            if (check.contains("l"))
                LocationHolder.loadLocs();
            if (check.contains("c")) {
                ReActionsPlugin.getInstance().reloadConfig();
                Cfg.load();
                FakeCommander.updateCommands();
            }
            if (check.contains("d"))
                Delayer.load();
            if (check.contains("v")) {
                if (!Cfg.playerSelfVarFile) VariablesManager.getInstance().load();
                else VariablesManager.getInstance().loadVars();
            }
            if (check.contains("t"))
                TimersManager.init();
            if (check.contains("m"))
                InventoryMenu.load();

        } else {
            ActivatorsManager.getInstance().clear();
            ActivatorsManager.getInstance().loadActivators();
            LocationHolder.loadLocs();
            ReActionsPlugin.getInstance().reloadConfig();
            Cfg.load();
            Delayer.load();
            if (!Cfg.playerSelfVarFile) VariablesManager.getInstance().load();
            else VariablesManager.getInstance().loadVars();
            TimersManager.init();
            InventoryMenu.load();
            FakeCommander.updateCommands();
        }
        Msg.MSG_CMDRELOAD.print(sender, ActivatorsManager.getInstance().size(), LocationHolder.sizeTpLoc());
        return true;
    }

}
