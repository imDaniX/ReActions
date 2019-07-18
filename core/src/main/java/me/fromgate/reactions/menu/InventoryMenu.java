package me.fromgate.reactions.menu;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InventoryMenu implements Listener {

	private static Map<Integer, List<String>> activeMenus = new HashMap<>();
	private static Map<String, VirtualInventory> menu = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private static Param tempvars;

	public static void init() {
		load();
		save();
		Bukkit.getPluginManager().registerEvents(new InventoryMenu(), ReActions.getPlugin());
	}

	public static void save() {
		File f = new File(ReActions.instance.getDataFolder() + File.separator + "menu.yml");
		if (f.exists()) f.delete();
		YamlConfiguration cfg = new YamlConfiguration();
		for (String key : menu.keySet()) {
			menu.get(key).save(cfg, key);
		}
		try {
			cfg.save(f);
		} catch (Exception e) {
			Msg.logMessage("Failed to save menu configuration file");
		}
	}

	public static void load() {
		menu.clear();
		File f = new File(ReActions.instance.getDataFolder() + File.separator + "menu.yml");
		if (!f.exists()) return;
		YamlConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(f);
			for (String key : cfg.getKeys(false)) {
				VirtualInventory vi = new VirtualInventory(cfg, key);
				menu.put(key, vi);
			}
		} catch (Exception e) {
			Msg.logMessage("Failed to load menu configuration file");
		}
	}

	public static boolean add(String id, int size, String title) {
		if (menu.keySet().contains(id)) return false;
		menu.put(id, new VirtualInventory(size, title));
		save();
		return true;
	}

	public static boolean set(String id, Param params) {
		if (!menu.keySet().contains(id)) return false;
		VirtualInventory vi = menu.get(id);
		String title = params.getParam("title", vi.title);
		int size = params.getParam("size", vi.size);
		size = (size % 9 == 0) ? size : ((size / 9) + 1) * 9;

		List<String> activators = vi.execs;
		if (activators.size() < size)
			for (int i = activators.size(); i < size; i++) activators.add("");
		List<String> slots = vi.slots;
		if (slots.size() < size)
			for (int i = slots.size(); i < size; i++) slots.add("");
		for (int i = 1; i <= size; i++) {
			if (params.isParamsExists("activator" + i))
				activators.set(i - 1, params.getParam("activator" + i, ""));
			if (params.isParamsExists("item" + i))
				slots.set(i - 1, params.getParam("item" + i, ""));
		}
		vi.title = title;
		vi.size = size;
		vi.slots = slots;
		vi.execs = activators;
		menu.put(id, vi);
		save();
		return true;
	}

	public static boolean remove(String id) {
		if (!menu.containsKey(id)) return false;
		menu.remove(id);
		return true;
	}


	private static List<String> getActivators(Param param) {
		if (param.isParamsExists("menu")) {
			String id = param.getParam("menu", "");
			if (menu.containsKey(id)) return menu.get(id).getActivators();
		} else {
			int size = param.getParam("size", 9);
			if (size > 0) {
				List<String> activators = new ArrayList<>();
				for (int i = 1; i <= size; i++)
					activators.add(param.getParam("exec" + i, ""));
				return activators;
			}
		}
		return new ArrayList<>();
	}

	public static Inventory getInventory(Param param) {
		Inventory inv = null;
		if (param.isParamsExists("menu")) {
			String id = param.getParam("menu", "");
			if (menu.containsKey(id)) inv = menu.get(id).getInventory();
		} else {
			String title = param.getParam("title", "ReActions Menu");
			int size = param.getParam("size", 9);
			if (size <= 0) return null;
			inv = Bukkit.createInventory(null, size, title);
			for (int i = 1; i <= size; i++) {
				String slotStr = "slot" + i;
				if (!param.isParamsExists(slotStr)) continue;
				ItemStack slotItem = ItemUtil.parseItemStack(param.getParam(slotStr, ""));
				if (slotItem == null) continue;
				inv.setItem(i - 1, slotItem);
			}
		}
		return inv;
	}

	public static boolean createAndOpenInventory(Player player, Param params, final Param tempVars) {
		tempvars = tempVars;
		Inventory inv = getInventory(params);
		if (inv == null) return false;
		activeMenus.put(getInventoryCode(player, inv), getActivators(params));
		openInventory(player, inv);
		return true;
	}

	private static void openInventory(final Player player, final Inventory inv) {
		Bukkit.getScheduler().runTaskLater(ReActions.instance, () -> {
			if (player.isOnline()) player.openInventory(inv);
			else activeMenus.remove(getInventoryCode(player, inv));
		}, 1);
	}

	private static boolean isMenu(Inventory inventory) {
		return activeMenus.containsKey(getInventoryCode(inventory));
	}

	private static void removeInventory(Inventory inv) {
		int code = getInventoryCode(inv);
		activeMenus.remove(code);
	}


	private static List<String> getActivators(Inventory inventory) {
		if (isMenu(inventory)) return activeMenus.get(getInventoryCode(inventory));
		return new ArrayList<>();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!InventoryMenu.isMenu(event.getInventory())) return;
		if (event.isShiftClick()) {
			event.setCancelled(true);
			return;
		}
		Player player = (Player) event.getWhoClicked();
		int clickedSlot = event.getRawSlot();
		if (clickedSlot < 0 || clickedSlot >= event.getInventory().getSize()) return;
		List<String> activators = getActivators(event.getInventory());
		if (activators.size() > clickedSlot) {
			String activator = activators.get(clickedSlot);
			if (!activator.isEmpty()) {
				EventManager.raiseExecEvent(player, new Param(activator, "activator"), tempvars);
			}
		}
		event.setCancelled(true);
		// TODO: Do not close menu option?
		InventoryMenu.removeInventory(event.getInventory());
		player.closeInventory();
	}

	@EventHandler
	public void onInventoryMove(InventoryDragEvent event) {
		if (!InventoryMenu.isMenu(event.getInventory())) return;
		event.setCancelled(true);
	}

	public static List<String> getEmptyList(int size) {
		List<String> l = new ArrayList<>();
		for (int i = 0; i < size; i++) l.add("");
		return l;
	}

	public static boolean exists(String id) {
		return menu.containsKey(id);
	}

	public static void printMenu(CommandSender sender, String id) {
		if (menu.containsKey(id)) {
			VirtualInventory vi = menu.get(id);
			Msg.printMSG(sender, "msg_menuinfotitle", 'e', '6', id, vi.size, vi.title);
			for (int i = 0; i < vi.size; i++) {
				String exec = vi.execs.get(i);
				String slot = vi.slots.get(i);
				if (exec.isEmpty() && slot.isEmpty()) continue;
				slot = itemToString(slot);
				Msg.printMSG(sender, "msg_menuinfoslot", i + 1, exec.isEmpty() ? "N/A" : exec, slot.isEmpty() ? "AIR" : slot);
			}
		} else Msg.printMSG(sender, "msg_menuidfail", id);
	}

	public static void printMenuList(CommandSender sender, int pageNum, String mask) {
		int linesPerPage = (sender instanceof Player) ? 15 : 10000;
		List<String> menuList = new ArrayList<>();
		for (String id : menu.keySet()) {
			if (mask.isEmpty() || id.toLowerCase().contains(mask.toLowerCase())) {
				menuList.add(id + " : " + menu.get(id).title);
			}
		}
		Msg.printPage(sender, menuList, Msg.MSG_MENULIST, pageNum, linesPerPage);
	}

	private static String itemToString(String itemStr) {
		if (itemStr.isEmpty()) return "AIR";
		ItemStack item = ItemUtil.parseItemStack(itemStr);
		if (item == null || item.getType() == Material.AIR) return "AIR";
		String returnStr = item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "";
		String itemTypeData = item.getType().name() + (ItemUtil.getDurability(item) == 0 ? "" : ":" + ItemUtil.getDurability(item)) + (item.getAmount() == 1 ? "" : "*" + item.getAmount());
		return ChatColor.stripColor(returnStr.isEmpty() ? itemTypeData : returnStr + "[" + itemTypeData + "]");
	}

	@SuppressWarnings("unused")
	public static int getInventoryCode(InventoryClickEvent event) {
		if (event.getViewers().size() != 1) return -1;
		HumanEntity human = event.getViewers().get(0);
		return getInventoryCode((Player) human, event.getInventory());
	}

	private static int getInventoryCode(Inventory inv) {
		if (inv.getViewers().size() != 1) return -1;
		HumanEntity human = inv.getViewers().get(0);
		return getInventoryCode((Player) human, inv);
	}

	@SuppressWarnings("deprecation")
	private static int getInventoryCode(Player player, Inventory inv) {
		if (player == null || inv == null) return -1;
		StringBuilder sb = new StringBuilder();
		sb.append(player.getName());
		sb.append(inv.getName());
		for (ItemStack i : inv.getContents()) {
			String iStr = "emptyslot";
			if (i != null && i.getType() != Material.AIR) {
				if (i.hasItemMeta()) {
					ItemMeta im = i.getItemMeta();
					if (im.hasDisplayName()) sb.append(im.getDisplayName());
					if (im.hasLore())
						for (String str : im.getLore())
							sb.append(str);
				}
				sb.append(i.getType().name());
				sb.append(":");
				sb.append(ItemUtil.getDurability(i));
				sb.append(":");
				sb.append(i.getAmount());
			}
			sb.append(iStr);
		}
		return sb.toString().hashCode();
	}
}
