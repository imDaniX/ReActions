package me.fromgate.reactions.selectors;

import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class SelectorsManager {
    // TODO Unstatic

    private static Set<Selector> selectors;
    private static Set<String> keys;

    public static void init() {
        selectors = new HashSet<>();
        keys = new HashSet<>();
        addSelector(new PlayerSelector());
        addSelector(new WorldSelector());
        addSelector(new LocSelector());
        addSelector(new GroupSelector());
        addSelector(new PermSelector());
        addSelector(new RegionSelector());
    }

    public static void addSelector(Selector selector) {
        if (selector == null) return;
        if (selector.getKey() == null) return;
        selectors.add(selector);
        keys.add(selector.getKey());
    }

    public static Set<Player> getPlayerList(Parameters param) {
        Set<Player> players = new HashSet<>();
        for (Selector selector : selectors) {
            String selectorParam = param.getString(selector.getKey());
            if (selector.getKey().equalsIgnoreCase("loc") && param.contains("radius"))
                selectorParam = "loc:" + selectorParam + " " + "radius:" + param.getString("radius", "1");
            players.addAll(selector.selectPlayers(selectorParam));
        }
        return players;
    }

    public static Set<String> getAllKeys() {
        return keys;
    }
}
