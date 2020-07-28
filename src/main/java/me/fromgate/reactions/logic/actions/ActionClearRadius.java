package me.fromgate.reactions.logic.actions;

import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.mob.EntityUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by MaxDikiy on 20/10/2017.
 */
public class ActionClearRadius extends Action {
    // TODO: Too weird. Optimize, simplify

    @Override
    public boolean execute(RaContext context, Parameters params) {
        int radius = params.getInteger("radius", 0);
        String type = params.getString("type", "all");
        if (radius == 0) return false;
        List<Location> locs = LocationUtils.getMinMaxRadiusLocations(context.getPlayer(), radius);
        context.setTempVariable("loc1", LocationUtils.locationToString(locs.get(0)));
        context.setTempVariable("loc2", LocationUtils.locationToString(locs.get(1)));
        if (locs.size() != 2) return false;
        Collection<Entity> en = EntityUtils.getEntities(locs.get(0), locs.get(1));
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
        return (Utils.isWordInList(e.getType().name().toLowerCase(Locale.ENGLISH), type.toLowerCase(Locale.ENGLISH)));
    }

}
