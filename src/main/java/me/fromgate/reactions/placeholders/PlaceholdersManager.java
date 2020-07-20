package me.fromgate.reactions.placeholders;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.Variables;
import me.fromgate.reactions.externals.placeholderapi.RaPlaceholderAPI;
import me.fromgate.reactions.logic.Flags;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Redesign all the system of placeholders.
public class PlaceholdersManager {
    private final static Pattern PATTERN_RAW = Pattern.compile("%raw:((%\\w+%)|(%\\w+:\\w+%)|(%\\w+:\\S+%))%");
    private static final Pattern PLACEHOLDER_GREEDY = Pattern.compile("%\\S+%");
    private static final Pattern PLACEHOLDER_NONGREEDY = Pattern.compile("%\\S+?%");
    private static int placeholderCounter = 0;
    @Getter
    @Setter
    private static int countLimit = 127;
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
        while (matcher.find()) {
            String group = matcher.group();
            matcher.appendReplacement(sb, "§~[RAW" + raws.size() + "]");
            raws.add(group.substring(5, group.length() - 1));
        }
        matcher.appendTail(sb);
        result = parsePlaceholders(sb.toString(), context);
        if (!raws.isEmpty()) {
            for (int i = 0; i < raws.size(); i++) {
                result = result.replace("§~[RAW" + i + "]", raws.get(i));
            }
        }
        return result;
    }

    private static String parsePlaceholders(String text, RaContext context) {
        if (text == null || text.length() < 3) return text;
        String oldText;
        int limit = countLimit;
        do {
            oldText = text;
            text = parseRecursive(text, PLACEHOLDER_GREEDY, context);
            text = parseRecursive(text, PLACEHOLDER_NONGREEDY, context);
        } while (--limit > 0 && !text.equals(oldText));
        return text;
    }

    private static String parseRecursive(String text, final Pattern phPattern, final RaContext context) {
        Matcher phMatcher = phPattern.matcher(text);
        // If found at least one
        if (phMatcher.find()) {
            StringBuffer buf = new StringBuffer();
            processIteration(buf, phMatcher, phPattern, context);
            while (phMatcher.find()) {
                processIteration(buf, phMatcher, phPattern, context);
            }
            return phMatcher.appendTail(buf).toString();
        }
        return text;
    }

    // Just some sh!tty stuff
    private static void processIteration(StringBuffer buffer, Matcher matcher, Pattern pattern, RaContext context) {
        matcher.appendReplacement(
                buffer,
                Matcher.quoteReplacement(
                        replacePlaceholder(
                                parseRecursive(
                                        crop(matcher.group()),
                                        pattern,
                                        context
                                ),
                                context
                        )
                )
        );
    }

    private static String crop(String text) {
        return text.substring(1, text.length() - 1);
    }

    private static String replacePlaceholder(String text, RaContext context) {
        String result = context.getTempVariable(text);
        if (result != null) return result;
        result = Variables.getVariable(context.getPlayer(), text, null);
        if (result != null) return result;
        for (Placeholder placeholder : placeholders) {
            String[] ph = result.split(":", 2);
            result = placeholder.processPlaceholder(context.getPlayer(), ph[0], ph.length > 1 ? ph[1] : ph[0]);
            if (result != null) return result;
        }
        return RaPlaceholderAPI.processPlaceholder(context.getPlayer(), result);
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
}
