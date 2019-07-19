package me.fromgate.reactions.commands;

import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CmdDefine(command = "react", description = Msg.CMD_INFO, permission = "reactions.config",
		subCommands = {"info"}, allowConsole = true, shortDescription = "&3/react info <activator> [f|a|r]")
public class CmdInfo extends Cmd {
	@Override
	public boolean execute(CommandSender sender, String[] args) {
		String id = args.length > 1 ? args[1] : "";
		if (id.isEmpty()) return false;
		String far = args.length > 2 ? args[2] : "";
		if (Activators.contains(id)) {
			printActInfo(sender, id, far);
		} else if (id.equalsIgnoreCase("menu")) {
			InventoryMenu.printMenu(sender, far);
		} else Msg.printMSG(sender, "cmd_unknownbutton", id);
		return true;
	}


	private void printActInfo(CommandSender sender, String activatorName, String far) {
		Activator act = Activators.get(activatorName);
		boolean f;
		boolean a;
		boolean r;
		if (far.isEmpty()) {
			f = true;
			a = true;
			r = true;
		} else {
			f = far.contains("f") || far.equalsIgnoreCase("flag") || far.equalsIgnoreCase("flags");
			a = far.contains("a") || far.equalsIgnoreCase("action") || far.equalsIgnoreCase("actions");
			r = far.contains("r") || far.equalsIgnoreCase("reaction") || far.equalsIgnoreCase("me/fromgate/reactions");
		}

		Msg.printMSG(sender, "&5☆ &d&l" + Msg.MSG_ACTINFOTITLE.getText("NOCOLOR") + " &r&5☆");
		Msg.printMSG(sender, "msg_actinfo", act.getName(), act.getType(), act.getGroup());
		Msg.printMSG(sender, "msg_actinfo2", act.getFlags().size(), act.getActions().size(), act.getReactions().size());
		if (f && (!act.getFlags().isEmpty())) {
			List<String> flg = new ArrayList<>();
			for (int i = 0; i < act.getFlags().size(); i++) {
				flg.add((act.getFlags().get(i).isInverted() ? "&4! &e" : "  &e") + act.getFlags().get(i).getFlag().name() + " &3= &a" + act.getFlags().get(i).getValue());
			}
			Msg.printPage(sender, flg, Msg.LST_FLAGS, 1, 100, true);
		}
		if (a && (!act.getActions().isEmpty())) {
			List<String> flg = new ArrayList<>();
			for (int i = 0; i < act.getActions().size(); i++) {
				String action = act.getActions().get(i).getAction().name();
				String param = act.getActions().get(i).getValue();
				if (action.equalsIgnoreCase("tp")) {
					Location loc = Locator.parseCoordinates(param);//Util.parseLocation(param);
					if (loc != null) param = Locator.locationToStringFormated(loc);
				}
				flg.add("  &e" + action + " &3= &a" + param);
			}
			Msg.printPage(sender, flg, Msg.LST_ACTIONS, 1, 100, true);
		}
		if (r && (!act.getReactions().isEmpty())) {
			List<String> flg = new ArrayList<>();
			for (int i = 0; i < act.getReactions().size(); i++) {
				String action = act.getReactions().get(i).getAction().name();
				String param = act.getReactions().get(i).getValue();
				if (action.equalsIgnoreCase("tp")) {
					Location loc = Locator.parseCoordinates(param);
					if (loc != null) param = Locator.locationToStringFormated(loc);
				}
				flg.add("  &e" + action + " &3= &a" + param);
			}
			Msg.printPage(sender, flg, Msg.LST_REACTIONS, 1, 100, true);
		}
	}
}
