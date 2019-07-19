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

package me.fromgate.reactions.event;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.activators.ItemHoldActivator;
import me.fromgate.reactions.activators.ItemWearActivator;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.activators.PlayerDeathActivator;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.util.BlockUtil;
import me.fromgate.reactions.util.Cfg;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.playerselector.PlayerSelectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// Is it good idea to call custom events and listening for them?
public class EventManager {

	public static boolean raiseFactionEvent(Player p, String oldFaction, String newFaction) {
		FactionChangeEvent e = new FactionChangeEvent(p, oldFaction, newFaction);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}


	public static boolean raiseFactionCreateEvent(String factionName, Player player) {
		FactionCreateEvent e = new FactionCreateEvent(factionName, player);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}

	public static boolean raiseFactionDisbandEvent(String factionName, Player player) {
		FactionDisbandEvent e = new FactionDisbandEvent(factionName, player);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}


	public static boolean raiseFactionRelationEvent(String faction, String factionOther, String oldRelation, String newRelation) {
		FactionRelationEvent e = new FactionRelationEvent(faction, factionOther, oldRelation, newRelation);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}

	public static boolean raiseMobClickEvent(Player player, LivingEntity mob) {
		if (mob == null) return false;
		MobClickEvent e = new MobClickEvent(player, mob);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}

	public static boolean raiseMobKillEvent(Player player, LivingEntity mob) {
		if (mob == null) return false;
		MobKillEvent e = new MobKillEvent(player, mob);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}


	public static boolean raiseJoinEvent(Player player, boolean joinfirst) {
		JoinEvent e = new JoinEvent(player, joinfirst);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}

	public static boolean raiseDoorEvent(PlayerInteractEvent event) {
		if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
			return false;
		if (!BlockUtil.isOpenable(event.getClickedBlock()) || event.getHand() != EquipmentSlot.HAND)
			return false;
		DoorEvent e = new DoorEvent(event.getPlayer(), BlockUtil.getDoorBottomBlock(event.getClickedBlock()));
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}

	public static boolean raiseItemConsumeEvent(PlayerItemConsumeEvent event) {
		ItemConsumeEvent ce = new ItemConsumeEvent(event.getPlayer(), event.getItem(), event.getPlayer().getInventory().getItemInMainHand().equals(event.getItem()));
		Bukkit.getServer().getPluginManager().callEvent(ce);
		return ce.isCancelled();
	}

	public static boolean raiseItemClickEvent(PlayerInteractEntityEvent event) {
		ItemClickEvent ice;
		boolean mainHand = event.getHand() == EquipmentSlot.HAND;
		ItemStack item = mainHand?event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();
		if (item == null || item.getType() == Material.AIR)
			return false;
		ice = new ItemClickEvent(event.getPlayer(), item, mainHand);
		Bukkit.getServer().getPluginManager().callEvent(ice);
		return true;
	}

	public static boolean raiseItemClickEvent(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return false;
		ItemClickEvent ice;
		boolean mainHand = event.getHand() == EquipmentSlot.HAND;
		ItemStack item = mainHand?event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();
		if (item == null || item.getType() == Material.AIR)
			return false;
		ice = new ItemClickEvent(event.getPlayer(), item, mainHand);
		Bukkit.getServer().getPluginManager().callEvent(ice);
		return true;
	}


	public static boolean raiseLeverEvent(PlayerInteractEvent event) {
		if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
			return false;
		if (event.getHand() != EquipmentSlot.HAND)
			return false;
		if (event.getClickedBlock().getType() != Material.LEVER) return false;
		LeverEvent e = new LeverEvent(event.getPlayer(), event.getClickedBlock());
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}


	// PVP Kill Event
	public static void raisePvpKillEvent(PlayerDeathEvent event) {
		Player deadplayer = event.getEntity();
		Player killer = Util.getKiller(deadplayer.getLastDamageCause());
		if (killer == null) return;
		PvpKillEvent pe = new PvpKillEvent(killer, deadplayer);
		Bukkit.getServer().getPluginManager().callEvent(pe);
	}

	// PVP Death Event
	public static void raisePvpDeathEvent(PlayerDeathEvent event) {
		Player deadplayer = event.getEntity();
		LivingEntity killer = Util.getAnyKiller(deadplayer.getLastDamageCause());
		PlayerDeathActivator.DeathCause ds = (killer == null) ? PlayerDeathActivator.DeathCause.OTHER : (killer instanceof Player) ? PlayerDeathActivator.DeathCause.PVP : PlayerDeathActivator.DeathCause.PVE;
		DeathEvent pe = new DeathEvent(killer, deadplayer, ds);
		Bukkit.getServer().getPluginManager().callEvent(pe);
	}

	// Button Event
	public static boolean raiseButtonEvent(PlayerInteractEvent event) {
		if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK))) {
			return false;
		}
		if (!Tag.BUTTONS.isTagged(event.getClickedBlock().getType())) {
			return false;
		}
		if (event.getHand() != EquipmentSlot.HAND) {
			return false;
		}
		Switch button = (Switch) event.getClickedBlock().getBlockData();
		if (button.isPowered()) return false;
		ButtonEvent be = new ButtonEvent(event.getPlayer(), event.getClickedBlock().getLocation());
		Bukkit.getServer().getPluginManager().callEvent(be);
		return be.isCancelled();
	}

	public static boolean raiseSignEvent(Player player, String[] lines, Location loc, boolean leftClick) {
		for (Activator act : Activators.getActivators(ActivatorType.SIGN)) {
			SignActivator sign = (SignActivator) act;
			if (sign.checkMask(lines)) {
				SignEvent se = new SignEvent(player, lines, loc, leftClick);
				Bukkit.getServer().getPluginManager().callEvent(se);
				return true;
			}
		}
		return false;
	}

	public static boolean raiseCommandEvent(Player p, String command, boolean cancelled) {
		if (command.isEmpty()) return false;
		String[] args = command.split(" ");
		CommandEvent ce = new CommandEvent(p, command, args, cancelled);
		Bukkit.getServer().getPluginManager().callEvent(ce);
		return ce.isCancelled();
	}

	public static boolean raiseExecEvent(CommandSender sender, String param) {
		if (param.isEmpty()) return false;
		return raiseExecEvent(sender, new Param(param, "player"));
	}

	public static boolean raiseExecEvent(CommandSender sender, Param param) {
		return raiseExecEvent(sender, param, null);
	}

	public static boolean raiseExecEvent(CommandSender sender, Param param, final Param tempVars) {
		if (param.isEmpty()) return false;
		final Player senderPlayer = (sender instanceof Player) ? (Player) sender : null;
		final String id = param.getParam("activator", param.getParam("exec"));
		if (id.isEmpty()) return false;
		Activator act = Activators.get(id);
		if (act == null) {
			Msg.logOnce("wrongact_" + id, "Failed to run exec activator " + id + ". Activator not found.");
			return false;
		}
		if (act.getType() != ActivatorType.EXEC) {
			Msg.logOnce("wrongactype_" + id, "Failed to run exec activator " + id + ". Wrong activator type.");
			return false;
		}

		int repeat = Math.min(param.getParam("repeat", 1), 1);

		long delay = Util.timeToTicks(Util.parseTime(param.getParam("delay", "1t")));

		final Set<Player> target = new HashSet<>();

		if (param.isParamsExists("player")) {
			target.addAll(PlayerSelectors.getPlayerList(new Param(param.getParam("player"), "player")));
		}
		target.addAll(PlayerSelectors.getPlayerList(param));   // Оставляем для совместимости со старым вариантом

		if (target.isEmpty() && !param.hasAnyParam(PlayerSelectors.getAllKeys())) target.add(senderPlayer);

		for (int i = 0; i < repeat; i++) {
			Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
				for (Player player : target) {
					if (Activators.isStopped(player, id, true)) continue;
					ExecEvent ce = new ExecEvent(senderPlayer, player, id, tempVars);
					Bukkit.getServer().getPluginManager().callEvent(ce);
				}
			}, delay * repeat);
		}
		return true;
	}

	// Plate Event
	public static boolean raisePlateEvent(PlayerInteractEvent event) {
		if (event.getAction() != Action.PHYSICAL) return false;
		if (!(event.getClickedBlock().getType().name().endsWith("_PRESSURE_PLATE"))) {
			return false;
		}
		final Player p = event.getPlayer();
		final Location l = event.getClickedBlock().getLocation();
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
			PlateEvent pe = new PlateEvent(p, l);
			Bukkit.getServer().getPluginManager().callEvent(pe);
		}, 1);
		return false;
	}

	public static void raiseCuboidEvent(final Player player) {
		Bukkit.getServer().getPluginManager().callEvent(new CuboidEvent(player));
	}

	public static void raiseAllRegionEvents(final Player player, final Location to, final Location from) {
		if (!RaWorldGuard.isConnected()) return;
		Bukkit.getScheduler().runTaskLaterAsynchronously(ReActions.instance, () -> {

			final List<String> regionsTo = RaWorldGuard.getRegions(to);
			final List<String> regionsFrom = RaWorldGuard.getRegions(from);

			Bukkit.getScheduler().runTask(ReActions.instance, () -> {
				raiseRegionEvent(player, regionsTo);
				raiseRgEnterEvent(player, regionsTo, regionsFrom);
				raiseRgLeaveEvent(player, regionsTo, regionsFrom);
			});
		}, 1);
	}

	private static void raiseRgEnterEvent(Player player, List<String> regionTo, List<String> regionFrom) {
		if (regionTo.isEmpty()) return;
		for (String rg : regionTo)
			if (!regionFrom.contains(rg)) {
				RegionEnterEvent wge = new RegionEnterEvent(player, rg);
				Bukkit.getServer().getPluginManager().callEvent(wge);
			}
	}

	private static void raiseRgLeaveEvent(Player player, List<String> regionTo, List<String> regionFrom) {
		if (regionFrom.isEmpty()) return;
		for (String rg : regionFrom)
			if (!regionTo.contains(rg)) {
				RegionLeaveEvent wge = new RegionLeaveEvent(player, rg);
				Bukkit.getServer().getPluginManager().callEvent(wge);
			}
	}

	private static void raiseRegionEvent(Player player, List<String> to) {
		if (to.isEmpty()) return;
		for (String region : to) {
			setFutureRegionCheck(player.getName(), region, false);
		}
	}

	@SuppressWarnings("deprecation")
	private static void setFutureRegionCheck(final String playerName, final String region, boolean repeat) {
		Player player = Bukkit.getPlayerExact(playerName);
		if (player == null) return;
		if (!player.isOnline()) return;
		if (player.isDead()) return;
		if (!RaWorldGuard.isPlayerInRegion(player, region)) return;
		String rg = "rg-" + region;
		if (!isTimeToRaiseEvent(player, rg, Cfg.worldguardRecheck, repeat)) return;

		RegionEvent wge = new RegionEvent(player, region);
		Bukkit.getServer().getPluginManager().callEvent(wge);

		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> setFutureRegionCheck(playerName, region, true), 20 * Cfg.worldguardRecheck);
	}


	private static void setFutureItemWearCheck(final UUID playerId, final String itemStr, boolean repeat) {
		Player player = Bukkit.getPlayer(playerId);
		if (player == null) return;
		if (!player.isOnline()) return;
		String rg = "iw-" + itemStr;
		if (!isTimeToRaiseEvent(player, rg, Cfg.itemWearRecheck, repeat)) return;
		ItemWearEvent iwe = new ItemWearEvent(player);
		if (!iwe.isItemWeared(itemStr)) return;
		Bukkit.getServer().getPluginManager().callEvent(iwe);
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> setFutureItemWearCheck(playerId, itemStr, true), 20 * Cfg.itemWearRecheck);
	}


	public static void raiseItemWearEvent(Player player) {
		final UUID playerId = player.getUniqueId();
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
			for (Activator iw : Activators.getActivators(ActivatorType.ITEM_WEAR))
				setFutureItemWearCheck(playerId, ((ItemWearActivator) iw).getItemStr(), false);
		}, 1);
	}

	public static void raiseItemHoldEvent(Player player) {
		final UUID playerId = player.getUniqueId();
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
			for (Activator ih : Activators.getActivators(ActivatorType.ITEM_HOLD))
				setFutureItemHoldCheck(playerId, ((ItemHoldActivator)ih).getItemStr(), false);
		}, 1);
	}


	private static boolean setFutureItemHoldCheck(final UUID playerId, final String itemStr, boolean repeat) {
		Player player = Bukkit.getPlayer(playerId);
		if (player == null || !player.isOnline() || player.isDead()) return false;
		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		if (itemInHand == null || itemInHand.getType() == Material.AIR) return false;
		String rg = "ih-" + itemStr;
		if (!isTimeToRaiseEvent(player, rg, Cfg.itemHoldRecheck, repeat)) return false;
		if (!ItemUtil.compareItemStr(itemInHand, itemStr)) return false;
		ItemHoldEvent ihe = new ItemHoldEvent(player);
		Bukkit.getServer().getPluginManager().callEvent(ihe);

		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> setFutureItemHoldCheck(playerId, itemStr, true), 20 * Cfg.itemHoldRecheck);
		return true;
	}

	private static boolean isTimeToRaiseEvent(Player p, String id, int seconds, boolean repeat) {
		long curTime = System.currentTimeMillis();
		long prevTime = p.hasMetadata("reactions-rchk-" + id) ? p.getMetadata("reactions-rchk-" + id).get(0).asLong() : 0;
		boolean needUpdate = repeat || ((curTime - prevTime) >= (1000 * seconds));
		if (needUpdate) p.setMetadata("reactions-rchk-" + id, new FixedMetadataValue(ReActions.getPlugin(), curTime));
		return needUpdate;
	}

	public static boolean raiseMessageEvent(CommandSender sender, MessageActivator.Source source, String message) {
		Player player = (sender instanceof Player) ? (Player) sender : null;
		for (Activator act : Activators.getActivators(ActivatorType.MESSAGE)) {
			MessageActivator a = (MessageActivator) act;
			if (a.filterMessage(source, message)) {
				MessageEvent me = new MessageEvent(player, a, message);
				Bukkit.getServer().getPluginManager().callEvent(me);
				return me.isCancelled();
			}
		}
		return false;
	}

	public static void raiseVariableEvent(String var, String playerName, String newValue, String prevValue) {
		if (newValue.equalsIgnoreCase(prevValue)) return;
		@SuppressWarnings("deprecation")
		Player player = Bukkit.getPlayerExact(playerName);
		if (!playerName.isEmpty() && player == null) return;
		VariableEvent ve = new VariableEvent(player, var, newValue, prevValue);
		Bukkit.getServer().getPluginManager().callEvent(ve);
	}

	public static boolean raiseMobDamageEvent(EntityDamageEvent event, Player damager) {
		if (damager == null) return false;
		if (!(event.getEntity() instanceof LivingEntity)) return false;
		double damage = event.getDamage();
		MobDamageEvent mde = new MobDamageEvent((LivingEntity) event.getEntity(), damager, damage, event.getCause());
		Bukkit.getServer().getPluginManager().callEvent(mde);
		event.setDamage(mde.getDamage());
		return mde.isCancelled();
	}

	public static void raiseQuitEvent(PlayerQuitEvent event) {
		QuitEvent qu = new QuitEvent(event.getPlayer(), event.getQuitMessage());
		Bukkit.getServer().getPluginManager().callEvent(qu);
		event.setQuitMessage(qu.getQuitMessage() == null || qu.getQuitMessage().isEmpty() ? null : ChatColor.translateAlternateColorCodes('&', qu.getQuitMessage()));
	}

	public static boolean raiseBlockClickEvent(PlayerInteractEvent event) {
		boolean leftClick;
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) leftClick = false;
		else if (event.getAction() == Action.LEFT_CLICK_BLOCK) leftClick = true;
		else return false;
		if (event.getHand() != EquipmentSlot.HAND) {
			return false;
		}
		BlockClickEvent e = new BlockClickEvent(event.getPlayer(), event.getClickedBlock(), leftClick);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}

	public static boolean raiseInventoryClickEvent(org.bukkit.event.inventory.InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		ItemStack oldItem = event.getCurrentItem();
		InventoryClickEvent e = new InventoryClickEvent(p, event.getAction(), event.getClick(), event.getInventory(), event.getSlotType(), event.getCurrentItem(), event.getHotbarButton(), event.getView(), event.getSlot());
		Bukkit.getServer().getPluginManager().callEvent(e);
		ItemStack newItemStack = e.getItemStack();
		if (newItemStack != null) {
			if (newItemStack.getType() != Material.AIR && newItemStack.getAmount() <= 1 && oldItem != null) {
				newItemStack.setAmount(oldItem.getAmount());
			}
			if (!(event instanceof InventoryCreativeEvent)) event.setCurrentItem(newItemStack);
		}
		return e.isCancelled();
	}

	public static boolean raiseDropEvent(PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		Player player = event.getPlayer();
		double pickupDelay = item.getPickupDelay();
		DropEvent e = new DropEvent(player, event.getItemDrop(), pickupDelay);
		Bukkit.getServer().getPluginManager().callEvent(e);
		e.setPickupDelay(e.getPickupDelay());
		ItemStack newItemStack = e.getItemStack();
		if (newItemStack != null && newItemStack.getType() == Material.AIR) {
			item.remove();
		} else if (newItemStack != null) {
			ItemStack itemStack = item.getItemStack();
			if (newItemStack.getAmount() > 1) {
				for (int i = 0; i < newItemStack.getAmount(); i++) {
					item.setItemStack(new ItemStack(newItemStack.clone()));
				}
			} else {
				itemStack.setType(newItemStack.getType());
				if (newItemStack.getData() != null) itemStack.setData(newItemStack.getData());
				if (newItemStack.getItemMeta() != null) itemStack.setItemMeta(newItemStack.getItemMeta());
				ItemUtil.setDurability(itemStack, ItemUtil.getDurability(newItemStack));
			}
		}
		return e.isCancelled();
	}

	public static boolean raiseFlightEvent(PlayerToggleFlightEvent event) {
		FlightEvent e = new FlightEvent(event.getPlayer(), event.isFlying());
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}

	public static boolean raiseEntityClickEvent(PlayerInteractEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return false;
		EntityClickEvent e = new EntityClickEvent(event.getPlayer(), event.getRightClicked());
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}

	public static boolean raiseBlockBreakEvent(org.bukkit.event.block.BlockBreakEvent event) {
		boolean isDropItems = event.isDropItems();
		BlockBreakEvent e = new BlockBreakEvent(event.getPlayer(), event.getBlock(), isDropItems);
		Bukkit.getServer().getPluginManager().callEvent(e);
		event.setDropItems(e.isDropItems());
		return e.isCancelled();
	}

	public static boolean raiseSneakEvent(PlayerToggleSneakEvent event) {
		SneakEvent e = new SneakEvent(event.getPlayer(), event.isSneaking());
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}

	public static boolean raisePlayerDamageByMobEvent(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) return false;
		DamageByMobEvent dm = new DamageByMobEvent((Player) event.getEntity(), event.getDamager(), event.getDamage(), event.getCause());
		Bukkit.getServer().getPluginManager().callEvent(dm);
		event.setDamage(dm.getDamage());
		return dm.isCancelled();
	}

	public static boolean raisePlayerDamageByBlockEvent(EntityDamageByBlockEvent event, Block blockDamager) {
		if (!(event.getEntity() instanceof LivingEntity))
			return false;
		double damage = event.getDamage();
		DamageByBlockEvent db = new DamageByBlockEvent((Player) event.getEntity(), blockDamager, damage, event.getCause());
		Bukkit.getServer().getPluginManager().callEvent(db);
		event.setDamage(db.getDamage());
		return db.isCancelled();
	}

	public static boolean raisePlayerDamageEvent(EntityDamageEvent event, String source) {
		if (!(event.getEntity() instanceof LivingEntity))
			return false;
		double damage = event.getDamage();
		DamageEvent de = new DamageEvent((Player) event.getEntity(), damage, event.getCause(), source);
		Bukkit.getServer().getPluginManager().callEvent(de);
		event.setDamage(de.getDamage());
		return de.isCancelled();
	}

	public static boolean raiseEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof FallingBlock) {
			FallingBlock fb = (FallingBlock) event.getEntity();
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				for (Entity e : p.getNearbyEntities(0.5D, 1.0D, 0.5D)) {
					if ((e instanceof FallingBlock) && fb == e) {
						Bukkit.getPluginManager().callEvent(new EntityDamageByEntityEvent(e, p, EntityDamageEvent.DamageCause.FALLING_BLOCK, 0));
					}
				}
			}
		}
		return false;
	}

	public static boolean raiseProjectileHitEvent(ProjectileHitEvent event) {
		Entity hitEntity = event.getHitEntity();
		if (!(hitEntity instanceof Player))
			return false;
		Player player = (Player) hitEntity;
		Entity entity = event.getEntity();
		// TODO PlayerProjectileHit activator
		return false;
	}

	public static boolean raisePlayerPickupItemEvent(EntityPickupItemEvent event) {
		Item item = event.getItem();
		if(event.getEntityType() != EntityType.PLAYER)
			return false;
		Player player = (Player) event.getEntity();
		int pickupDelay = item.getPickupDelay();
		PickupItemEvent e = new PickupItemEvent(player, event.getItem(), pickupDelay);
		Bukkit.getServer().getPluginManager().callEvent(e);
		item.setPickupDelay(e.getPickupDelay());
		ItemStack newItemStack = e.getItemStack();
		if (newItemStack != null && newItemStack.getType() == Material.AIR) {
			e.setCancelled(true);
			item.remove();
		} else if (newItemStack != null) {
			ItemStack itemStack = item.getItemStack();
			if (newItemStack.getAmount() > 1) {
				e.setCancelled(true);
				item.remove();
				ItemUtil.giveItemOrDrop(player, newItemStack);
			} else {
				itemStack.setType(newItemStack.getType());
				if (newItemStack.getData() != null) itemStack.setData(newItemStack.getData());
				if (newItemStack.getItemMeta() != null) itemStack.setItemMeta(newItemStack.getItemMeta());
				ItemUtil.setDurability(itemStack, ItemUtil.getDurability(newItemStack));
			}
		}
		return e.isCancelled();
	}

	public static boolean raisePlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		GameModeEvent e = new GameModeEvent(event.getPlayer(), event.getNewGameMode());
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}

	public static boolean raisePlayerGodChangeEvent(Player player, boolean god) {
		GodEvent e = new GodEvent(player, god);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}

	public static boolean raiseItemHeldEvent(Player player, int newSlot, int previousSlot) {
		ItemHeldEvent e = new ItemHeldEvent(player, newSlot, previousSlot);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}
}
