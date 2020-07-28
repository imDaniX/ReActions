package me.fromgate.reactions.externals.worldedit;

import lombok.Value;
import org.bukkit.Location;
import org.bukkit.World;

@Value
public class WeSelection {

    String selType;
    Location min;
    Location max;
    int area;
    World world;
    String region;

    public boolean isValid() {
        return world != null && region != null && !region.isEmpty() && min != null && max != null && area != -1;
    }
}
