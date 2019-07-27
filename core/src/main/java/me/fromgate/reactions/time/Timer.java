/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
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

package me.fromgate.reactions.time;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.message.Msg;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Timer {

	@Getter @Setter private boolean paused;
	@Getter private boolean ingameTimer;
	@Getter private Param params;
	@Getter private Set<String> timesIngame;
	private CronExpression timeServer;

	public Timer(Param params2) {
		this.timesIngame = new HashSet<>();
		this.params = params2;
		this.ingameTimer = params2.getParam("timer-type", "ingame").equalsIgnoreCase("ingame");
		this.paused = params2.getParam("paused", false);
		params2.set("paused", String.valueOf(this.paused));
		this.parseTime();

	}

	public void parseTime() {
		if (this.ingameTimer) {
			this.timesIngame = new HashSet<>();
			this.timesIngame.addAll(Arrays.asList(params.getParam("time", "").split(",\\S*")));
		} else {
			String time = params.getParam("time", "").replace("_", " ");
			try {
				this.timeServer = new CronExpression(time);
			} catch (ParseException e) {
				Msg.logOnce(time, "Failed to parse cron format: " + time);
				this.timeServer = null;
				e.printStackTrace();
			}
		}
	}


	public boolean isTimeToRun() {
		if (isPaused()) return false;
		return this.ingameTimer ? isIngameTimeToRun() : isServerTimeToRun();
	}

	private boolean isServerTimeToRun() {
		if (this.ingameTimer) return false;
		if (this.timeServer == null) return false;
		return (this.timeServer.isSatisfiedBy(new Date()));
	}

	private boolean isIngameTimeToRun() {
		return ingameTimer && timesIngame.contains(TimeUtil.currentIngameTime());
	}

	@Override
	public String toString() {
		return params.getParam("activator", "Undefined") + " : " + params.getParam("time", "Undefined") + (this.isIngameTimer() ? " (ingame)" : " (server)");
	}
}
