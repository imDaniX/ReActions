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

package me.fromgate.reactions.logic.actions;

import lombok.Setter;
import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.logic.Actions;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.entity.Player;


public abstract class Action {
    private Actions type = null;
    @Setter
    private String messageParam = "";
    private boolean actionExecuting = true;

    public void init(Actions at) {
        this.type = at;
    }

    public boolean isAction() {
        return this.actionExecuting;
    }

    public final void executeAction(RaContext context, boolean action, Parameters params) {
        this.actionExecuting = action;
        Player player = context.getPlayer();
        //this.activator = a;
        if (!params.hasAnyParam("param-line")) params.set("param-line", "");
        setMessageParam(params.getParam("param-line"));
        boolean actionFailed = (!execute(context, params));
        if ((player != null) && (printAction())) {
            Msg msg = Msg.getByName(("ACT_" + type.name() + (actionFailed ? "FAIL" : "")).toUpperCase());
            if (msg == null) {
                Msg.LNG_FAIL_ACTION_MSG.print(type.name());
            } else {
                msg.print(player, messageParam);
            }
        }
    }

    private boolean printAction() {
        return (Util.isWordInList(this.type.name(), Cfg.actionMsg) || Util.isWordInList(this.type.getAlias(), Cfg.actionMsg));
    }

    /**
     * Try to execute action
     *
     * @param context Context of activator
     * @param params  Parameters of action
     * @return Is action execution was successful
     */
    public abstract boolean execute(RaContext context, Parameters params);
}
