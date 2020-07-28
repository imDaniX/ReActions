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


package me.fromgate.reactions.logic.activators;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.storages.DoorStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.BlockUtils;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.BlockParameters;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class DoorActivator extends Activator implements Locatable {
    String state; //open, close
    //координаты нижнего блока двери
    String world;
    int x;
    int y;
    int z;

    private DoorActivator(ActivatorBase base, String state, String world, int x, int y, int z) {
        super(base);
        this.state = state;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static DoorActivator create(ActivatorBase base, Parameters p) {
        if (!(p instanceof BlockParameters)) return null;
        BlockParameters param = (BlockParameters) p;
        Block targetBlock = param.getBlock();
        if (targetBlock == null || BlockUtils.isOpenable(targetBlock)) {
            String state = param.getString("state", "ANY");
            if (!(state.equalsIgnoreCase("open") || state.equalsIgnoreCase("close"))) state = "ANY";
            String world = targetBlock.getWorld().getName();
            int x = targetBlock.getX();
            int y = targetBlock.getY();
            int z = targetBlock.getZ();
            return new DoorActivator(base, state, world, x, y, z);
        } else return null;
    }

    public static DoorActivator load(ActivatorBase base, ConfigurationSection cfg) {
        String state = cfg.getString("state", "ANY");
        if (!(state.equalsIgnoreCase("open") || state.equalsIgnoreCase("close"))) state = "ANY";
        String world = cfg.getString("world");
        int x = cfg.getInt("x");
        int y = cfg.getInt("y");
        int z = cfg.getInt("z");
        return new DoorActivator(base, state, world, x, y, z);
    }

    @Override
    public boolean activate(Storage event) {
        DoorStorage de = (DoorStorage) event;
        if (de.getDoorBlock() == null) return false;
        if (!isLocatedAt(de.getDoorLocation())) return false;
        if (this.state.equalsIgnoreCase("open") && de.isDoorOpened()) return false;
        return !this.state.equalsIgnoreCase("close") || (de.isDoorOpened());
    }

    @Override
    public boolean isLocatedAt(Location l) {
        if (l == null) return false;
        if (!world.equals(l.getWorld().getName())) return false;
        if (x != l.getBlockX()) return false;
        if (y != l.getBlockY()) return false;
        return (z == l.getBlockZ());
    }

    @Override
    public boolean isLocatedAt(World world, int x, int y, int z) {
        return this.world.equals(world.getName()) &&
                this.x == x &&
                this.y == y &&
                this.z == z;
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("world", this.world);
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("z", z);
        cfg.set("state", state);
        cfg.set("lever-state", null);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.DOOR;
    }

    @Override
    public boolean isValid() {
        return !Utils.isStringEmpty(world);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append(world).append(", ").append(x).append(", ").append(y).append(", ").append(z);
        sb.append("; state:").append(this.state.toUpperCase(Locale.ENGLISH));
        sb.append(")");
        return sb.toString();
    }
}
