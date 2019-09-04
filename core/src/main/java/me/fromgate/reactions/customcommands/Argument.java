package me.fromgate.reactions.customcommands;

import lombok.Getter;
import me.fromgate.reactions.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * One part of arguments chain.
 */
public class Argument {
	private final static List<String> NUMBERS = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

	private final String argument;
	private final Set<String> multiple;
	private final Argument.Type type;

	public Argument(String argument) {
		switch (argument) {
			case "~player":
				this.type = Type.PLAYER;
				multiple = new HashSet<>();
				break;
			case "~int":
				this.type = Type.INTEGER;
				multiple = new HashSet<>();
				break;
			case "~float":
				this.type = Type.FLOAT;
				multiple = new HashSet<>();
				break;
			case "*":
				this.type = Type.ANY;
				multiple = new HashSet<>();
				break;
			default:
				if(argument.contains("|") && !argument.contains("\\|")) {
					this.type = Type.MULTIPLE_TEXT;
					multiple = new HashSet<>(Arrays.asList(argument.split("\\|")));
				} else {
					this.type = Type.TEXT;
					if(argument.startsWith("\\~") || argument.equals("\\*")) argument = argument.substring(1);
					multiple = Collections.singleton(argument);
				}
		}
		this.argument = argument;
	}

	/**
	 * Compare argument for given string
	 * @param arg String to check
	 * @return {@link ExecType#DEFAULT} if everything is OK, some error if not
	 */
	public ExecType check(String arg) {
		switch(type) {
			case PLAYER:
				Player player = Util.getPlayerExact(arg);
				return player != null ? ExecType.DEFAULT : ExecType.OFFLINE;
			case TEXT: return argument.equalsIgnoreCase(arg) ? ExecType.DEFAULT : ExecType.BACKUP;
			case MULTIPLE_TEXT: return multiple.contains(arg) ? ExecType.DEFAULT : ExecType.BACKUP;
			case INTEGER: return Util.INT.matcher(arg).matches() ? ExecType.DEFAULT : ExecType.NOT_INTEGER;
			case FLOAT: return Util.FLOAT.matcher(arg).matches() ? ExecType.DEFAULT : ExecType.NOT_FLOAT;
		}
		return ExecType.DEFAULT;
	}

	/**
	 * Add value of argument to list of tab-complete if possible
	 * @param complete Original list
	 * @param arg Current argument
	 */
	public void tabComplete(List<String> complete, String arg) {
		switch(type) {
			case PLAYER:
				StringUtil.copyPartialMatches(arg, Util.getPlayersList(), complete);
				return;
			case TEXT: case MULTIPLE_TEXT:
				StringUtil.copyPartialMatches(arg, multiple, complete);
				return;
			case INTEGER: case FLOAT:
				StringUtil.copyPartialMatches(arg, NUMBERS, complete);
		}
	}

	/**
	 * Get priority of argument based on it's type
	 * @return Argument's priority
	 */
	public int getPriority() {
		return type.getPriority();
	}

	private enum Type {
		TEXT(10), MULTIPLE_TEXT(8), PLAYER(6), INTEGER(3), FLOAT(4), ANY(1);
		@Getter private final int priority;
		Type(int priority) {
			this.priority = priority;
		}
	}

	@Override
	public String toString() {
		switch(type) {
			case TEXT: return argument;
			case MULTIPLE_TEXT: return String.join(ChatColor.ITALIC + "|" + ChatColor.RESET, multiple);
			default: return ChatColor.ITALIC + argument;
		}
	}
}
