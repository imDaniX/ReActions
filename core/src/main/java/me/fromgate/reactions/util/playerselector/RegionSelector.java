package me.fromgate.reactions.util.playerselector;

import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "region")
public class RegionSelector implements Selector {

	@Override
	public Set<Player> selectPlayers(String regionStr) {
		Set<Player> players = new HashSet<>();
		if (!RaWorldGuard.isConnected()) return players;
		if (regionStr.isEmpty()) return players;
		String[] arrRegion = regionStr.split(",\\s*");
		for (String regionName : arrRegion)
			players.addAll(RaWorldGuard.playersInRegion(regionName));
		return players;
	}

}
