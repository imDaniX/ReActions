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
import me.fromgate.reactions.storages.PrecommandStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.regex.Pattern;

public class PrecommandActivator extends Activator {
	private final String command;
	private final Pattern pattern;
	private final boolean useRegex;
	private final boolean consoleAllowed;

	public PrecommandActivator(ActivatorBase base, String command, boolean useRegex, boolean consoleAllowed) {
		super(base);
		command = command == null ? "unknown" : command;
		this.command = command;
		this.pattern = useRegex ? Pattern.compile(command) : null;
		this.useRegex = useRegex;
		this.consoleAllowed = consoleAllowed;
	}

	@Override
	public boolean activate(Storage storage) {
		PrecommandStorage cs = (PrecommandStorage) storage;
		if(!consoleAllowed && cs.getSender() instanceof ConsoleCommandSender) return false;
		if(useRegex) {
			if(!pattern.matcher(cs.getCommand()).matches()) return false;
		} else
			if(!command.equalsIgnoreCase(cs.getCommand())) return false;
		return Actions.executeActivator(storage.getPlayer(), getBase());
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("regex", useRegex);
		cfg.set("command", command);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.PRECOMMAND;
	}

	@Override
	public boolean isValid() {
		return !Util.isStringEmpty(command);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("; regex:").append(this.useRegex);
		sb.append("; command:").append(this.command);
		sb.append(")");
		return sb.toString();
	}

	public static PrecommandActivator create(ActivatorBase base, Param param) {
		String command = param.getParam("command", param.toString());
		boolean useRegex = param.getParam("regex", false);
		boolean consoleAllowed = param.getParam("console", true);
		return new PrecommandActivator(base, command, useRegex, consoleAllowed);
	}

	public static PrecommandActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String command = cfg.getString("command");
		boolean useRegex = cfg.getBoolean("regex", false);
		boolean consoleAllowed = cfg.getBoolean("console_allowed", true);
		return new PrecommandActivator(base, command, useRegex, consoleAllowed);
	}
}
