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

package me.fromgate.reactions.time;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.FileUtil;
import me.fromgate.reactions.util.data.RaContext;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Delayer {

	private static Map<String, Long> delays = new HashMap<>();

	public static void save() {
		YamlConfiguration cfg = new YamlConfiguration();
		File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "delay.yml");
		for (String key : delays.keySet()) {
			long delayTime = delays.get(key);
			if (delayTime > System.currentTimeMillis())
				cfg.set(key, delayTime);
		}
		FileUtil.saveCfg(cfg, f, "Failed to save delays configuration file");
	}

	public static void load() {
		delays.clear();
		YamlConfiguration cfg = new YamlConfiguration();
		File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "delay.yml");
		if(FileUtil.loadCfg(cfg, f, "Failed to load delay configuration file"))
			for (String key : cfg.getKeys(true)) {
				if (!key.contains(".")) continue;
				long delayTime = cfg.getLong(key);
				if (delayTime > System.currentTimeMillis())
					delays.put(key, delayTime);
			}
	}

	public static boolean checkDelay(String id, long updateTime) {
		String idd = (id.contains(".") ? id : "global." + id);
		boolean result = !delays.containsKey(idd) || delays.get(idd) < System.currentTimeMillis();
		if (result && updateTime > 0) Delayer.setDelay(idd, updateTime, false);
		return result;
	}

	public static boolean checkPersonalDelay(String playerName, String id, long updateTime) {
		return checkDelay(playerName + "." + id, updateTime);
	}

	public static void setDelay(String id, long delayTime, boolean add) {
		setDelaySave(id, delayTime, true, add);
	}

	public static void setDelaySave(String id, long delayTime, boolean save, boolean add) {
		String delayId = id.contains(".") ? id : "global." + id;
		long currentDelay = add && delays.containsKey(delayId) ? delays.get(delayId) : System.currentTimeMillis();
		delays.put(delayId, delayTime + currentDelay);
		if (save) save();
	}

	public static void setPersonalDelay(String playerName, String id, long delayTime, boolean add) {
		setDelay(playerName + "." + id, delayTime, add);
	}

	public static void printDelayList(CommandSender sender, int pageNum, int linePerPage) {
		Set<String> lst = new TreeSet<>();
		for (String key : delays.keySet()) {
			long delayTime = delays.get(key);
			if (delayTime < System.currentTimeMillis()) continue;
			String[] ln = key.split("\\.", 2);
			if (ln.length != 2) continue;
			lst.add("[" + ln[0] + "] " + ln[1] + ": " + TimeUtil.fullTimeToString(delays.get(key)));
		}
		Msg.printPage(sender, lst, Msg.MSG_LISTDELAY, pageNum, linePerPage, true);
	}

	public static String[] getStringTime(String playerName, String id) {
		String fullId = (id.contains(".") ? id : (playerName == null || playerName.isEmpty() ? "global." + id : playerName + "." + id));
		if (checkDelay(fullId, 0)) return null;
		if (!delays.containsKey(fullId)) return null;
		long time = delays.get(fullId);
		String[] times = new String[8];
		times[0] = TimeUtil.fullTimeToString(time, "dd-MM-YYYY HH:mm:ss");
		times[1] = TimeUtil.fullTimeToString(time, "HH:mm:ss");
		time = time - System.currentTimeMillis();

		int sec = (int) (time / 1000);
		int min = sec / 60;
		sec = sec % 60;
		int hour = min / 60;
		min = min % 60;
		int days = hour / 24;
		hour = hour % 24;

		times[2] = String.format("%dd %02d:%02d:%02d", days, hour, min, sec);
		times[3] = String.format("%02d:%02d:%02d", hour, min, sec);
		times[4] = String.format("%02d", hour);
		times[5] = String.format("%02d", min);
		times[6] = String.format("%02d", sec);
		StringBuilder sb = new StringBuilder();
		if (days > 0) sb.append(days).append("d");
		if (days > 0 || hour > 0) sb.append(" ").append(hour).append("h");
		if (days > 0 || hour > 0 || min > 0) sb.append(" ").append(min).append("m");
		if (sb.length() == 0 || sec > 0) sb.append(" ").append(sec).append("s");
		times[7] = sb.toString().trim();
		return times;
	}

	public static void setTempPlaceholders(RaContext context, String playerName, String id) {
		String[] times = Delayer.getStringTime(playerName, id);
		if (times != null) {
			context.setTempVariable("delay-fulltime", times[0]);
			context.setTempVariable("delay-time", times[1]);
			context.setTempVariable("delay-left", times[7]);
			context.setTempVariable("delay-left-full", times[2]);
			context.setTempVariable("delay-left-hms", times[3]);
			context.setTempVariable("delay-left-hh", times[4]);
			context.setTempVariable("delay-left-mm", times[5]);
			context.setTempVariable("delay-left-ss", times[6]);
		}
	}

}
