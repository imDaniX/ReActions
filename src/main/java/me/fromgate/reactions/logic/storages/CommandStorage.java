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

import lombok.Getter;
import me.fromgate.reactions.logic.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

public class CommandStorage extends Storage {
    @Getter
    private final String label, command;
    @Getter
    private final String[] args;
    @Getter
    private final CommandSender sender;

    public CommandStorage(Player p, CommandSender sender, String command) {
        super(p, ActivatorType.COMMAND);
        this.sender = sender;
        this.command = command;
        String[] split = command.split("\\s");
        this.label = split[0];
        this.args = Arrays.copyOfRange(split, 1, split.length);
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        String[] start = label.split(":");
        if (start.length == 1) {
            tempVars.put("prefix", start[0]);
            tempVars.put("label", start[0]);
        } else {
            tempVars.put("prefix", start[0]);
            tempVars.put("label", start[1]);
        }
        // All the arguments
        tempVars.put("args", String.join(" ", args));
        // Full command
        tempVars.put("command", command);
        // Count of arguments
        tempVars.put("argscount", Integer.toString(args.length));
        // Just command
        tempVars.put("arg0", label);
        for (int i = 0; i < args.length; i++) {
            // [i] argument
            tempVars.put("arg" + (i + 1), args[i]);
        }
        StringBuilder builder = new StringBuilder();
        for (int j = args.length - 1; j >= 0; j--) {
            builder.append(" ").append(args[j]);
            // Arguments after [j] argument
            tempVars.put("args" + j, builder.toString().substring(1));
        }
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
    }
}
