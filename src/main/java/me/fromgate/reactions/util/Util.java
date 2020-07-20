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

package me.fromgate.reactions.util;

import me.fromgate.reactions.util.location.LocationUtil;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Util {

    // Byte
    public final static Pattern BYTE = Pattern.compile("(2[1-5][1-6]|\\d{1,2})");
    // Integer
    public final static Pattern INT_POSITIVE = Pattern.compile("\\d+");
    public final static Pattern INT = Pattern.compile("-?\\d+");
    public final static Pattern INT_NOTZERO_POSITIVE = Pattern.compile("[1-9]\\d*");
    public final static Pattern INT_NOTZERO = Pattern.compile("-?[1-9]\\d*");
    public final static Pattern INT_MIN_MAX = Pattern.compile("\\d+(-\\d+)?");
    // Float
    public final static Pattern FLOAT_POSITIVE = Pattern.compile("\\d+(\\.\\d+)?");
    public final static Pattern FLOAT = Pattern.compile("-?\\d+(\\.\\d+)?");
    public final static Pattern FLOAT_WITHZERO = Pattern.compile("^\\d+\\.0$");
    private final static ThreadLocalRandom random = ThreadLocalRandom.current();

    public static String removeSpaces(String str) {
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ') bld.append(c);
        }
        return bld.toString();
    }

    public static double getDouble(String str, double def) {
        if (isStringEmpty(str) || !FLOAT.matcher(str).matches()) return def;
        return Double.parseDouble(str);
    }

    /**
     * Get random value by min and max values
     *
     * @param numsStr String with min-max values or just max value(e.g. "2-47", "76")
     * @return Random value
     */
    public static int getMinMaxRandom(String numsStr) {
        if (numsStr.contains("-")) {
            int min = 0;
            int max = 0;
            String minStr;
            String maxStr;
            int index = numsStr.indexOf('-');
            minStr = numsStr.substring(0, index);
            maxStr = numsStr.substring(index + 1);
            if (INT_POSITIVE.matcher(minStr).matches())
                min = Integer.parseInt(minStr);
            if (INT_POSITIVE.matcher(maxStr).matches())
                max = Integer.parseInt(maxStr);
            if (max > min)
                return min + getRandomInt(1 + max - min);
            return min;
        } else {
            if (INT_POSITIVE.matcher(numsStr).matches())
                return Integer.parseInt(numsStr);
            return 0;
        }
    }

    /**
     * Play sound on location
     *
     * @param loc    Location where sound should be played
     * @param params Parameters of sound
     * @return Name of played sound
     */
    public static String soundPlay(Location loc, Parameters params) {
        if (params.isEmpty()) return "";
        Location soundLoc = loc;
        String sndstr = "";
        String strvolume = "1";
        String strpitch = "1";
        float pitch = 1;
        float volume = 1;
        if (params.hasAnyParam("param")) {
            String param = params.getParam("param", "");
            if (param.isEmpty()) return "";
            if (param.contains("/")) {
                String[] prm = param.split("/");
                if (prm.length > 1) {
                    sndstr = prm[0];
                    strvolume = prm[1];
                    if (prm.length > 2) strpitch = prm[2];
                }
            } else sndstr = param;
            if (FLOAT_POSITIVE.matcher(strvolume).matches()) volume = Float.parseFloat(strvolume);
            if (FLOAT_POSITIVE.matcher(strpitch).matches()) pitch = Float.parseFloat(strpitch);
        } else {
            String locationStr = params.getParam("loc");
            soundLoc = locationStr.isEmpty() ? loc : LocationUtil.parseLocation(locationStr, null);
            sndstr = params.getParam("type", "");
            pitch = params.getParam("pitch", 1.0f);
            volume = params.getParam("volume", 1.0f);
        }
        Sound sound = getEnum(Sound.class, sndstr, Sound.UI_BUTTON_CLICK);
        if (soundLoc != null) soundLoc.getWorld().playSound(soundLoc, sound, volume, pitch);
        return sound.name();
    }

    /**
     * Play sound on location
     *
     * @param loc   Location where sound should be played
     * @param param Parameters of sound
     */
    public static void soundPlay(Location loc, String param) {
        if (param.isEmpty()) return;
        Parameters params = new Parameters(param, "param");
        soundPlay(loc, params);
    }

    /**
     * Check string is empty or null
     *
     * @param str String to check
     * @return Is string empty or null
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Check if word contained in string with ","
     *
     * @param word Word to search
     * @param str  String with words
     * @return Is word contained
     */
    public static boolean isWordInList(String word, String str) {
        String[] ln = str.split(",");
        if (ln.length > 0)
            for (String wordInList : ln) {
                if (wordInList.equalsIgnoreCase(word)) return true;
            }
        return false;
    }

    /**
     * Roll dice-100
     *
     * @param chance Chance of success(until 100)
     * @return Is roll successful
     */
    public static boolean rollDiceChance(int chance) {
        return (random.nextInt(100) < chance);
    }

    /**
     * Get random integer
     *
     * @param maxvalue Maximal random value
     * @return Random integer
     */
    public static int getRandomInt(int maxvalue) {
        return random.nextInt(maxvalue);
    }

    // TODO: Should be removed or refactored

    public static boolean isIntegerSigned(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!INT.matcher(s).matches()) return false;
        return true;
    }

    public static boolean isInteger(String str) {
        return (INT_POSITIVE.matcher(str).matches());
    }

    public static boolean isInteger(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!INT_POSITIVE.matcher(s).matches()) return false;
        return true;
    }

    public static boolean isIntegerGZ(String str) {
        return INT_NOTZERO_POSITIVE.matcher(str).matches();
    }

    public static boolean isIntegerGZ(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!INT_NOTZERO_POSITIVE.matcher(s).matches()) return false;
        return true;
    }

    public static boolean isNumber(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!FLOAT.matcher(s).matches()) return false;
        return true;
    }

    /**
     * Check if string contains positive float
     *
     * @param numStr String to check
     * @return Is string contains positive float
     */
    public static boolean isFloat(String numStr) {
        return FLOAT_POSITIVE.matcher(numStr).matches();
    }

    // *************************************

    /**
     * Safe transition from long to int
     *
     * @param l Long to transit
     * @return Final int
     */
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        if (l > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int) l;
    }

    public static UUID getUUID(OfflinePlayer player) {
        return Bukkit.getOnlineMode() ?
                player.getUniqueId() :
                UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("deprecation")
    public static UUID getUUID(String player) {
        Player p = getPlayerExact(player);
        if (p == null) {
            OfflinePlayer offP = Bukkit.getOfflinePlayer(player);
            if (offP != null) return getUUID(offP);
        } else return getUUID(p);
        return null;
    }

    /**
     * Escape java symbols
     *
     * @param doco String to escape
     * @return Escaped string
     */
    public static String escapeJava(String doco) {
        if (doco == null)
            return "";

        StringBuilder b = new StringBuilder();
        for (char c : doco.toCharArray()) {
            switch (c) {
                case '\r':
                    b.append("\\r");
                    continue;
                case '\n':
                    b.append("\\n");
                    continue;
                case '"':
                    b.append("\\\"");
                    continue;
                case '\\':
                    b.append("\\\\");
                    continue;
                default:
                    b.append(c);
            }
        }
        return b.toString();
    }

    /**
     * Get array of integers sorted by value
     *
     * @param arg1 First integer
     * @param arg2 Second integer
     * @return Array of integers, where first value is minimal
     */
    public static int[] sortedIntPair(int arg1, int arg2) {
        int[] pair = new int[2];
        if (arg1 > arg2) {
            pair[0] = arg2;
            pair[1] = arg1;
        } else {
            pair[0] = arg1;
            pair[1] = arg2;
        }
        return pair;
    }

    /**
     * Generate list with empty strings
     *
     * @param size Size of list
     * @return List with specified size
     */
    public static List<String> getEmptyList(int size) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < size; i++) l.add("");
        return l;
    }

    /**
     * Actually {@link Bukkit#getPlayerExact(String)}, but *not* deprecated
     *
     * @param name Nickname of player
     * @return Player with specified name or null
     */
    public static Player getPlayerExact(String name) {
        if (name != null)
            for (Player player : Bukkit.getOnlinePlayers())
                if (player.getName().equalsIgnoreCase(name)) return player;
        return null;
    }

    /**
     * Get list of names of all online players
     *
     * @return List of names
     */
    public static List<String> getPlayersList() {
        List<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
        return players;
    }

    /**
     * Check permissions
     *
     * @param user User to check
     * @param perm Permission to check
     * @return Is permission is null or user has permissions
     */
    public static boolean checkPermission(Permissible user, String perm) {
        return perm == null || user.hasPermission(perm);
    }

    /**
     * Search for notnull element in array
     *
     * @param def Default value if element wasn't found
     * @param obj Array of objects to search
     * @param <T> Type of object
     * @return Searched object or default if not found
     */
    @SafeVarargs
    public static <T> T searchNotNull(T def, T... obj) {
        for (T searched : obj)
            if (searched != null) return searched;
        return def;

    }

    /**
     * Trim 4+ numbers after dot
     *
     * @param d Number to trim
     * @return Trimed double
     */
    public static double trimDouble(double d) {
        long i = (long) (d * 1000);
        return ((double) i) / 1000;
    }

    /**
     * Idk yet
     */
    public static String implode(String... data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            //data.length - 1 => to not add separator at the end
            if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
                sb.append(data[i]);
                sb.append(File.separator);
            }
        }
        sb.append(data[data.length - 1].trim());
        return sb.toString();
    }

    /**
     * Get any enum by it's name
     *
     * @param <T>   Enum type
     * @param clazz Enum class
     * @param name  Name of enum
     * @return Corresponding enum, or null if not found
     */
    public static <T extends Enum<T>> T getEnum(Class<T> clazz, String name) {
        return getEnum(clazz, name, null);
    }

    /**
     * Get any enum by it's name or default value if not found
     *
     * @param <T>   Enum type
     * @param clazz Enum class
     * @param name  Name of enum
     * @return Corresponding enum, or null if not found
     */
    public static <T extends Enum<T>> T getEnum(Class<T> clazz, String name, T def) {
        if (clazz != null && !Util.isStringEmpty(name)) {
            try {
                return Enum.valueOf(clazz, name.toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return def;
    }

    public static boolean containsValue(String str, String... values) {
        for (String s : values)
            if (s.equalsIgnoreCase(str)) return true;
        return false;
    }
}
