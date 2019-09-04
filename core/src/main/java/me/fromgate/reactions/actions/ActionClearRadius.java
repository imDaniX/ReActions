package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.mob.EntityUtil;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

/**
 * Created by MaxDikiy on 20/10/2017.
 */
public class ActionClearRadius extends Action {
	// TODO: Too weird. Optimize, simplify

	@Override
	public boolean execute(Player p, Param params) {
		int radius = params.getParam("radius", 0);
		String type = params.getParam("type", "all");
		if (radius == 0) return false;
		List<Location> locs = LocationUtil.getMinMaxRadiusLocations(p, radius);
		Variables.setTempVar("loc1", LocationUtil.locationToString(locs.get(0)));
		Variables.setTempVar("loc2", LocationUtil.locationToString(locs.get(1)));
		if (locs.size() != 2) return false;
		Collection<Entity> en = EntityUtil.getEntities(locs.get(0), locs.get(1));
		int count = 0;
		for (Entity e : en) {
			if (e.getType() == EntityType.PLAYER) continue;
			if (isEntityIsTypeOf(e, type)) {
				e.remove();
				count++;
			}
		}
		setMessageParam(Integer.toString(count));
		return true;
	}

	private boolean isEntityIsTypeOf(Entity e, String type) {
		if (e == null) return false;
		if (type.isEmpty()) return true;
		if (type.equalsIgnoreCase("all")) return true;
		if (e instanceof LivingEntity) {
			if (type.equalsIgnoreCase("mob") || type.equalsIgnoreCase("mobs")) return true;
		} else {
			if (type.equalsIgnoreCase("item") || type.equalsIgnoreCase("items")) return true;
		}
		return (Util.isWordInList(e.getType().name().toLowerCase(), type.toLowerCase()));
	}

}
