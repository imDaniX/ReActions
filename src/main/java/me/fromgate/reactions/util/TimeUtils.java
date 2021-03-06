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

package me.fromgate.reactions.util;

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.util.math.NumberUtils;
import org.bukkit.Bukkit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class TimeUtils {
    private final Pattern TIME_HH_MM = Pattern.compile("^[0-5][0-9]:[0-5][0-9]$");
    private final Pattern TIME_HH_MM_SS = Pattern.compile("^([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
    private final Pattern TIME_X_MSDHMST = Pattern.compile("\\d+([dhmst]|ms)");
    private final Pattern TIME_X_MS = Pattern.compile("^\\d+ms$");
    private final Pattern TIME_X_D = Pattern.compile("^\\d+d$");
    private final Pattern TIME_X_H = Pattern.compile("^\\d+h$");
    private final Pattern TIME_X_M = Pattern.compile("^\\d+m$");
    private final Pattern TIME_X_S = Pattern.compile("^\\d+s$");
    private final Pattern TIME_X_T = Pattern.compile("^\\d+t$");

    public long ingameTime() {
        return Bukkit.getWorlds().get(0).getTime();
    }

    public String formattedIngameTime() {
        return formattedIngameTime(Bukkit.getWorlds().get(0).getTime(), false);
    }

    public String formattedIngameTime(long time, boolean showms) {
        return showms && (time < 1000) ?
                time + "ms" :
                (int) ((time / 1000 + 6) % 24) + ":" + (int) (60 * (time % 1000) / 1000);
    }

    public String fullTimeToString(long time, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(new Date(time));
    }

    public String fullTimeToString(long time) {
        return fullTimeToString(time, "dd-MM-YYYY HH:mm:ss");
		/*Date date = new Date(time);
		DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
		return formatter.format(date);*/
    }


    public long timeToTicks(long time) {
        //1000 ms = 20 ticks
        return Math.max(1, (time / 50));
    }

    public long parseTime(String time) {
        int dd = 0; // дни
        int hh = 0; // часы
        int mm = 0; // минуты
        int ss = 0; // секунды
        int tt = 0; // тики
        int ms = 0; // миллисекунды
        if (NumberUtils.isInteger(time)) {
            ss = Integer.parseInt(time);
        } else if (TIME_HH_MM.matcher(time).matches()) {
            String[] ln = time.split(":");
            mm = Integer.parseInt(ln[0]);
            ss = Integer.parseInt(ln[1]);
        } else if (TIME_HH_MM_SS.matcher(time).matches()) {
            String[] ln = time.split(":");
            hh = Integer.parseInt(ln[0]);
            mm = Integer.parseInt(ln[1]);
            ss = Integer.parseInt(ln[2]);
        } else {
            Matcher matcher = TIME_X_MSDHMST.matcher(time);
            while (matcher.find()) {
                String foundTime = matcher.group();
                if (TIME_X_MS.matcher(foundTime).matches())
                    ms = Integer.parseInt(foundTime.substring(0, foundTime.length() - 2));
                else if (TIME_X_D.matcher(foundTime).matches())
                    dd = Integer.parseInt(foundTime.substring(0, foundTime.length() - 1));
                else if (TIME_X_H.matcher(foundTime).matches())
                    hh = Integer.parseInt(foundTime.substring(0, foundTime.length() - 1));
                else if (TIME_X_M.matcher(foundTime).matches())
                    mm = Integer.parseInt(foundTime.substring(0, foundTime.length() - 1));
                else if (TIME_X_S.matcher(foundTime).matches())
                    ss = Integer.parseInt(foundTime.substring(0, foundTime.length() - 1));
                else if (TIME_X_T.matcher(foundTime).matches())
                    tt = Integer.parseInt(foundTime.substring(0, foundTime.length() - 1));
            }
        }
        return Math.max(1, (dd * 86400000L) + (hh * 3600000L) + (mm * 60000L) + (ss * 1000L) + (tt * 50L) + ms);
    }
}
