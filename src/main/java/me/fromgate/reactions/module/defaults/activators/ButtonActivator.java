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


package me.fromgate.reactions.module.defaults.activators;

import me.fromgate.reactions.logic.ActivatorLogic;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Locatable;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.storages.ButtonStorage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.BlockParameters;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class ButtonActivator extends Activator implements Locatable {
    private final String world;
    private final int x;
    private final int y;
    private final int z;

    private ButtonActivator(ActivatorLogic base, String world, int x, int y, int z) {
        super(base);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static ButtonActivator create(ActivatorLogic base, Parameters p) {
        if (!(p instanceof BlockParameters param)) return null;
        Location loc = param.getBlock().getLocation();
        return new ButtonActivator(base, loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static ButtonActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        int x = cfg.getInt("x", 0);
        int y = cfg.getInt("y", 0);
        int z = cfg.getInt("z", 0);
        String world = cfg.getString("world", Bukkit.getWorlds().get(0).getName());
        return new ButtonActivator(base, world, x, y, z);
    }

    @Override
    public boolean checkStorage(Storage event) {
        ButtonStorage be = (ButtonStorage) event;
        return isLocatedAt(be.getButtonLocation());
    }

    public boolean isLocatedAt(Location l) {
        if (l == null) return false;
        if (!world.equalsIgnoreCase(l.getWorld().getName())) return false;
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
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("world", this.world);
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("z", z);
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
        sb.append(")");
        return sb.toString();
    }

}
