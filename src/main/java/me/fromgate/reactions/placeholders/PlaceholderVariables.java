package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.VariablesManager;
import me.fromgate.reactions.util.Alias;
import me.fromgate.reactions.util.data.RaContext;

@Alias({"varp", "variable"})
public class PlaceholderVariables implements Placeholder.Prefixed {
    @Override
    public String processPlaceholder(RaContext context, String prefix, String text) {
        String result;
        switch (prefix) {
            case "var":
            case "variable":
                String[] varSplit = text.split("\\.", 2);
                if(varSplit.length > 1) {
                    result = VariablesManager.getInstance().getVariable(varSplit[0], varSplit[1]);
                } else {
                    result = VariablesManager.getInstance().getVariable("", varSplit[0]);
                }
                return result;

            case "varp":
                result = VariablesManager.getInstance().getVariable(context.getPlayer(), text);
                return result;

            default:
                return null;
        }
    }

    @Override
    public String getPrefix() {
        return "var";
    }
}
