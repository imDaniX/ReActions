package me.fromgate.reactions.externals.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.fromgate.reactions.ReActions;
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
    public String onRequest(OfflinePlayer player, String s) {
        if (StringUtil.startsWithIgnoreCase(s, "var")) {
            return ReActions.getVariables().getVariable(player.getName(), s);
        }
        return null;
    }
}
