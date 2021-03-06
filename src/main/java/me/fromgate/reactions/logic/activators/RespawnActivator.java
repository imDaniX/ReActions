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

import me.fromgate.reactions.logic.storages.RespawnStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.enums.DeathCause;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class RespawnActivator extends Activator {

    private final DeathCause deathCause;

    private RespawnActivator(ActivatorLogic base, DeathCause cause) {
        super(base);
        this.deathCause = cause;
    }

    public static RespawnActivator create(ActivatorLogic base, Parameters param) {
        DeathCause cause = DeathCause.getByName(param.getString("cause", param.toString()));
        return new RespawnActivator(base, cause);
    }

    public static RespawnActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        DeathCause cause = DeathCause.getByName(cfg.getString("death-cause", "ANY"));
        return new RespawnActivator(base, cause);
    }

    @Override
    public boolean check(Storage event) {
        RespawnStorage pe = (RespawnStorage) event;
        return this.deathCause == DeathCause.ANY || pe.getDeathCause() == this.deathCause;
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("death-cause", deathCause.name());
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.RESPAWN;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("(").append(this.deathCause.name()).append(")");
        return sb.toString();
    }
}