/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *
 *  This file is part of ReActions.
 *
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 *
 */


package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.commands.FakeCmd;
import me.fromgate.reactions.storage.CommandStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;

public class CommandActivator extends Activator {

	private final boolean checkExact;
	private final String command;
	private final Param arguments;
	private final boolean useRegex;
	final boolean override;

	public CommandActivator(ActivatorBase base, String command, boolean useRegex, boolean override) {
		super(base);
		this.command = command == null ? "unknown" : command;
		this.useRegex = useRegex;
		this.override = override;

		Param params = new Param(command);
		if (params.isParamsExists("cmd")) {
			this.checkExact = true;
			this.arguments = params;
		} else {
			this.checkExact = false;
			this.arguments = new Param();
		}
		params.remove("param-line");
	}

	private boolean checkLine(String line) {
		if (this.useRegex) return line.matches(command);
		return line.toLowerCase().startsWith(command.toLowerCase());
	}

	private boolean commandMatches(String line) {
		if (!this.checkExact) return checkLine(line);
		String[] cmdLn = line.replaceFirst("/", "").split(" ");
		if (cmdLn.length == 0) return false;
		Set<String> keys = new HashSet<>();
		for (int i = 0; i < cmdLn.length; i++) {
			String key = (i == 0 ? "cmd" : "arg" + i);
			keys.add(key);
			if (!arguments.hasAnyParam(key)) return false;
			if (arguments.getParam(key).equalsIgnoreCase("*")) continue;
			if (!arguments.getParam(key).equalsIgnoreCase(cmdLn[i])) return false;
		}
		return arguments.keySet().equals(keys);
	}

	private void setTempVars(String command, String[] args) {
		String argStr = args.length > 1 ? command.replaceAll("^" + args[0] + " ", "") : "";
		Variables.setTempVar("command", command);
		Variables.setTempVar("args", argStr);
		String argsLeft = command.replaceAll("(\\S+ )+{" + args.length + "}", "");
		Variables.setTempVar("argsleft", argsLeft);
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++)
				Variables.setTempVar("arg" + i, args[i]);
		}

		int count = 0;
		while (argStr.indexOf(" ") > 0) {
			count++;
			argStr = argStr.substring(argStr.indexOf(" ") + 1);
			Variables.setTempVar("args" + count, argStr);
			Variables.setTempVar("argscount", Integer.toString(count + 1));
		}
	}

	@Override
	public boolean activate(RAStorage event) {
		CommandStorage ce = (CommandStorage) event;
		if (ce.isParentCancelled() && !this.override) return false;
		if (!commandMatches(ce.getCommand())) return false;
		setTempVars(ce.getCommand(), ce.getArgs());
		if (!isCommandRegistered(ce.getCommand()) && FakeCmd.registerNewCommand(ce.getCommand())) {
			Msg.CMD_REGISTERED.log(ce.getCommand());
		}
		return Actions.executeActivator(ce.getPlayer(), getBase());
	}

	public String getCommand() {
		if (this.checkExact) {
			return arguments.getParam("cmd", "");
		} else {
			String[] cmd = this.command.split(" ");
			if (cmd.length == 0) return "";
			return cmd[0];
		}
	}

	public boolean isCommandRegistered() {
		String command = getCommand();
		if (command.isEmpty()) return false;
		return isCommandRegistered(command);
	}


	public boolean isCommandRegistered(String cmd) {
		if (cmd.isEmpty()) return false;
		Command cmm = Bukkit.getServer().getPluginCommand(cmd);
		return (cmm != null);
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("override", this.override);
		cfg.set("regex", this.useRegex);
		cfg.set("command", command);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.COMMAND;
	}

	@Override
	public boolean isValid() {
		return !Util.emptyString(command);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("override:").append(this.override);
		sb.append("; regex:").append(this.useRegex);
		sb.append("; command:").append(this.command);
		sb.append(")");
		return sb.toString();
	}

	public boolean useRegex() {
		return this.useRegex;
	}

	public static CommandActivator create(ActivatorBase base, Param param) {
		String command = param.getParam("command", param.toString());
		boolean override = param.getParam("override", true);
		boolean useRegex = param.getParam("regex", false);
		return new CommandActivator(base, command, useRegex, override);
	}

	public static CommandActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String command = cfg.getString("command");
		boolean override = cfg.getBoolean("override", true);
		boolean useRegex = cfg.getBoolean("regex", false);
		return new CommandActivator(base, command, useRegex, override);
	}
}
