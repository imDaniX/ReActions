package me.fromgate.reactions.selectors;

import me.fromgate.reactions.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "player")
public class PlayerSelector implements Selector {

    @Override
    public Set<Player> selectPlayers(String param) {
        Set<Player> players = new HashSet<>();
        if (param.isEmpty()) return players;
        if (param.equalsIgnoreCase("~null")) {
            players.add(null);
        } else if (param.equalsIgnoreCase("~all")) {
            players.addAll(Bukkit.getOnlinePlayers());
        } else {
            String[] arrPlayers = param.split(",\\s*");
            for (String playerName : arrPlayers) {
                Player targetPlayer = Utils.getPlayerExact(playerName);
                if ((targetPlayer != null) && (targetPlayer.isOnline())) players.add(targetPlayer);
            }
        }
        return players;
    }
}
