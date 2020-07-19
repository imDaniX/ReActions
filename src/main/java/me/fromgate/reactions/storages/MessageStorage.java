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


package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.StringValue;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.regex.Pattern;

public class MessageStorage extends Storage {
    public static final String MESSAGE = "message";

    private final static Pattern NOT_D = Pattern.compile("\\D+");
    @Getter
    private final String message;
    private final MessageActivator activator;


    public MessageStorage(Player player, MessageActivator activator, String message) {
        super(player, ActivatorType.MESSAGE);
        this.activator = activator;
        this.message = message;
    }

    public boolean isForActivator(MessageActivator messageActivator) {
        return this.activator.equals(messageActivator);
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("message", message);
        String[] args = message.split(" ");
        int countInt = 0;
        int countNum = 0;
        if(args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                tempVars.put("word" + (i + 1), args[i]);
                tempVars.put("wnum" + (i + 1), NOT_D.matcher(args[i]).replaceAll(""));
                if(Util.INT.matcher(args[i]).matches()) {
                    countInt++;
                    tempVars.put("int" + countInt, args[i]);
                }
                if(Util.FLOAT.matcher(args[i]).matches()) {
                    countNum++;
                    tempVars.put("num" + countNum, args[i]);
                }
            }
        }
        tempVars.put("word-count", Integer.toString(args.length));
        tempVars.put("int-count", Integer.toString(countInt));
        tempVars.put("num-count", Integer.toString(countNum));
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
        changeables.put(MESSAGE, new StringValue(message));
    }

}
