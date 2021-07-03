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

import lombok.experimental.UtilityClass;
import me.fromgate.reactions.util.location.LocationUtils;
import me.fromgate.reactions.util.math.NumberUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@UtilityClass
public class Utils {
    private final String[] EMPTY_ARRAY = new String[0];

    public String removeChar(String str, char remove) {
        StringBuilder bld = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c != remove) bld.append(c);
        }
        return bld.toString();
    }

    /**
     * Play sound on location
     *
     * @param loc    Location where sound should be played
     * @param params Parameters of sound
     * @return Name of played sound
     */
    public String soundPlay(Location loc, Parameters params) {
        if (params.isEmpty()) return "";
        Location soundLoc = loc;
        String sndstr = "";
        String strvolume = "1";
        String strpitch = "1";
        float pitch = 1;
        float volume = 1;
        if (params.contains("param")) {
            String param = params.getString("param", "");
            if (param.isEmpty()) return "";
            if (param.contains("/")) {
                String[] prm = param.split("/");
                if (prm.length > 1) {
                    sndstr = prm[0];
                    strvolume = prm[1];
                    if (prm.length > 2) strpitch = prm[2];
                }
            } else sndstr = param;
            if (NumberUtils.FLOAT_POSITIVE.matcher(strvolume).matches()) volume = Float.parseFloat(strvolume);
            if (NumberUtils.FLOAT_POSITIVE.matcher(strpitch).matches()) pitch = Float.parseFloat(strpitch);
        } else {
            String locationStr = params.getString("loc");
            soundLoc = locationStr.isEmpty() ? loc : LocationUtils.parseLocation(locationStr, null);
            sndstr = params.getString("type", "");
            pitch = (float) params.getDouble("pitch", 1.0f);
            volume = (float) params.getDouble("volume", 1.0f);
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
    public void soundPlay(Location loc, String param) {
        if (param.isEmpty()) return;
        Parameters params = Parameters.fromString(param, "param");
        soundPlay(loc, params);
    }

    /**
     * Check string is empty or null
     *
     * @param str String to check
     * @return Is string empty or null
     */
    @Contract("null -> true")
    public boolean isStringEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Check if word contained in string with ","
     *
     * @param word Word to search
     * @param str  String with words
     * @return Is word contained
     */
    public boolean isWordInList(String word, String str) {
        String[] ln = str.split(",");
        if (ln.length > 0)
            for (String wordInList : ln) {
                if (wordInList.equalsIgnoreCase(word)) return true;
            }
        return false;
    }

    // *************************************

    public UUID getUUID(OfflinePlayer player) {
        return Bukkit.getOnlineMode() ?
                player.getUniqueId() :
                UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("deprecation")
    public UUID getUUID(String playerName) {
        Player player = getPlayerExact(playerName);
        return player == null ?
                getUUID(Bukkit.getOfflinePlayer(playerName)) :
                getUUID(player);
    }

    /**
     * Escape java symbols
     *
     * @param doco String to escape
     * @return Escaped string
     */
    public String escapeJava(String doco) {
        if (doco == null)
            return "";

        StringBuilder b = new StringBuilder();
        for (char c : doco.toCharArray()) {
            switch (c) {
                case '\r' -> {
                    b.append("\\r");
                    continue;
                }
                case '\n' -> {
                    b.append("\\n");
                    continue;
                }
                case '"' -> {
                    b.append("\\\"");
                    continue;
                }
                case '\\' -> {
                    b.append("\\\\");
                    continue;
                }
                default -> b.append(c);
            }
        }
        return b.toString();
    }

    /**
     * Generate list with empty strings
     *
     * @param size Size of list
     * @return List with specified size
     */
    public List<String> getEmptyList(int size) {
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
    public Player getPlayerExact(String name) {
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
    public List<String> getPlayersList() {
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
    public boolean checkPermission(Permissible user, String perm) {
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
    public <T> T searchNotNull(T def, T... obj) {
        for (T searched : obj)
            if (searched != null) return searched;
        return def;

    }

    /**
     * Idk yet
     */
    public String implode(String... data) {
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
    public <T extends Enum<T>> T getEnum(Class<T> clazz, String name) {
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
    public <T extends Enum<T>> T getEnum(Class<T> clazz, String name, T def) {
        if (clazz != null && !Utils.isStringEmpty(name)) {
            try {
                return Enum.valueOf(clazz, name.toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return def;
    }

    public boolean containsValue(String str, String... values) {
        for (String s : values)
            if (s.equalsIgnoreCase(str)) return true;
        return false;
    }

    public static String[] getAliases(Class<?> clazz) {
        if(clazz.isAnnotationPresent(Alias.class)) {
            return clazz.getAnnotation(Alias.class).value();
        }
        return EMPTY_ARRAY;
    }

    public static String[] getAliases(Object obj) {
        return getAliases(obj.getClass());
    }
}
