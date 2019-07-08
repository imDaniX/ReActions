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
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.mob.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	private final static Pattern INT_GZ = Pattern.compile("[1-9]+[0-9]*");
	private final static Pattern INT_LGZ = Pattern.compile("-?[0-9]+[0-9]*");
	private final static Pattern INT = Pattern.compile("[0-9]+[0-9]*");
	private final static Pattern FLOAT = Pattern.compile("([0-9]+\\.[0-9]+)|([0-9]+)");
	private final static Pattern NUM = Pattern.compile("[0-9]*");

	private final static Pattern TIME_HH_MM = Pattern.compile("^[0-5][0-9]:[0-5][0-9]$");
	private final static Pattern TIME_HH_MM_SS = Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
	private final static Pattern TIME_X_MSDHMST = Pattern.compile("\\d+(ms|d|h|m|s|t)");
	private final static Pattern TIME_X_MS = Pattern.compile("^\\d+ms$");
	private final static Pattern TIME_X_D = Pattern.compile("^\\d+d$");
	private final static Pattern TIME_X_H = Pattern.compile("^\\d+h$");
	private final static Pattern TIME_X_M = Pattern.compile("^\\d+m$");
	private final static Pattern TIME_X_S = Pattern.compile("^\\d+s$");
	private final static Pattern TIME_X_T = Pattern.compile("^\\d+t$");

	private static ThreadLocalRandom random = ThreadLocalRandom.current();

	public static int getMinMaxRandom(String minmaxstr) {
		int min = 0;
		int max;
		String strmin = minmaxstr;
		String strmax = minmaxstr;

		if (minmaxstr.contains("-")) {
			strmin = minmaxstr.substring(0, minmaxstr.indexOf("-"));
			strmax = minmaxstr.substring(minmaxstr.indexOf("-") + 1);
		}
		if (INT_GZ.matcher(strmin).matches()) min = Integer.parseInt(strmin);
		max = min;
		if (INT_GZ.matcher(strmax).matches()) max = Integer.parseInt(strmax);
		if (max > min) return min + tryChance(1 + max - min);
		else return min;
	}

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

	public static void soundPlay(Location loc, String param) {
		if (param.isEmpty()) return;
		/*Map<String,String> params = new HashMap<String,String>();
		params.put("param", param); */
		Param params = new Param(param, "param");
		soundPlay(loc, params);
	}


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
		locs.put("%selection%", Selector.getSelectedLocation(p));
		locs.put("%select%", Selector.getSelectedLocation(p));
		locs.put("%sel%", Selector.getSelectedLocation(p));
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


	public static LivingEntity getDamagerEntity(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
			if (evdmg.getCause() == DamageCause.PROJECTILE) {
				Projectile prj = (Projectile) evdmg.getDamager();
				return EntityUtil.getEntityFromProjectile(prj.getShooter());
			} else if (evdmg.getCause() == DamageCause.MAGIC) {
				Entity entityDamager = evdmg.getDamager();
				LivingEntity shooterEntity = null;
				if (entityDamager instanceof ThrownPotion)
					shooterEntity = EntityUtil.getEntityFromProjectile(((ThrownPotion) entityDamager).getShooter());
				return shooterEntity;
			} else if (evdmg.getDamager() instanceof LivingEntity)
				return (LivingEntity) evdmg.getDamager();
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

	public static boolean emptySting(String str) {
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
		String[] selectedLines = Arrays.copyOfRange(lines.toArray(new String[lines.size()]), from, to);
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

	/*
	 * Функция проверяет входит есть ли item (блок) с заданным id и data в списке,
	 * представленным в виде строки вида id1:data1,id2:data2,MATERIAL_NAME:data
	 * При этом если data может быть опущена
	 */
	public static boolean isItemInList(Material type, int data, String str) {
		String[] ln = str.split(",");
		if (ln.length > 0)
			for (String itemInList : ln) {
				if (compareItemIdDataStr(type, data, itemInList)) return true;
			}

		return false;
	}

	public static boolean compareItemIdDataStr(Material type, int data, String itemStr) {
		ItemStack item = ItemUtil.parseItemStack(itemStr);
		if (item == null) return false;
		if (item.getType() != type) return false;
		if (data < 0) return true;
		return data == ItemUtil.getDurability(item);
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
			if (!INT_LGZ.matcher(s).matches()) return false;
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
		return INT_GZ.matcher(str).matches();
	}

	public static boolean isIntegerGZ(String... str) {
		if (str.length == 0) return false;
		for (String s : str)
			if (!INT_GZ.matcher(s).matches()) return false;
		return true;
	}

	public static long timeToTicks(Long time) {
		//1000 ms = 20 ticks
		return Math.max(1, (time / 50));
	}

	public static long parseTime(String time) {
		int dd = 0; // дни
		int hh = 0; // часы
		int mm = 0; // минуты
		int ss = 0; // секунды
		int tt = 0; // тики
		int ms = 0; // миллисекунды
		if (isInteger(time)) {
			ss = Integer.parseInt(time);
		} else if (TIME_HH_MM.matcher(time).matches()) {
			String[] ln = time.split(":");
			if (isInteger(ln[0])) mm = Integer.parseInt(ln[0]);
			if (isInteger(ln[1])) ss = Integer.parseInt(ln[1]);
		} else if (TIME_HH_MM_SS.matcher(time).matches()) {
			String[] ln = time.split(":");
			if (isInteger(ln[0])) hh = Integer.parseInt(ln[0]);
			if (isInteger(ln[1])) mm = Integer.parseInt(ln[1]);
			if (isInteger(ln[2])) ss = Integer.parseInt(ln[2]);
		} else {
			Matcher matcher = TIME_X_MSDHMST.matcher(time);
			while (matcher.find()) {
				String foundTime = matcher.group();
				if (TIME_X_MS.matcher(foundTime).matches())
					ms = Integer.parseInt(time.substring(0, time.length()-2));
				else if (TIME_X_D.matcher(foundTime).matches())
					dd = Integer.parseInt(time.substring(0, time.length()-1));
				else if (TIME_X_H.matcher(foundTime).matches())
					hh = Integer.parseInt(time.substring(0, time.length()-1));
				else if (TIME_X_M.matcher(foundTime).matches())
					mm = Integer.parseInt(time.substring(0, time.length()-1));
				else if (TIME_X_S.matcher(foundTime).matches())
					ss = Integer.parseInt(time.substring(0, time.length()-1));
				else if (TIME_X_T.matcher(foundTime).matches())
					tt = Integer.parseInt(time.substring(0, time.length()-1));
			}
		}
		return (dd * 86400000L) + (hh * 3600000L) + (mm * 60000L) + (ss * 1000L) + (tt * 50L) + ms;
	}

	public static int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE) return Integer.MIN_VALUE;
		if (l > Integer.MAX_VALUE) return Integer.MAX_VALUE;
		return (int) l;
	}

	public static UUID getUUID(OfflinePlayer player, String playerName, Boolean isOnlineMode) {
		if (!isOnlineMode) {
			return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8));
		}
		return player.getUniqueId();
	}

	public static UUID getUUID(OfflinePlayer player) {
		boolean isOnlineMode = Bukkit.getOnlineMode();
		return getUUID(player, player.getName(), isOnlineMode);
	}

	@SuppressWarnings("deprecation")
	public static UUID getUUID(String player) {
		Player p = Bukkit.getServer().getPlayer(player);
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

}
