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

package me.fromgate.reactions.activators;

import me.fromgate.reactions.storages.JoinStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;

public class JoinActivator extends Activator {

    private final boolean firstJoin;

    private JoinActivator(ActivatorBase base, boolean firstJoin) {
        super(base);
        this.firstJoin = firstJoin;
    }

    public static JoinActivator create(ActivatorBase base, Param param) {
        boolean firstJoin = param.toString().contains("first");
        return new JoinActivator(base, firstJoin);
    }

    public static JoinActivator load(ActivatorBase base, ConfigurationSection cfg) {
        boolean firstJoin = cfg.getString("join-state", "ANY").equalsIgnoreCase("first");
        return new JoinActivator(base, firstJoin);
    }

    @Override
    public boolean activate(Storage event) {
        JoinStorage ce = (JoinStorage) event;
        return isJoinActivate(ce.isFirstJoin());
    }

    private boolean isJoinActivate(boolean joinFirstTime) {
        if (this.firstJoin) return joinFirstTime;
        return true;
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("join-state", (firstJoin ? "TRUE" : "ANY"));
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.JOIN;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (first join:").append(this.firstJoin).append(")");
        return sb.toString();
    }

}
