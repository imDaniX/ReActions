package me.fromgate.reactions.playerselector;

import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "loc")
public class LocSelector implements Selector {
    @Override
    public Set<Player> selectPlayers(String param) {
        Set<Player> players = new HashSet<>();
        if (param.isEmpty()) return players;
        Parameters params = new Parameters(param, "loc");
        String locStr = params.getParam("loc");
        if (locStr.isEmpty()) return players;
        Location loc = LocationUtils.parseLocation(locStr, null);
        if (loc == null) return players;
        loc.setX(loc.getBlockX() + 0.5);
        loc.setY(loc.getBlockY() + 0.5);
        loc.setZ(loc.getBlockZ() + 0.5);
        double radius = params.getParam("radius", 1.0);
        radius *= radius;
        for (Player player : loc.getWorld().getPlayers())
            if (player.getLocation().distanceSquared(loc) <= radius) players.add(player);
        return players;
    }

}
