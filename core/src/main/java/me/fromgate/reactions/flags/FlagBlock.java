package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FlagBlock implements Flag {

	@Override
	public boolean checkFlag(RaContext context, String param) {
		Player player = context.getPlayer();
		Param params = new Param(param, "loc");
		Location loc = LocationUtil.parseLocation(params.getParam("loc", ""), player.getLocation());
		if (loc == null) return false;
		String istr = params.getParam("block", "");
		if (istr.isEmpty()) return loc.getBlock().getType() != Material.AIR;
		Param block = new Param(istr);
		String type = block.getParam("type", block.getParam("param-line", "AIR"));
		return loc.getBlock().getType().name().equalsIgnoreCase(type);
	}

}
