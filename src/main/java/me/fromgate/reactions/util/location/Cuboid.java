package me.fromgate.reactions.util.location;

import lombok.Getter;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;

public class Cuboid {
    @Getter
    private final String world;
    @Getter
    private final int xMin;
    @Getter
    private final int xMax;
    @Getter
    private final int zMin;
    @Getter
    private final int zMax;
    @Getter
    private final Integer yMin;
    @Getter
    private final Integer yMax;

    public Cuboid(Location loc1, Location loc2) {
        this.world = loc1.getWorld().getName();
        int[] pair;
        pair = Util.sortedIntPair(loc1.getBlockX(), loc2.getBlockX());
        this.xMin = pair[0];
        this.xMax = pair[1];
        pair = Util.sortedIntPair(loc1.getBlockZ(), loc2.getBlockZ());
        this.zMin = pair[0];
        this.zMax = pair[1];
        pair = Util.sortedIntPair(loc1.getBlockY(), loc2.getBlockY());
        this.yMin = pair[0];
        this.yMax = pair[1];
    }

    public Cuboid(VirtualLocation loc1, VirtualLocation loc2) {
        this.world = loc1.getWorld();
        int[] pair;
        pair = Util.sortedIntPair(loc1.getX(0), loc2.getX(0));
        this.xMin = pair[0];
        this.xMax = pair[1];
        pair = Util.sortedIntPair(loc1.getZ(0), loc2.getZ(0));
        this.zMin = pair[0];
        this.zMax = pair[1];
        if (loc1.getY() == null || loc2.getY() == null) {
            yMin = null;
            yMax = null;
        } else {
            pair = Util.sortedIntPair(loc1.getY(), loc2.getY());
            this.yMin = pair[0];
            this.yMax = pair[1];
        }
    }

    public boolean isInside(Location loc, boolean head) {
        if (!loc.getWorld().getName().equalsIgnoreCase(world))
            return false;
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        if ((xMin > x || xMax < x) || (zMin > z || zMax < z))
            return false;
        if (yMin == null)
            return true;
        double y = loc.getY();
        return (y >= yMin && y <= yMax) || (head && (y + 1.75 >= yMin && y + 1.75 <= yMax));
    }

    // TODO: toString method
}
