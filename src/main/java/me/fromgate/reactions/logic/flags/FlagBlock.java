package me.fromgate.reactions.logic.flags;

import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FlagBlock implements Flag {

    @Override
    public boolean checkFlag(RaContext context, String param) {
        Player player = context.getPlayer();
        Parameters params = new Parameters(param, "loc");
        Location loc = LocationUtils.parseLocation(params.getParam("loc", ""), player.getLocation());
        if (loc == null) return false;
        String istr = params.getParam("block", "");
        if (istr.isEmpty()) return loc.getBlock().getType() != Material.AIR;
        Parameters block = new Parameters(istr);
        String type = block.getParam("type", block.getParam("param-line", "AIR"));
        return loc.getBlock().getType().name().equalsIgnoreCase(type);
    }

}
