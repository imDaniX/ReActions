package me.fromgate.reactions.commands;

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.commands.custom.FakeCommander;
import me.fromgate.reactions.holders.LocationHolder;
import me.fromgate.reactions.logic.ActivatorsManager;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.time.Delayer;
import me.fromgate.reactions.time.TimersManager;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;


@CmdDefine(command = "react", description = Msg.CMD_LIST, permission = "reactions.config",
        subCommands = {"list"}, allowConsole = true, shortDescription = "&3/react list [loc|group|type] [page]")
public class CmdList extends Cmd {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (sender instanceof Player) ? (Player) sender : null;
        int lpp = (player == null) ? 1000 : 15;
        int page = 1;
        String arg1 = args.length >= 2 ? args[1].toLowerCase(Locale.ENGLISH) : "";
        String arg2 = args.length >= 3 ? args[2] : "";
        String arg3 = args.length >= 4 ? args[3] : "";
        if (NumberUtils.isNonzeroInteger(arg1)) printAct(sender, 1, lpp);
        else {
            String mask = "";
            if (NumberUtils.isNonzeroInteger(arg2)) {
                page = Integer.parseInt(arg2);
                mask = arg3;
            } else if (NumberUtils.isNonzeroInteger(arg3)) {
                page = Integer.parseInt(arg3);
                mask = arg2;
            }

            switch (arg1) {
                case "type":
                    printActType(sender, mask, page, lpp);
                    break;
                case "group":
                    printActGroup(sender, mask, page, lpp);
                    break;
                case "timer":
                case "timers":
                    TimersManager.listTimers(sender, page);
                    break;
                case "delay":
                case "delays":
                    Delayer.printDelayList(sender, page, lpp);
                    break;
                case "loc":
                case "location":
                    LocationHolder.printLocList(sender, page, lpp);
                    break;
                case "var":
                case "variables":
                case "variable":
                    Variables.printList(sender, page, mask);
                    break;
                case "menu":
                case "menus":
                    InventoryMenu.printMenuList(sender, page, mask);
                    break;
                case "cmd":
                case "commands":
                    FakeCommander.list().forEach(sender::sendMessage);
                    break;
                default:
                    printAct(sender, page, lpp);
            }
        }
        return true;
    }

    private void printAct(CommandSender sender, int page, int lpp) {
        List<String> ag = ActivatorsManager.getNames();
        Msg.printPage(sender, ag, Msg.MSG_ACTLIST, page, lpp, true);
        Msg.MSG_LISTCOUNT.print(sender, ActivatorsManager.size(), LocationHolder.sizeTpLoc());
    }

    private void printActGroup(CommandSender sender, String group, int page, int lpp) {
        List<String> ag = ActivatorsManager.getNamesByGroup(group);
        Msg.MSG_ACTLISTGRP.print(sender, group, '6', '6');
        Msg.printPage(sender, ag, null, page, lpp, true);
    }

    private void printActType(CommandSender sender, String type, int page, int lpp) {
        List<String> ag = ActivatorsManager.getNamesByType(type);
        Msg.MSG_ACTLISTTYPE.print(sender, type, '6', '6');
        Msg.printPage(sender, ag, null, page, lpp, true);
    }


}
