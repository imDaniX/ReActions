package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.playerselector.SelectorsManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Parameters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MaxDikiy on 2017-04-29.
 */
public class ActionRegex extends Action {

    @Override
    public boolean execute(RaContext context, Parameters params) {
        String prefix = params.getString("prefix", "");
        String regex = params.getString("regex", "");
        String input = params.getString("input", removeParams(params.toString()));

        if (input.isEmpty()) return false;

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        int count = -1;
        String group;

        while (m.find()) {
            count++;
            for (int i = 0; i <= m.groupCount(); i++) {
                if (m.group(i) != null) group = m.group(i);
                else group = "";
                context.setVariable(prefix + "group" + count + "" + i, group);
                context.setVariable(prefix + "group_" + count + "_" + i, group);
                context.setVariable(prefix + "group:" + count + ":" + i, group);
            }
        }
        return true;
    }

    // TODO: Remove it somehow
    private String removeParams(String message) {
        String sb = "(?i)(" + String.join("|", SelectorsManager.getAllKeys()) +
                "|hide|regex|prefix):(\\{.*\\}|\\S+)\\s?";
        return message.replaceAll(sb, "");

    }

}