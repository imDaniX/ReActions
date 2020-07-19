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

package me.fromgate.reactions.actions;

import me.fromgate.reactions.time.TimersManager;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.parameter.Param;

public class ActionTimer extends Action {
    private final boolean pauseTimer;
	/*	TIMER_STOP("timerstop",false,new ActionTimer(false)),
	TIMER_RESUME("timerresume",false,new ActionTimer(true));
	*/

    public ActionTimer(boolean pauseTimer) {
        this.pauseTimer = pauseTimer;
    }

    @Override
    public boolean execute(RaContext context, Param params) {
        String timer = params.getParam("timer", "");
        if(timer.isEmpty()) return false;
        return TimersManager.setPause(timer, pauseTimer);
    }

}
