package me.fromgate.reactions.externals.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RaPlaceholderAPI {

    private static boolean enabled = false;

    public static void init() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            enabled = true;
            new RaPapiExpansion().register();
            Msg.logMessage("Connected to PlaceholderAPI");
        }
    }

    public static String processPlaceholder(Player player, String text) {
        return enabled ? PlaceholderAPI.setPlaceholders(player, text) : text;
    }
}
