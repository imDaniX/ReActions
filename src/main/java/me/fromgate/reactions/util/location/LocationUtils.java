/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *
 *  This file is part of ReActions.
 *
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 *
 */


package me.fromgate.reactions.util.location;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.holders.LocationHolder;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.math.Rng;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class LocationUtils {
    public Location ZERO_LOCATION = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

    /**
     * Returns location defined by group of parameter:
     *
     * @param param           String that contains parameters:
     *                        parameter1:value1 parameter2:value2 ... parameterN:valueN
     *                        Parameters:
     *                        <WorldName,X,Y,Z[,Yaw,Pitch]> - default, simplest way to define location
     *                        loc:<WorldName,X,Y,Z[,Yaw,Pitch]> - same as previous
     *                        <p>
     *                        loc:<WorldName,X,Y,Z[,Yaw,Pitch]> radius:<Radius> - find random location around the defined block
     *                        region:<RegionName> - find random location in provided region
     *                        loc1:<WorldName,X,Y,Z> loc2:<WorldName,X,Y,Z> - find random location in area defined by too points
     *                        <p>
     *                        Additional parameters:
     *                        land:true - forces to find location in point where player can stay (solid block with two blocks above it)
     *                        add-vector:<X,Y,Z> - allows to modify result of locations selections. For example,
     *                        loc:world,10,10,10 add-vector:0,5,0 will point to action world,10,15,10.
     * @param defaultLocation default location, used when definitions of locations is wrong or missed
     * @return Location
     */
    public Location parseLocation(String param, Location defaultLocation) {
        Parameters params = Parameters.fromString(param, "loc");
        return parseLocation(params, defaultLocation);
    }

    public Location parseLocation(Parameters params, Location defaultLocation) {
        Location location = null;
        if (params.contains("loc")) {
            String locStr = params.getString("loc", "");
            location = LocationHolder.getTpLoc(locStr);
            if (location == null) location = parseCoordinates(locStr);
        }

        boolean land = params.getBoolean("land", true);
        if (params.contains("region")) {
            location = getRegionLocation(params.getString("region", ""), land);
            location = copyYawPitch(location, defaultLocation);
        }

        if (params.containsEvery("loc1", "loc2")) {
            Location loc1 = parseCoordinates(params.getString("loc1", ""));
            Location loc2 = parseCoordinates(params.getString("loc2", ""));
            if (loc1 != null && loc2 != null) {
                location = getCubeLocation(loc1, loc2, land);
                location = copyYawPitch(location, defaultLocation);
            }
        }
        if (params.contains("radius")) {
            int radius = params.getInteger("radius", -1);
            if (radius > 0) {
                location = getRadiusLocation(location == null ? defaultLocation : location, radius, land);
                location = copyYawPitch(location, defaultLocation);
            }
        }
        Vector vector = LocationUtils.parseVector(params.getString("add-vector", ""));
        Location result = location == null ? defaultLocation : location;
        if (result != null && vector != null) result.add(vector);
        return result;
    }

    public Location copyYawPitch(Location targetLoc, Location sourceLoc) {
        if (targetLoc == null || sourceLoc == null) return targetLoc;
        targetLoc.setYaw(sourceLoc.getYaw());
        targetLoc.setPitch(sourceLoc.getPitch());
        return targetLoc;
    }

    public Location getCubeLocation(Location loc1, Location loc2, boolean land) {
        List<Location> minmax = new ArrayList<>();
        minmax.add(new Location(loc1.getWorld(), Math.min(loc1.getBlockX(), loc2.getBlockX()),
                Math.min(loc1.getBlockY(), loc2.getBlockY()),
                Math.min(loc1.getBlockZ(), loc2.getBlockZ())));
        minmax.add(new Location(loc1.getWorld(), Math.max(loc1.getBlockX(), loc2.getBlockX()),
                Math.max(loc1.getBlockY(), loc2.getBlockY()),
                Math.max(loc1.getBlockZ(), loc2.getBlockZ())));
        return getMinMaxLocation(minmax, land);
    }

    public Location getRegionLocation(String regionStr, boolean land) {
        List<Location> minmax = RaWorldGuard.getRegionMinMaxLocations(regionStr);
        if (minmax.isEmpty()) return null;
        return getMinMaxLocation(minmax, land);
    }


    public Location getRadiusLocation(Location center, int radius, boolean land) {
        List<Location> locs = new ArrayList<>();
        if (radius <= 16) {
            for (int x = -radius; x <= radius; x++)
                for (int y = -radius; y <= radius; y++)
                    for (int z = -radius; z <= radius; z++) {
                        Location loc = (new Location(center.getWorld(),
                                center.getBlockX() + x,
                                center.getBlockY() + y,
                                center.getBlockZ() + z)).add(0.5, 0.5, 0.5);
                        if (loc.getBlockY() < 0 || loc.getBlockY() >= loc.getWorld().getMaxHeight()) continue;
                        if (loc.distance(center) <= radius) locs.add(loc);
                    }
        } else {
            int x, y, z;
            int radiusSquared = radius * radius;
            do {
                x = Rng.nextInt(radius * 2 + 1) - radius;
                y = Rng.nextInt(radius * 2 + 1) - radius;
                z = Rng.nextInt(radius * 2 + 1) - radius;
            } while (radiusSquared < x * x + z * z + y * y);

            for (y = Math.max(center.getBlockY() - radius, 0); y <= Math.min(center.getBlockY() + radius, center.getWorld().getMaxHeight() - 1); y++) {
                if (radiusSquared < x * x + z * z + y * y)
                    locs.add(new Location(center.getWorld(), center.getBlockX() + x, y, center.getBlockZ() + z));
            }
        }
        if (locs.isEmpty()) locs.add(center);
        return getEmptyOrLandedLocations(locs, land);
    }

    public Location parseCoordinates(String strloc) {
        Location loc;
        if (strloc.isEmpty()) return null;
        String[] ln = strloc.split(",");
        if (!((ln.length == 4) || (ln.length == 6))) return null;
        World w = Bukkit.getWorld(ln[0]);
        if (w == null) return null;
        for (int i = 1; i < ln.length; i++) {
            if (!NumberUtils.FLOAT.matcher(ln[i]).matches()) return null;
        }
        loc = new Location(w, Double.parseDouble(ln[1]), Double.parseDouble(ln[2]), Double.parseDouble(ln[3]));
        if (ln.length == 6) {
            loc.setYaw(Float.parseFloat(ln[4]));
            loc.setPitch(Float.parseFloat(ln[5]));
        }
        return loc;
    }

    public Vector parseVector(String vectorStr) {
        if (vectorStr.isEmpty()) return null;
        String[] ln = vectorStr.split(",");
        if (ln.length != 3) return null;
        for (String s : ln) {
            if (!NumberUtils.FLOAT.matcher(s).matches()) return null;
        }
        return new Vector(Double.parseDouble(ln[0]), Double.parseDouble(ln[1]), Double.parseDouble(ln[2]));
    }

    public boolean isLocationEmpty(Location loc) {
        Block block = loc.getBlock();
        return block.isPassable() && block.getRelative(BlockFace.UP).isPassable();
    }

    public boolean isLocationLandable(Location loc) {
        Block block = loc.getBlock();
        if (block.getRelative(BlockFace.DOWN).isPassable()) return false;
        return isLocationEmpty(loc);
    }

    public Location getRandomLocation(List<Location> locs) {
        if (locs.isEmpty()) return null;
        return locs.get(Rng.nextInt(locs.size()));
    }

    public Location getEmptyOrLandedLocations(List<Location> locs, boolean land) {
        List<Location> landLocs = new ArrayList<>();
        for (Location loc : locs) {
            if (land) {
                if (isLocationLandable(loc)) landLocs.add(loc);
            } else {
                if (isLocationEmpty(loc)) landLocs.add(loc);
            }
        }
        return landLocs.isEmpty() ? getRandomLocation(locs) : getRandomLocation(landLocs);
    }

    public Location getMinMaxLocation(List<Location> minmax, boolean land) {
        if (minmax.isEmpty()) return null;
        int x = minmax.get(0).getBlockX() + Rng.nextInt(minmax.get(1).getBlockX() - minmax.get(0).getBlockX() + 1);
        int z = minmax.get(0).getBlockZ() + Rng.nextInt(minmax.get(1).getBlockZ() - minmax.get(0).getBlockZ() + 1);
        List<Location> locations = new ArrayList<>();
        for (int y = minmax.get(0).getBlockY(); y <= minmax.get(1).getBlockY(); y++) {
            locations.add(new Location(minmax.get(0).getWorld(), x, y, z));
        }
        return getEmptyOrLandedLocations(locations, land);
    }

    public String locationToStringFormatted(Location loc) {
        if (loc == null) return "";
        DecimalFormat fmt = new DecimalFormat("####0.##");
        String lstr = loc.toString();
        try {
            lstr = "[" + loc.getWorld().getName() + "] " + fmt.format(loc.getX()) + ", " + fmt.format(loc.getY()) + ", " + fmt.format(loc.getZ());
        } catch (Exception ignored) {
        }
        return lstr;
    }

    public String locationToString(Block block) {
        if (block == null) return "";
        return block.getWorld().getName()
                + "," + block.getX()
                + "," + block.getY()
                + "," + block.getZ();
    }

    public String locationToString(Location loc) {
        if (loc == null || loc.getWorld() == null) return "";
        return loc.getWorld().getName() + "," +
                NumberUtils.trimDouble(loc.getX()) + "," +
                NumberUtils.trimDouble(loc.getY()) + "," +
                NumberUtils.trimDouble(loc.getZ()) + "," +
                (float) NumberUtils.trimDouble(loc.getYaw()) + "," +
                (float) NumberUtils.trimDouble(loc.getPitch());
    }

    public List<Location> getMinMaxRadiusLocations(Player p, int radius) {
        List<Location> locs = new ArrayList<>();
        Location loc = p.getLocation();
        World world = p.getWorld();
        locs.add(new Location(world, loc.getBlockX() + radius, loc.getBlockY() + radius, loc.getBlockZ() + radius));
        locs.add(new Location(world, loc.getBlockX() - radius, loc.getBlockY() - radius, loc.getBlockZ() - radius));
        return locs;
    }

    public boolean equals(Location loc1, Location loc2) {
        return loc1.getWorld().equals(loc2.getWorld()) &&
                loc1.getX() == loc2.getX() &&
                loc1.getZ() == loc2.getZ() &&
                loc1.getY() == loc2.getY();
    }

    public String parsePlaceholders(Player p, String param) {
        if (p == null) return param;
        Location targetBlock = null;
        try {
            targetBlock = p.getTargetBlock(null, 100).getLocation();
        } catch (NullPointerException ignored) {
        }
        Map<String, Location> locs = new HashMap<>();
        locs.put("%here%", p.getLocation());
        locs.put("%eye%", p.getEyeLocation());
        locs.put("%head%", p.getEyeLocation());
        locs.put("%viewpoint%", targetBlock);
        locs.put("%view%", targetBlock);
        locs.put("%selection%", LocationHolder.getHeld(p));
        locs.put("%select%", LocationHolder.getHeld(p));
        locs.put("%sel%", LocationHolder.getHeld(p));
        String newparam = param;
        for (String key : locs.keySet()) {
            Location l = locs.get(key);
            if (l == null) continue;
            newparam = newparam.replace(key, locationToString(l));
        }
        return newparam;
    }

    @FunctionalInterface
    public interface LocationFunction<R> {
        R apply(World world, int x, int y, int z);
    }
}
