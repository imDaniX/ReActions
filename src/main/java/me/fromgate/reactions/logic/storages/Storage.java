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

package me.fromgate.reactions.logic.storages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.RaContext;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

/**
 * Storages are used to transfer some data to activators
 */
@Getter
@FieldDefaults(level=AccessLevel.PRIVATE)
public abstract class Storage {
    public static final String CANCEL_EVENT = "cancel_event";

    protected final Player player;
    final ActivatorType type;
    final boolean async;
    // Default temporary placeholders
    Map<String, String> variables;
    Map<String, DataValue> changeables;

    public Storage(Player player, ActivatorType type) {
        this(player, type, false);
    }

    public Storage(Player player, ActivatorType type, boolean async) {
        this.player = player;
        this.type = type;
        this.async = async;
    }

    public final void init() {
        variables = prepareVariables();
        changeables = prepareChangeables();
    }

    protected Map<String, String> prepareVariables() {
        return Collections.emptyMap();
    }

    protected Map<String, DataValue> prepareChangeables() {
        return Collections.emptyMap();
    }

    public final RaContext generateContext(String activator) {
        return new RaContext(activator, variables, changeables, player);
    }

}
