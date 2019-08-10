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

import com.google.common.base.Charsets;
import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.util.location.Holder;
import me.fromgate.reactions.util.location.Locator;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.mob.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Util {

	public final static Pattern BYTE = Pattern.compile("(2[1-5][1-6]|\\d{1,2})");

	public final static Pattern INT = Pattern.compile("\\d+");
	public final static Pattern INT_NEG = Pattern.compile("-?\\d+");
	public final static Pattern INT_MIN_MAX = Pattern.compile("\\d+(-\\d+)?");
	public final static Pattern INT_NOTZERO = Pattern.compile("[1-9]\\d*");
	public final static Pattern INT_NOTZERO_NEG = Pattern.compile("-?[1-9]\\d*");

	public final static Pattern FLOAT = Pattern.compile("\\d+(\\.\\d+)?");
	public final static Pattern FLOAT_ZERO = Pattern.compile("^\\d+\\.0$");
	public final static Pattern FLOAT_NEG = Pattern.compile("-?\\d+(\\.\\d+)?");

	private final static ThreadLocalRandom random = ThreadLocalRandom.current();

	/**
	 * Get random value by min and max values
	 * @param minmaxstr String with min-max values or just max value(e.g. "2-47", "76")
	 * @return Random value
	 */
	public static int getMinMaxRandom(String minmaxstr) {
		int min = 0;
		int max;
		String strmin = minmaxstr;
		String strmax = minmaxstr;

		if (minmaxstr.contains("-")) {
			strmin = minmaxstr.substring(0, minmaxstr.indexOf("-"));
			strmax = minmaxstr.substring(minmaxstr.indexOf("-") + 1);
		}
		if (INT_NOTZERO.matcher(strmin).matches()) min = Integer.parseInt(strmin);
		max = min;
		if (INT_NOTZERO.matcher(strmax).matches()) max = Integer.parseInt(strmax);
		if (max > min) return min + tryChance(1 + max - min);
		else return min;
	}

	/**
	 * Play sound on location
	 * @param loc Location where sound should be played
	 * @param params Parameters of sound
	 * @return Name of played sound
	 */
	public static String soundPlay(Location loc, Param params) {
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
			if (FLOAT.matcher(strvolume).matches()) volume = Float.parseFloat(strvolume);
			if (FLOAT.matcher(strpitch).matches()) pitch = Float.parseFloat(strpitch);
		} else {
			String locationStr = params.getParam("loc");
			soundLoc = locationStr.isEmpty() ? loc : Locator.parseLocation(locationStr, null);
			sndstr = params.getParam("type", "");
			pitch = params.getParam("pitch", 1.0f);
			volume = params.getParam("volume", 1.0f);
		}
		Sound sound = getSoundStr(sndstr);
		if (soundLoc != null) soundLoc.getWorld().playSound(soundLoc, sound, volume, pitch);
		return sound.name();
	}

	/**
	 * Play sound on location
	 * @param loc Location where sound should be played
	 * @param param Parameters of sound
	 * @return Name of played sound
	 */
	public static void soundPlay(Location loc, String param) {
		if (param.isEmpty()) return;
		/*Map<String,String> params = new HashMap<String,String>();
		params.put("param", param); */
		Param params = new Param(param, "param");
		soundPlay(loc, params);
	}

	/**
	 * Get Sound from string
	 * @param param Name of sound
	 * @return Sound from name or Sound.UI_BUTTON_CLICK
	 */
	private static Sound getSoundStr(String param) {
		try {
			return Sound.valueOf(param.toUpperCase());
		} catch (Exception ignored) {
			return Sound.UI_BUTTON_CLICK;
		}
	}


	public static String replaceStandardLocations(Player p, String param) {
		if (p == null) return param;
		Location targetBlock = null;
		try {
			targetBlock = p.getTargetBlock(null, 100).getLocation();
		} catch (Exception ignored) {
		}
		Map<String, Location> locs = new HashMap<>();
		locs.put("%here%", p.getLocation());
		locs.put("%eye%", p.getEyeLocation());
		locs.put("%head%", p.getEyeLocation());
		locs.put("%viewpoint%", targetBlock);
		locs.put("%view%", targetBlock);
		locs.put("%selection%", Holder.getHeldLocation(p));
		locs.put("%select%", Holder.getHeldLocation(p));
		locs.put("%sel%", Holder.getHeldLocation(p));
		String newparam = param;
		for (String key : locs.keySet()) {
			Location l = locs.get(key);
			if (l == null) continue;
			newparam = newparam.replace(key, Locator.locationToString(l));
		}
		return newparam;
	}

	public static PotionEffectType parsePotionEffect(String name) {
		PotionEffectType pef = null;
		try {
			pef = PotionEffectType.getByName(name);
		} catch (Exception ignored) {
		}
		return pef;
	}

	public static LivingEntity getAnyKiller(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
			if (evdmg.getDamager() instanceof LivingEntity) return (LivingEntity) evdmg.getDamager();
			if (evdmg.getCause() == DamageCause.PROJECTILE) {
				Projectile prj = (Projectile) evdmg.getDamager();
				return EntityUtil.getEntityFromProjectile(prj.getShooter());
			}
		}
		return null;
	}


	public static Player getKiller(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
			if (evdmg.getDamager().getType() == EntityType.PLAYER) return (Player) evdmg.getDamager();
			if (evdmg.getCause() == DamageCause.PROJECTILE) {
				Projectile prj = (Projectile) evdmg.getDamager();
				LivingEntity shooterEntity = EntityUtil.getEntityFromProjectile(prj.getShooter());
				if (shooterEntity == null) return null;
				if (shooterEntity instanceof Player) return (Player) shooterEntity;
			}
		}
		return null;
	}


	public static boolean isAnyParamExist(Map<String, String> params, String... param) {
		for (String key : params.keySet())
			for (String prm : param) {
				if (key.equalsIgnoreCase(prm)) return true;
			}
		return false;
	}

	public static String join(String... s) {
		StringBuilder sb = new StringBuilder();
		for (String str : s)
			sb.append(str);
		return sb.toString();
	}

	public static boolean emptyString(String str) {
		return str == null || str.isEmpty();
	}


	public static void printPage(CommandSender sender, List<String> list, Msg title, int page) {
		int pageHeight = (sender instanceof Player) ? 9 : 1000;
		if (title != null) title.print(sender);
		ChatPage chatPage = paginate(list, page, Cfg.chatLength, pageHeight);
		for (String str : chatPage.getLines()) {
			Msg.printMessage(sender, str);
		}

		if (pageHeight == 9) {
			Msg.LST_FOOTER.print(sender, 'e', '6', chatPage.getPageNumber(), chatPage.getTotalPages());
		}
	}

	public static ChatPage paginate(List<String> unpaginatedStrings, int pageNumber, int lineLength, int pageHeight) {
		List<String> lines = new ArrayList<>();
		for (String str : unpaginatedStrings) {
			lines.addAll(Arrays.asList(ChatPaginator.wordWrap(str, lineLength)));
		}
		int totalPages = lines.size() / pageHeight + (lines.size() % pageHeight == 0 ? 0 : 1);
		int actualPageNumber = pageNumber <= totalPages ? pageNumber : totalPages;
		int from = (actualPageNumber - 1) * pageHeight;
		int to = from + pageHeight <= lines.size() ? from + pageHeight : lines.size();
		String[] selectedLines = Arrays.copyOfRange(lines.toArray(new String[0]), from, to);
		return new ChatPage(selectedLines, actualPageNumber, totalPages);
	}

	/*
	 * Функция проверяет входит ли слово (String) в список слов
	 * представленных в виде строки вида n1,n2,n3,...nN
	 */
	public static boolean isWordInList(String word, String str) {
		String[] ln = str.split(",");
		if (ln.length > 0)
			for (String wordInList : ln) {
				if (wordInList.equalsIgnoreCase(word)) return true;
			}
		return false;
	}

	public static boolean rollDiceChance(int chance) {
		return (random.nextInt(100) < chance);
	}

	public static int tryChance(int chance) {
		return random.nextInt(chance);
	}


	public static int getRandomInt(int maxvalue) {
		return random.nextInt(maxvalue);
	}


	public static boolean isIntegerSigned(String... str) {
		if (str.length == 0) return false;
		for (String s : str)
			if (!INT_NEG.matcher(s).matches()) return false;
		return true;
	}

	public static boolean isInteger(String str) {
		return (INT.matcher(str).matches());
	}

	public static boolean isInteger(String... str) {
		if (str.length == 0) return false;
		for (String s : str)
			if (!INT.matcher(s).matches()) return false;
		return true;
	}


	public static boolean isIntegerGZ(String str) {
		return INT_NOTZERO.matcher(str).matches();
	}

	public static boolean isIntegerGZ(String... str) {
		if (str.length == 0) return false;
		for (String s : str)
			if (!INT_NOTZERO.matcher(s).matches()) return false;
		return true;
	}

	public static int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE) return Integer.MIN_VALUE;
		if (l > Integer.MAX_VALUE) return Integer.MAX_VALUE;
		return (int) l;
	}

	public static UUID getUUID(OfflinePlayer player) {
		return Bukkit.getOnlineMode() ?
				player.getUniqueId() :
				UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8));
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

	public static List<Location> getMinMaxRadiusLocations(Player p, int radius) {
		List<Location> locs = new ArrayList<>();
		Location loc = p.getLocation();
		World world = p.getWorld();
		locs.add(new Location(world, loc.getBlockX() + radius, loc.getBlockY() + radius, loc.getBlockZ() + radius));
		locs.add(new Location(world, loc.getBlockX() - radius, loc.getBlockY() - radius, loc.getBlockZ() - radius));
		Variables.setTempVar("loc1", Locator.locationToString(locs.get(0)));
		Variables.setTempVar("loc2", Locator.locationToString(locs.get(1)));
		return locs;
	}

	/**
	 * Escape java symbols
	 * @param doco String to escape
	 * @return Escaped string
	 */
	public static String escapeJava(String doco) {
		if (doco == null)
			return "";

		StringBuilder b = new StringBuilder();
		for (char c : doco.toCharArray()) {
			if (c == '\r')
				b.append("\\r");
			else if (c == '\n')
				b.append("\\n");
			else if (c == '"')
				b.append("\\\"");
			else if (c == '\\')
				b.append("\\\\");
			else
				b.append(c);
		}
		return b.toString();
	}

	/**
	 * Get array of integers sorted by value
	 * @param arg1 First integer
	 * @param arg2 Second integer
	 * @return Array of integers, where first value is minimal
	 */
	public static int[] sortedIntPair(int arg1, int arg2) {
		int[] pair = new int[2];
		if(arg1 > arg2) {
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
	 * @param name Nickname of player
	 * @return Player with specified name or null
	 */
	public static Player getPlayerExact(String name) {
		if(name != null)
			for(Player player : Bukkit.getOnlinePlayers())
				if(player.getName().equalsIgnoreCase(name)) return player;
		return null;
	}

	public static Boolean getBoolean(String str) {
		str = str.toLowerCase();
		if(str.equals("true")||str.equals("on")||str.equals("yes")) return Boolean.TRUE;
		if(str.equals("false")||str.equals("off")||str.equals("no")) return Boolean.FALSE;
		return null;
	}
}
