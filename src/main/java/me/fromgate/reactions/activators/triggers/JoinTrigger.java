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

package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.JoinStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class JoinTrigger extends Trigger {

    private final boolean firstJoin;

    private JoinTrigger(ActivatorBase base, boolean firstJoin) {
        super(base);
        this.firstJoin = firstJoin;
    }

    public static JoinTrigger create(ActivatorBase base, Parameters param) {
        boolean firstJoin = param.toString().contains("first");
        return new JoinTrigger(base, firstJoin);
    }

    public static JoinTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        boolean firstJoin = cfg.getString("join-state", "ANY").equalsIgnoreCase("first");
        return new JoinTrigger(base, firstJoin);
    }

    @Override
    public boolean proceed(Storage event) {
        JoinStorage ce = (JoinStorage) event;
        return isJoinActivate(ce.isFirstJoin());
    }

    private boolean isJoinActivate(boolean joinFirstTime) {
        if (this.firstJoin) return joinFirstTime;
        return true;
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
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
