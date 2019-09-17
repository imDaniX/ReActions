package me.fromgate.reactions.util.item;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ItemUtil {

	private final static Pattern BYTES_RGB = Pattern.compile("^[0-9]{1,3},[0-9]{1,3},[0-9]{1,3}$");

	private final static Pattern ITEM_D = Pattern.compile("item\\d+|ITEM\\d+");
	private final static Pattern SET_D = Pattern.compile("set\\d+|SET\\d+");

	public static void removeItemAmount(ItemStack item, int amount) {
		int itemAmount = item.getAmount();
		if(amount >= itemAmount)
			item.setType(Material.AIR);
		else
			item.setAmount(itemAmount-amount);
	}

	public static Enchantment getEnchantmentByName(String name) {
		if(!Util.isStringEmpty(name))
			try {
				return Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase()));
			} catch (IllegalArgumentException ignore) {}
		return null;
	}

	public static int getDurability(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if(meta instanceof Damageable)
			return ((Damageable)meta).getDamage();
		return 0;
	}

	public static void setDurability(ItemStack item, int durability) {
		ItemMeta meta = item.getItemMeta();
		if(meta instanceof Damageable) {
			Damageable dmg = (Damageable) meta;
			dmg.setDamage(durability);
			item.setItemMeta(meta);
		}
	}

	public static void giveItemOrDrop(Player player, ItemStack item) {
		for (ItemStack itemDrop : player.getInventory().addItem(item).values()) {
			player.getWorld().dropItemNaturally(player.getLocation(), itemDrop);
		}
	}

	@SuppressWarnings("unused")
	public static void giveItemOrDrop(Player player, String itemStr) {
		VirtualItem vi = VirtualItem.fromString(itemStr);
		if (vi == null) return;
		giveItemOrDrop(player, vi);
	}

	public static boolean removeItemInHand(Player player, String itemStr) {
		ItemStack inHand = player.getInventory().getItemInMainHand();
		if (!isExist(inHand)) return false;
		VirtualItem hand = VirtualItem.fromItemStack(inHand);
		VirtualItem vi = removeItemFromStack(hand, itemStr);
		if (vi == null) return false;
		player.getInventory().setItemInMainHand(vi.getType() == Material.AIR ? null : vi);
		return true;
	}

	public static boolean removeItemInOffHand(Player player, String itemStr) {
		ItemStack inHand = player.getInventory().getItemInOffHand();
		if (!isExist(inHand)) return false;
		VirtualItem hand = VirtualItem.fromItemStack(inHand);
		VirtualItem vi = removeItemFromStack(hand, itemStr);
		if (vi == null) return false;
		player.getInventory().setItemInOffHand(vi.getType() == Material.AIR ? null : vi);
		return true;
	}


	public static boolean removeItemInInventory(Inventory inventory, String itemStr) {
		Map<String, String> itemParams = Param.parseParams(itemStr, "");
		return removeItemInInventory(inventory, itemParams);
	}

	public static int countItemsInInventory(Inventory inventory, String itemStr) {
		Map<String, String> itemMap = Param.parseParams(itemStr, "");
		return countItemsInventory(inventory, itemMap);
	}

	private static boolean removeItemInInventory(Inventory inventory, Map<String, String> itemParams) {
		int amountToRemove = Integer.parseInt(itemParams.getOrDefault("amount", "1"));
		//int countItems =  countItemsInventory (inventory, itemParams);
		//if (amountToRemove>countItems) return false;
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) continue;
			VirtualItem vi = VirtualItem.fromItemStack(inventory.getItem(i));
			if (!vi.compare(itemParams, 1)) continue;
			if (vi.getAmount() <= amountToRemove) {
				amountToRemove -= vi.getAmount();
				inventory.setItem(i, null);
			} else {
				vi.setAmount(vi.getAmount() - amountToRemove);
				inventory.setItem(i, vi);
				amountToRemove = 0;
			}
			if (amountToRemove == 0) return true;
		}
		return false;
	}

	private static int countItemsInventory(Inventory inventory, Map<String, String> itemParams) {
		int count = 0;
		for (ItemStack slot : inventory) {
			if (slot == null || slot.getType() == Material.AIR) continue;
			VirtualItem vi = VirtualItem.fromItemStack(slot);
			if (!vi.compare(itemParams, 1)) continue;
			count += slot.getAmount();
		}
		return count;
	}

	/**
	 * @param stack   - source item
	 * @param itemStr - item description to remove
	 * @return - item stack contained left items (if all items removed - remove
	 */
	private static VirtualItem removeItemFromStack(VirtualItem stack, String itemStr) {
		if (!ItemUtil.compareItemStr(stack, itemStr)) return null;
		int amountToRemove = getAmount(itemStr);
		if (amountToRemove <= 0) return null;
		int leftAmount = stack.getAmount() - amountToRemove;
		if (leftAmount < 0) return null;
		VirtualItem result = VirtualItem.fromItemStack(stack);
		if (leftAmount == 0) result.setType(Material.AIR);
		else result.setAmount(leftAmount);
		return result;
	}

	public static int getAmount(String itemStr) {
		Map<String, String> itemMap = Param.parseParams(itemStr, "");
		String amountStr = itemMap.getOrDefault("amount", "1");
		if (Util.INT_NOTZERO_POSITIVE.matcher(amountStr).matches()) return Integer.parseInt(amountStr);
		return 1;
	}

	public static VirtualItem itemFromBlock(Block block) {
		if (block == null) return VirtualItem.fromString("AIR");
		return VirtualItem.fromItemStack(new ItemStack(block.getType(), 1));
	}

	public static ItemStack parseItemStack(String string) {
		return VirtualItem.fromString(string);
	}

	public static boolean compareItemStr(Block block, String itemStr) {
		if (block == null || block.getType() == Material.AIR) return false;
		ItemStack item = new ItemStack(block.getType(), 1);
		return compareItemStr(item, itemStr);
	}

	public static boolean compareItemStr(ItemStack item, String itemStr) {
		if (item == null || item.getType() == Material.AIR) return false;
		return VirtualItem.fromItemStack(item).compare(itemStr);
	}

	public static boolean compareItemStr(ItemStack item, String itemStr, boolean allowHand) {
		if (item != null && item.getType() != Material.AIR) return compareItemStr(item, itemStr);
		if (!allowHand) return false;
		return (itemStr.equalsIgnoreCase("HAND") || itemStr.equalsIgnoreCase("AIR"));
	}

	public static boolean removeItemInInventory(Player player, String itemStr) {
		return removeItemInInventory(player.getInventory(), itemStr);
	}

	public static ItemStack getRndItem(String str) {
		if (str.isEmpty()) return new ItemStack(Material.AIR);
		String[] ln = str.split(",");
		if (ln.length == 0) return new ItemStack(Material.AIR);

		ItemStack item = ItemUtil.parseItemStack(ln[Util.getRandomInt(ln.length)]);

		if (item == null) return new ItemStack(Material.AIR);
		item.setAmount(1);
		return item;
	}


	/*
	 *  <item>;<item>;<item>[%<chance>]/<item>;<item>;<item>[%<chance>]
	 *
	 */
	public static List<ItemStack> parseItemStacksOld(String items) {
		List<ItemStack> stacks = new ArrayList<>();
		String[] ln = items.split(";"); // ВОТ ЭТО ЛОМАЕТ К ЧЕРТЯМ НОВЫЙ ФОРМАТ!!!
		for (String item : ln) {
			VirtualItem vi = VirtualItem.fromString(item);
			if (vi != null) stacks.add(vi);
		}
		return stacks;
	}

	public static String itemToString(ItemStack item) {
		VirtualItem vi = VirtualItem.fromItemStack(item);
		return vi == null ? "" : vi.toString();
	}

	public static String toDisplayString(List<ItemStack> items) {
		StringBuilder sb = new StringBuilder();
		for (ItemStack i : items) {
			VirtualItem vi = VirtualItem.fromItemStack(i);
			if (sb.length() > 0) sb.append(", ");
			sb.append(vi.toDisplayString());
		}
		return sb.toString();
	}

	//item:{item1:{[...] chance:50} item2:{} item3:{}

	public static VirtualItem itemFromMap(Param params) {
		return VirtualItem.fromMap(params.getMap());

	}

	public static List<ItemStack> parseItemsSet(Param params) {
		List<ItemStack> items = new ArrayList<>();
		for (String key : params.keySet()) {
			if (ITEM_D.matcher(key).matches()) {
				String itemStr = params.getParam(key, "");
				VirtualItem vi = VirtualItem.fromString(itemStr);
				if (vi != null) items.add(vi);
			}
		}
		if (items.isEmpty()) {
			VirtualItem item = itemFromMap(params);
			if (item != null) items.add(item);
		}
		return items;
	}

	/**
	 * Get list of items from random set
	 * @param items Set of items, e.g set1:{item1:{}  item2:{} item3:{} chance:50}  set2:{item1:{}  item2:{} item3:{} chance:50}
	 * @return List of items
	 */
	public static List<ItemStack> parseRandomItemsStr(String items) {
		Param params = new Param(items);
		if (params.matchAnyParam(SET_D)) {
			Map<List<ItemStack>, Integer> sets = new HashMap<>();
			int maxChance = 0;
			int nochcount = 0;
			for (String key : params.keySet()) {
				if (!SET_D.matcher(key).matches()) continue;
				Param itemParams = new Param(params.getParam(key));
				List<ItemStack> itemList = parseItemsSet(itemParams);
				if (itemList == null || itemList.isEmpty()) continue;
				int chance = itemParams.getParam("chance", -1);
				if (chance > 0) maxChance += chance;
				else nochcount++;
				sets.put(itemList, chance);
			}
			int eqperc = (nochcount * 100) / sets.size();
			maxChance = maxChance + eqperc * nochcount;
			int rnd = Util.getRandomInt(maxChance);
			int curchance = 0;
			for (List<ItemStack> stack : sets.keySet()) {
				curchance = curchance + (sets.get(stack) < 0 ? eqperc : sets.get(stack));
				if (rnd <= curchance) return stack;
			}
		} else if (params.matchAnyParam("item\\d+|ITEM\\d+")) {
			return parseItemsSet(params);
		} else {
			VirtualItem vi = VirtualItem.fromString(items);
			if (vi != null) {
				List<ItemStack> iList = new ArrayList<>();
				iList.add(vi);
				return iList;
			}

		}
		return null;
	}


	//id:data*amount@enchant:level,color;id:data*amount%chance/id:data*amount@enchant:level,color;id:data*amount%chance
	@SuppressWarnings("unused")
	public static String parseRandomItemsStrOld(String items) {
		if (items.isEmpty()) return "";
		String[] loots = items.split("/");
		Map<String, Integer> drops = new HashMap<>();
		int maxchance = 0;
		int nochcount = 0;
		for (String loot : loots) {
			String[] ln = loot.split("%");
			if (ln.length > 0) {
				String stacks = ln[0];
				if (stacks.isEmpty()) continue;
				int chance = -1;
				if ((ln.length == 2) && (Util.INT_NOTZERO_POSITIVE.matcher(ln[1]).matches())) {
					chance = Integer.parseInt(ln[1]);
					maxchance += chance;
				} else nochcount++;
				drops.put(stacks, chance);
			}
		}
		if (drops.isEmpty()) return "";
		int eqperc = (nochcount * 100) / drops.size();
		maxchance = maxchance + eqperc * nochcount;
		int rnd = Util.getRandomInt(maxchance);
		int curchance = 0;
		for (String stack : drops.keySet()) {
			curchance = curchance + (drops.get(stack) < 0 ? eqperc : drops.get(stack));
			if (rnd <= curchance) return stack;
		}
		return "";
	}

	public static String toDisplayString(String itemStr) {
		VirtualItem vi = VirtualItem.fromString(itemStr);
		if (vi != null) return vi.toDisplayString();
		Map<String, String> itemMap = Param.parseParams(itemStr, "");
		String name = itemMap.containsKey("name") ? itemMap.get("name") : itemMap.getOrDefault("type", null);
		if (name == null) return itemStr;
		int amount = getAmount(itemStr);
		String data = itemMap.getOrDefault("data", "0");
		StringBuilder sb = new StringBuilder(name);
		if (!itemMap.containsKey("name") && !data.equals("0")) sb.append(":").append(data);
		if (amount > 1) sb.append("*").append(amount);
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', sb.toString()));
	}

	/*
	 * Функция проверяет входит есть ли item (блок) с заданным id и data в списке,
	 * представленным в виде строки вида id1:data1,id2:data2,MATERIAL_NAME:data
	 * При этом если data может быть опущена
	 */
	public static boolean isItemInList(Material type, int durability, String str) {
		String[] ln = str.split(",");
		if (ln.length > 0)
			for (String itemInList : ln) {
				if (compareItemIdDataStr(type, durability, itemInList)) return true;
			}
		return false;
	}

	public static boolean compareItemIdDataStr(Material type, int durability, String itemStr) {
		ItemStack item = parseItemStack(itemStr);
		if (item == null) return false;
		if (item.getType() != type) return false;
		if (durability < 0) return true;
		return durability == getDurability(item);
	}

	/**
	 * Get material from name
	 * @param name Name of material
	 * @return Material (may be legacy)
	 */
	public static Material getMaterial(String name) {
		if(Util.isStringEmpty(name)) return null;
		name = name.toUpperCase();
		Material material = Material.getMaterial(name, false);
		return material == null ? Material.getMaterial(name, true) : material;
	}

	/**
	 * Is item actually exist
	 * @param item Item to check
	 * @return Is item not null and not air
	 */
	public static boolean isExist(ItemStack item) {
		return item != null && item.getType() != Material.AIR;
	}

	public static String fireworksToString(FireworkEffect fe) {
		StringBuilder sb = new StringBuilder();
		sb.append("type:").append(fe.getType().name());
		sb.append(" flicker:").append(fe.hasFlicker());
		sb.append(" trail:").append(fe.hasTrail());
		if (!fe.getColors().isEmpty()) {
			sb.append(" colors:");
			for (int i = 0; i < fe.getColors().size(); i++) {
				Color c = fe.getColors().get(i);
				if (i > 0)
					sb.append(";");
				sb.append(colorToString(c, true));
			}
		}
		if (!fe.getFadeColors().isEmpty()) {
			sb.append(" fade-colors:");
			for (int i = 0; i < fe.getFadeColors().size(); i++) {
				Color c = fe.getColors().get(i);
				if (i > 0) sb.append(";");
				sb.append(colorToString(c, true));
			}
		}
		return sb.toString();
	}

	static List<Color> parseColors(String colorStr) {
		List<Color> colors = new ArrayList<>();
		String[] clrs = colorStr.split(";");
		for (String cStr : clrs) {
			Color c = parseColor(cStr.trim());
			if (c == null)
				continue;
			colors.add(c);
		}
		return colors;
	}

	/**
	 * Parse bukkit colors. Name and RGB values supported
	 *
	 * @param colorStr - Color name, or RGB values (Example: 10,15,20)
	 * @return - Color
	 */
	public static Color parseColor(String colorStr) {
		if (BYTES_RGB.matcher(colorStr).matches()) {
			String[] rgb = colorStr.split(",");
			int red = Integer.parseInt(rgb[0]);
			int green = Integer.parseInt(rgb[1]);
			int blue = Integer.parseInt(rgb[2]);
			return Color.fromRGB(red, green, blue);
		} else if (Util.BYTE.matcher(colorStr).matches()) {
			int num = Integer.parseInt(colorStr);
			if (num > 15)
				num = 15;
			@SuppressWarnings("deprecation")
			DyeColor c = DyeColor.getByDyeData((byte) num);
			return c == null ? null : c.getColor();
		} else {
			/*
			for (DyeColor dc : DyeColor.values())
				if (dc.name().equalsIgnoreCase(colorStr))
					return dc.getColor();
			 */
		}
		return null;
	}

	private static double getColorDistance(Color c1, Color c2) {
		double rmean = (c1.getRed() + c2.getRed()) / 2.0;
		double r = c1.getRed() - c2.getRed();
		double g = c1.getGreen() - c2.getGreen();
		int b = c1.getBlue() - c2.getBlue();
		double weightR = 2 + rmean / 256.0;
		double weightG = 4.0;
		double weightB = 2 + (255 - rmean) / 256.0;
		return weightR * r * r + weightG * g * g + weightB * b * b;
	}

	public static DyeColor getClosestColor(Color color) {
		int index = 0;
		double best = -1;
		for (int i = 0; i < DyeColor.values().length; i++) {
			double distance = getColorDistance(color,
					DyeColor.values()[i].getColor());
			if (distance < best || best == -1) {
				best = distance;
				index = i;
			}
		}
		return DyeColor.values()[index];
	}

	public static String colorToString(Color c, boolean useRGB) {
		for (DyeColor dc : DyeColor.values())
			if (dc.getColor().equals(c))
				return dc.name();
		if (!useRGB)
			getClosestColor(c).name();
		String sb = c.getRed() + "," +
				c.getGreen() + "," +
				c.getBlue();
		return sb;
	}

	public static Map<Enchantment, Integer> parseEnchantmentsString(String enchStr) {
		Map<Enchantment, Integer> ench = new HashMap<>();
		if (enchStr == null || enchStr.isEmpty()) return ench;
		String[] ln = enchStr.split(";");
		for (String e : ln) {
			String eType = e;
			int power = 0;
			if (eType.contains(":")) {
				String powerStr = eType.substring(eType.indexOf(":") + 1);
				eType = eType.substring(0, eType.indexOf(":"));
				power = Util.getMinMaxRandom(powerStr);
			}
			Enchantment enchantment = getEnchantmentByName(eType);
			if (enchantment == null)
				continue;
			ench.put(enchantment, power);
		}
		return ench;

	}

	/**
	 * Old format algorithm. Implemented for compatibility.
	 *
	 * @param itemStr - old item format
	 * @return - ItemStack
	 */
	protected static ItemStack parseOldItemStack(String itemStr) {
		if (Util.isStringEmpty(itemStr))
			return null;
		String iStr = itemStr;
		String enchant = "";
		String name = "";
		String loreStr = "";
		if (iStr.contains("$")) {
			name = iStr.substring(0, iStr.indexOf("$"));
			iStr = iStr.substring(name.length() + 1);
			if (name.contains("@")) {
				loreStr = name.substring(name.indexOf("@") + 1);
				name = name.substring(0, name.indexOf("@"));
			}

		}
		if (iStr.contains("@")) {
			enchant = iStr.substring(iStr.indexOf("@") + 1);
			iStr = iStr.substring(0, iStr.indexOf("@"));
		}
		Material id;
		int amount = 1;
		short data = 0;
		String[] si = iStr.split("\\*");
		if (si.length > 0) {
			if (si.length == 2)
				amount = Math.max(Util.getMinMaxRandom(si[1]), 1);
			String[] ti = si[0].split(":");
			if (ti.length > 0) {
				Material m = Material.getMaterial(ti[0].toUpperCase());
				if (m == null)
					return null;
				id = m;
				if ((ti.length == 2) && (Util.INT_POSITIVE.matcher(ti[1]).matches()))
					data = Short.parseShort(ti[1]);
				ItemStack item = new ItemStack(id, amount);
				setDurability(item, data);
				if (!enchant.isEmpty()) {

					String[] ln = enchant.split(",");
					for (String ec : ln) {
						if (ec.isEmpty())
							continue;

						Color clr = parseColor(ec);
						if (clr != null) {
							if (item.hasItemMeta()
									&& (item.getItemMeta() instanceof LeatherArmorMeta)) {
								LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
								meta.setColor(clr);
								item.setItemMeta(meta);
							}
						} else {
							String ench = ec;
							int level = 1;
							if (ec.contains(":")) {
								ench = ec.substring(0, ec.indexOf(":"));
								level = Math.max(1, Util.getMinMaxRandom(ec.substring(ench
										.length() + 1)));
							}
							Enchantment e = ItemUtil.getEnchantmentByName(ench);
							if (e == null)
								continue;
							item.addUnsafeEnchantment(e, level);
						}
					}
				}
				if (!name.isEmpty()) {
					ItemMeta im = item.getItemMeta();
					im.setDisplayName(ChatColor.translateAlternateColorCodes(
							'&', name.replace("_", " ")));
					item.setItemMeta(im);
				}
				if (!loreStr.isEmpty()) {
					ItemMeta im = item.getItemMeta();
					String[] ln = loreStr.split("@");
					List<String> lore = new ArrayList<>();
					for (String loreLine : ln)
						lore.add(loreLine.replace("_", " "));
					im.setLore(lore);
					item.setItemMeta(im);
				}
				return item;
			}
		}
		return null;
	}
}
