package me.fromgate.reactions.commands;

import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.Variables;
import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.customcommands.FakeCommander;
import me.fromgate.reactions.holders.LocationHolder;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.time.Delayer;
import me.fromgate.reactions.time.TimersManager;
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
				ActivatorsManager.loadActivators(params[2].replaceAll("[/\\\\]", File.separator));
			} else
			if(check.contains("a")) {
				ActivatorsManager.clear();
				ActivatorsManager.loadActivators();
			}
			if(check.contains("l"))
				LocationHolder.loadLocs();
			if(check.contains("c")) {
				ReActions.getPlugin().reloadConfig();
				Cfg.load();
				FakeCommander.updateCommands();
			}
			if(check.contains("d"))
				Delayer.load();
			if(check.contains("v")) {
				if (!Cfg.playerSelfVarFile) Variables.load();
				else Variables.loadVars();
			}
			if(check.contains("t"))
				TimersManager.init();
			if(check.contains("m"))
				InventoryMenu.load();

		} else {
			ActivatorsManager.clear();
			ActivatorsManager.loadActivators();
			LocationHolder.loadLocs();
			ReActions.getPlugin().reloadConfig();
			Cfg.load();
			Delayer.load();
			if (!Cfg.playerSelfVarFile) Variables.load();
			else Variables.loadVars();
			TimersManager.init();
			InventoryMenu.load();
			FakeCommander.updateCommands();
		}
		Msg.MSG_CMDRELOAD.print(sender, ActivatorsManager.size(), LocationHolder.sizeTpLoc());
		return true;
	}

}