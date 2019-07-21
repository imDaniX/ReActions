package me.fromgate.reactions.commands;

import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.location.Locator;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;


@CmdDefine(command = "react", description = Msg.CMD_LIST, permission = "reactions.config",
		subCommands = {"list"}, allowConsole = true, shortDescription = "&3/react list [loc|group|type] [page]")
public class CmdList extends Cmd {


	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player player = (sender instanceof Player) ? (Player) sender : null;
		int lpp = (player == null) ? 1000 : 15;
		int page = 1;
		String arg1 = args.length >= 2 ? args[1].toLowerCase() : "";
		String arg2 = args.length >= 3 ? args[2] : "";
		String arg3 = args.length >= 4 ? args[3] : "";
		if (Util.isIntegerGZ(arg1)) printAct(sender, 1, lpp);
		else {
			String mask = "";
			if (Util.isIntegerGZ(arg2)) {
				page = Integer.parseInt(arg2);
				mask = arg3;
			} else if (Util.isIntegerGZ(arg3)) {
				page = Integer.parseInt(arg3);
				mask = arg2;
			}

			switch(arg1) {
				case "type":
					printActType(sender, mask, page, lpp);
				case "group":
					printActGroup(sender, mask, page, lpp);
				case "timer": case "timers":
					Timers.listTimers(sender, page);
				case "delay": case "delays":
					Delayer.printDelayList(sender, page, lpp);
				case "loc": case "location":
					Locator.printLocList(sender, page, lpp);
				case "var": case "variables": case "variable":
					Variables.printList(sender, page, mask);
				case "menu": case "menus":
					InventoryMenu.printMenuList(sender, page, mask);
				default:
					printAct(sender, page, lpp);
			}
		}
		return true;
	}

	private void printAct(CommandSender sender, int page, int lpp) {
		Set<String> ag = Activators.getActivatorsSet();
		Msg.printPage(sender, ag, Msg.MSG_ACTLIST, page, lpp, true);
		Msg.MSG_LISTCOUNT.print(sender, Activators.size(), Locator.sizeTpLoc());
	}

	private void printActGroup(CommandSender sender, String group, int page, int lpp) {
		Set<String> ag = Activators.getActivatorsSetGroup(group);
		Msg.MSG_ACTLISTGRP.print(sender, group, '6', '6');
		Msg.printPage(sender, ag, null, page, lpp, true);
	}

	private void printActType(CommandSender sender, String type, int page, int lpp) {
		Set<String> ag = Activators.getActivatorsSet(type);
		Msg.MSG_ACTLISTTYPE.print(sender, type, '6', '6');
		Msg.printPage(sender, ag, null, page, lpp, true);
	}


}
