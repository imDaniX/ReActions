package me.fromgate.reactions.externals.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.fromgate.reactions.VariablesManager;
import org.bukkit.OfflinePlayer;

import java.util.regex.Pattern;

public class RaPapiExpansion extends PlaceholderExpansion {
    private static final String IDENTIFIER = "reactions";
    private static final String AUTHOR = "fromgate";
    private static final String VERSION = "0.0.3";
    private static final Pattern VARP = Pattern.compile("(?i)varp?:\\S+");

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
        if (VARP.matcher(s).find()) {
            return VariablesManager.getInstance().getVariable(player.getName(), s);
        }
        return "";
    }
}
