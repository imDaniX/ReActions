package me.fromgate.reactions.logic.triggers;

import org.bukkit.Location;
import org.bukkit.World;

public interface Locatable {
    boolean isLocatedAt(Location loc);

    boolean isLocatedAt(World world, int x, int y, int z);
}
