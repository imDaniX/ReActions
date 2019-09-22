package me.fromgate.reactions.placeholders;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.Variables;
import me.fromgate.reactions.externals.placeholderapi.RaPlaceholderAPI;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Redesign all the system of placeholders.
public class PlaceholdersManager {
	private static int placeholderCounter = 0;
	@Getter @Setter private static int countLimit = 127;
	private final static Pattern PATTERN_RAW = Pattern.compile("%raw:((%\\w+%)|(%\\w+:\\w+%)|(%\\w+:\\S+%))%");
	private final static Pattern PATTERN_ANY = Pattern.compile("(%\\w+%)|(%\\w+:\\w+%)|(%\\w+:\\S+%)");

	private static Set<Placeholder> placeholders = new HashSet<>();

	public static void init() {
		add(new PlaceholderPlayer());
		add(new PlaceholderMoney());
		add(new PlaceholderRandom());
		add(new PlaceholderTime());
		add(new PlaceholderCalc());
	}

	public static boolean add(Placeholder ph) {
		if (ph == null) return false;
		if (ph.getKeys().length == 0) return false;
		if (ph.getId().equalsIgnoreCase("UNKNOWN")) return false;
		placeholders.add(ph);
		return true;
	}

	public static String replacePlaceholderButRaw(RaContext context, String original) {
		List<String> raws = new ArrayList<>();
		String result = original;
		Matcher matcher = PATTERN_RAW.matcher(Matcher.quoteReplacement(result));
		StringBuffer sb = new StringBuffer();
		int count = 0;
		while (matcher.find()) {
			raws.add(matcher.group().replaceAll("(^%raw:)|(%$)", ""));
			matcher.appendReplacement(sb, "§~[RAW" + count + "]");
			count++;
		}
		matcher.appendTail(sb);
		result = replacePlaceholders(context, sb.toString());
		if (!raws.isEmpty()) {
			for (int i = 0; i < raws.size(); i++) {
				result = result.replace("§~[RAW" + i + "]", raws.get(i));
			}
		}
		return result;
	}

	private static String replacePlaceholders(RaContext context, String original) {
		Player player = context.getPlayer();
		String result = original;
		result = replaceTempVars(context.getTempVariables(), result);
		result = Variables.replacePlaceholders(player, result);
		Matcher matcher = PATTERN_ANY.matcher(Matcher.quoteReplacement(result));
		StringBuffer sb = new StringBuffer();
		String group;
		String replacement;
		while (matcher.find()) {
			group = "%" +
					replacePlaceholders(context,
							matcher.group().replaceAll("(^%)|(%$)", "")) +
					"%";
			replacement = replacePlaceholder(player, group);
			matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement == null ? group : replacement));
		}
		matcher.appendTail(sb);
		result = sb.toString();
		if (!original.equals(result) && countPlaceholder()) result = replacePlaceholders(context, result);
		result = RaPlaceholderAPI.processPlaceholder(player, result);
		placeholderCounter = 0;
		return result;
	}

	private static String replaceTempVars(Map<String, String> tempVars, String str) {
		if (str.isEmpty()) return str;
		String newStr = str;
		for (String key : tempVars.keySet()) {
			String replacement = tempVars.get(key);
			replacement = Util.FLOAT_WITHZERO.matcher(replacement).matches() ?
					Integer.toString((int) Double.parseDouble(replacement)) :
					replacement; // Matcher.quoteReplacement(replacement);
			newStr = newStr.replaceAll("(?i)%" + key + "%", replacement);
		}
		return newStr;
	}

	private final static Pattern PH_W_S = Pattern.compile("(%\\w+:\\S+%)");

	private static String replacePlaceholder(Player player, String field) {
		String key = field.replaceAll("^%", "").replaceAll("%$", "");
		String value = "";
		if (PH_W_S.matcher(field).matches()) {
			value = field.replaceAll("^%\\w+:", "").replaceAll("%$", "");
			key = key.replaceAll(Pattern.quote(":" + value) + "$", "");
		}
		for (Placeholder ph : placeholders) {
			if (ph.checkKey(key)) return ph.processPlaceholder(player, key, value);
		}
		return field;
	}

	public static void listPlaceholders(CommandSender sender, int pageNum) {
		List<String> phList = new ArrayList<>();
		for (Placeholder ph : placeholders) {
			for (String phKey : ph.getKeys()) {
				if (phKey.toLowerCase().equals(phKey)) continue;
				Msg desc = Msg.getByName("placeholder_" + phKey);
				if (desc == null) {
					Msg.LNG_FAIL_PLACEHOLDER_DESC.log(phKey);
				} else {
					phList.add("&6" + phKey + "&3: &a" + desc.getText("NOCOLOR"));
				}
			}
		}
		for (Flags f : Flags.values()) {
			if (f != Flags.TIME && f != Flags.CHANCE) continue;
			String name = f.name();
			Msg desc = Msg.getByName("placeholder_" + name);
			if (desc == null) {
				Msg.LNG_FAIL_PLACEHOLDER_DESC.log(name);
			} else {
				phList.add("&6" + name + "&3: &a" + desc.getText("NOCOLOR"));
			}
		}
		phList.add("&6VAR&3: &a" + Msg.PLACEHOLDER_VAR.getText("NOCOLOR"));
		phList.add("&6SIGN_LOC, SIGN_LINE1,.. SIGN_LINE4&3: &a" + Msg.PLACEHOLDER_SIGNACT.getText("NOCOLOR"));
		phList.add("&6ARG0, ARG1, ARG2...&3: &a" + Msg.PLACEHOLDER_COMMANDACT.getText("NOCOLOR"));
		Msg.printPage(sender, phList, Msg.MSG_PLACEHOLDERLISTTITLE, pageNum, sender instanceof Player ? 10 : 1000);
	}

	/**
	 * @return Is it allowed to process placeholder
	 */
	public static boolean countPlaceholder() {
		return placeholderCounter++ < countLimit;
	}
}
