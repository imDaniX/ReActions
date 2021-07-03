package me.fromgate.reactions.selectors;

import me.fromgate.reactions.externals.RaVault;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "group")
public class GroupSelector implements Selector {

    @Override
    public Set<Player> selectPlayers(String param) {
        Set<Player> players = new HashSet<>();
        if (!RaVault.isPermissionConnected()) return players;
        if (param.isEmpty()) return players;
        String[] group = param.split(",\\s*");
        for (Player player : Bukkit.getOnlinePlayers())
            for (String g : group)
                if (RaVault.playerInGroup(player, g)) players.add(player);
        return players;
    }
}
