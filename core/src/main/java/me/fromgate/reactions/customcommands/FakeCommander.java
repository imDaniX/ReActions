package me.fromgate.reactions.customcommands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.storages.PrecommandStorage;
import me.fromgate.reactions.storages.StoragesManager;
import me.fromgate.reactions.util.FileUtil;
import me.fromgate.reactions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FakeCommander {
	private final static Map<String, RaCommand> commands = new HashMap<>();

	public static void init() {
		ReActions.getPlugin().saveResource("commands.yml", false);
		updateCommands();
	}

	public static void updateCommands() {
		File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "commands.yml");
		YamlConfiguration cfg = new YamlConfiguration();
		if(!FileUtil.loadCfg(cfg, f, "Failed to load commands")) return;
		CommandMap commandMap = getCommandMap();
		unregisterAll(/*commandMap*/);
		if(commandMap == null) return;
		for(String cmdKey : cfg.getKeys(false)) {
			ConfigurationSection cmdSection = cfg.getConfigurationSection(cmdKey);
			String command = cmdSection.getString("command");
			// TODO: Error message
			if(command == null) continue;
			String prefix = cmdSection.getString("prefix");
			List<String> aliases = cmdSection.getStringList("alias");
			boolean toBukkit = cmdSection.getBoolean("register", true);
			// TODO: Error message
			register(command, prefix, aliases, commandMap, new RaCommand(cmdSection, toBukkit), toBukkit);
		}
	}

	private static CommandMap getCommandMap() {
		try {
			final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			return (CommandMap) commandMapField.get(Bukkit.getServer());
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception){
			exception.printStackTrace();
			return null;
		}
	}

	public static boolean raiseRaCommand(PrecommandStorage storage) {
		String[] split = storage.getCommand().split("\\s");
		RaCommand raCmd = commands.get(split[0].toLowerCase());
		if(raCmd == null) return false;
		String[] args = Arrays.copyOfRange(split, 1, split.length);
		String exec = raCmd.executeCommand(storage.getSender(), args);
		StoragesManager.raiseExecActivator(storage.getSender(), exec, generateTempVars(split[0], storage.getCommand(), args));
		return raCmd.isOverride();
	}
	
	private static Map<String, String> generateTempVars(String command, String fullCmd, String[] args) {
		Map<String, String> tempVars = new HashMap<>();
		String[] start = command.split(":");
		if(start.length == 1) {
			tempVars.put("prefix", start[0]);
			tempVars.put("label", start[0]);
		} else {
			tempVars.put("prefix", start[0]);
			tempVars.put("label", start[1]);
		}
		// All the arguments
		tempVars.put("args", String.join(" ", args));
		// Full command
		tempVars.put("command", command + fullCmd);
		// Count of arguments
		tempVars.put("argscount", Integer.toString(args.length));
		// Just command
		tempVars.put("arg0", command);
		for(int i = 0; i < args.length; i++) {
			// [i] argument
			tempVars.put("arg" + (i+1), args[i]);
		}
		StringBuilder builder = new StringBuilder();
		for(int j = args.length - 1; j >= 0; j--) {
			builder.append(" ").append(args[j]);
			// Arguments after [j] argument
			tempVars.put("args" + j, builder.toString().substring(1));
		}
		return tempVars;
	}

	// @SuppressWarnings("unchecked")
	private static void unregisterAll(/*CommandMap commandMap*/) {
		if(commands.isEmpty()) return;
		/*
		TODO: Command unregister
		try {
			final Field f = commandMap.getClass().getDeclaredField("knownCommands");
			f.setAccessible(true);
			Map<String, Command> cmds = (Map<String, Command>) f.get(commandMap);
			commands.keySet().forEach(cmds::remove);
			f.set(commandMap, cmds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		commands.clear();
	}

	private static boolean register(String command, String prefix, List<String> aliases, CommandMap commandMap, RaCommand raCommand, boolean toBukkit) {
		if(Util.isStringEmpty(command)) return false;
		command = command.toLowerCase();
		prefix = Util.isStringEmpty(prefix) ? command : prefix.toLowerCase();
		if(aliases == null)
			aliases = new ArrayList<>();
		// Registering main command
		if(toBukkit) commandMap.register(prefix, raCommand);
		commands.put(command, raCommand);
		commands.put(prefix + ":" + command, raCommand);
		// ReActions.getPlugin().getCommand(raCommand.getName()).setTabCompleter(tabCompleter);
		// Registering aliases
		for(String alias : aliases) {
			if(toBukkit) commandMap.register(alias, prefix, raCommand);
			commands.put(alias, raCommand);
			commands.put(prefix + ":" + alias, raCommand);
		}
		return true;
	}

	private static Set<RaCommand> getCommandsSet() {
		return new HashSet<>(commands.values());
	}

	public static List<String> list() {
		List<String> list = new ArrayList<>();
		for(RaCommand cmd : getCommandsSet()) {
			List<String> sublist = cmd.list();
			sublist.forEach(s -> list.add(ChatColor.UNDERLINE + "/" + cmd.getName() + ChatColor.RESET + " " + s));
		}
		return list;
	}
}
