package me.fromgate.reactions.externals.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.VariablesManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.StringUtil;

public class RaPapiExpansion extends PlaceholderExpansion {
    private static final String IDENTIFIER = "reactions";
    private static final String AUTHOR = "fromgate";
    private static final String VERSION = "0.0.3";

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String getAuthor() {
        return AUTHOR;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String onRequest(OfflinePlayer player, String param) {
        VariablesManager variables = ReActions.getVariables();

        if (StringUtil.startsWithIgnoreCase(param, "varp:")) {
            return player.getName() == null ?
                   null :
                   variables.getVariable(player.getName(), param.substring(5));
        } else if (StringUtil.startsWithIgnoreCase(param, "var:")) {
            String[] split = param.substring(4).split("\\.", 2);
            return split.length > 1 ?
                   variables.getVariable(split[0], split[1]) :
                   variables.getVariable(null, split[0]);
        }

        return null;
    }
}
