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

package me.fromgate.reactions.storage;

import me.fromgate.reactions.Cfg;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.activators.ItemHoldActivator;
import me.fromgate.reactions.activators.ItemWearActivator;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.time.TimeUtil;
import me.fromgate.reactions.util.BlockUtil;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.playerselector.SelectorsManager;
import me.fromgate.reactions.util.simpledata.DeathCause;
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
import org.bukkit.event.inventory.InventoryClickEvent;
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
import java.util.Set;
import java.util.UUID;

public class StorageManager {

	public static boolean raiseFactionActivator(Player p, String oldFaction, String newFaction) {
		FactionChangeStorage e = new FactionChangeStorage(p, oldFaction, newFaction);
		ActivatorsManager.activate(e);
		return true;
	}


	public static boolean raiseFactionCreateActivator(String factionName, Player player) {
		FactionCreateStorage e = new FactionCreateStorage(factionName, player);
		ActivatorsManager.activate(e);
		return true;
	}

	public static boolean raiseFactionDisbandActivator(String factionName, Player player) {
		FactionDisbandStorage e = new FactionDisbandStorage(factionName, player);
		ActivatorsManager.activate(e);
		return true;
	}


	public static boolean raiseFactionRelationActivator(String faction, String factionOther, String oldRelation, String newRelation) {
		FactionRelationStorage e = new FactionRelationStorage(faction, factionOther, oldRelation, newRelation);
		ActivatorsManager.activate(e);
		return true;
	}

	public static boolean raiseMobClickActivator(Player player, LivingEntity mob) {
		if (mob == null) return false;
		MobClickStorage e = new MobClickStorage(player, mob);
		ActivatorsManager.activate(e);
		return true;
	}

	public static boolean raiseMobKillActivator(Player player, LivingEntity mob) {
		if (mob == null) return false;
		MobKillStorage e = new MobKillStorage(player, mob);
		ActivatorsManager.activate(e);
		return true;
	}


	public static boolean raiseJoinActivator(Player player, boolean joinfirst) {
		JoinStorage e = new JoinStorage(player, joinfirst);
		ActivatorsManager.activate(e);
		return true;
	}

	public static boolean raiseDoorActivator(PlayerInteractEvent event) {
		if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
			return false;
		if (!BlockUtil.isOpenable(event.getClickedBlock()) || event.getHand() != EquipmentSlot.HAND)
			return false;
		DoorStorage e = new DoorStorage(event.getPlayer(), BlockUtil.getDoorBottomBlock(event.getClickedBlock()));
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}

	public static boolean raiseItemConsumeActivator(PlayerItemConsumeEvent event) {
		ItemConsumeStorage ce = new ItemConsumeStorage(event.getPlayer(), event.getItem(), event.getPlayer().getInventory().getItemInMainHand().isSimilar(event.getItem()));
		ActivatorsManager.activate(ce);
		return ce.isCancelled();
	}

	public static boolean raiseItemClickActivator(PlayerInteractEntityEvent event) {
		ItemClickStorage ice;
		boolean mainHand = event.getHand() == EquipmentSlot.HAND;
		ItemStack item = mainHand?event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();
		if (item == null || item.getType() == Material.AIR)
			return false;
		ice = new ItemClickStorage(event.getPlayer(), item, mainHand);
		ActivatorsManager.activate(ice);
		return true;
	}

	public static boolean raiseItemClickActivator(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return false;
		ItemClickStorage ice;
		boolean mainHand = event.getHand() == EquipmentSlot.HAND;
		ItemStack item = mainHand?event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();
		if (item == null || item.getType() == Material.AIR)
			return false;
		ice = new ItemClickStorage(event.getPlayer(), item, mainHand);
		ActivatorsManager.activate(ice);
		return true;
	}


	public static boolean raiseLeverActivator(PlayerInteractEvent event) {
		if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
			return false;
		if (event.getHand() != EquipmentSlot.HAND)
			return false;
		if (event.getClickedBlock().getType() != Material.LEVER) return false;
		LeverStorage e = new LeverStorage(event.getPlayer(), event.getClickedBlock());
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}


	// PVP Kill Event
	public static void raisePvpKillActivator(PlayerDeathEvent event) {
		Player deadplayer = event.getEntity();
		Player killer = Util.getKiller(deadplayer.getLastDamageCause());
		if (killer == null) return;
		PvpKillStorage pe = new PvpKillStorage(killer, deadplayer);
		ActivatorsManager.activate(pe);
	}

	// PVP Death Event
	public static void raisePvpDeathActivator(PlayerDeathEvent event) {
		Player deadplayer = event.getEntity();
		LivingEntity killer = Util.getAnyKiller(deadplayer.getLastDamageCause());
		DeathCause ds = (killer == null) ? DeathCause.OTHER : (killer instanceof Player) ? DeathCause.PVP : DeathCause.PVE;
		DeathStorage pe = new DeathStorage(killer, deadplayer, ds);
		ActivatorsManager.activate(pe);
	}

	// Button Event
	public static boolean raiseButtonActivator(PlayerInteractEvent event) {
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
		ButtonStorage be = new ButtonStorage(event.getPlayer(), event.getClickedBlock().getLocation());
		ActivatorsManager.activate(be);
		return be.isCancelled();
	}

	public static boolean raiseSignActivator(Player player, String[] lines, Location loc, boolean leftClick) {
		for (Activator act : ActivatorsManager.getActivators(ActivatorType.SIGN)) {
			SignActivator sign = (SignActivator) act;
			if (sign.checkMask(lines)) {
				SignStorage se = new SignStorage(player, lines, loc, leftClick);
				ActivatorsManager.activate(se);
				return true;
			}
		}
		return false;
	}

	public static boolean raiseCommandActivator(Player p, String command, boolean cancelled) {
		if (command.isEmpty()) return false;
		String[] args = command.split(" ");
		CommandStorage ce = new CommandStorage(p, command, args, cancelled);
		ActivatorsManager.activate(ce);
		return ce.isCancelled();
	}

	public static boolean raiseExecActivator(CommandSender sender, String param) {
		if (param.isEmpty()) return false;
		return raiseExecActivator(sender, new Param(param, "player"));
	}

	public static boolean raiseExecActivator(CommandSender sender, Param param) {
		return raiseExecActivator(sender, param, null);
	}

	public static boolean raiseExecActivator(CommandSender sender, Param param, final Param tempVars) {
		if (param.isEmpty()) return false;
		final Player senderPlayer = (sender instanceof Player) ? (Player) sender : null;
		final String id = param.getParam("activator", param.getParam("exec"));
		if (id.isEmpty()) return false;
		Activator act = ActivatorsManager.get(id);
		if (act == null) {
			Msg.logOnce("wrongact_" + id, "Failed to run exec activator " + id + ". Activator not found.");
			return false;
		}
		if (act.getType() != ActivatorType.EXEC) {
			Msg.logOnce("wrongactype_" + id, "Failed to run exec activator " + id + ". Wrong activator type.");
			return false;
		}

		int repeat = Math.min(param.getParam("repeat", 1), 1);

		long delay = TimeUtil.timeToTicks(TimeUtil.parseTime(param.getParam("delay", "1t")));

		final Set<Player> target = new HashSet<>();

		if (param.isParamsExists("player")) {
			target.addAll(SelectorsManager.getPlayerList(new Param(param.getParam("player"), "player")));
		}
		target.addAll(SelectorsManager.getPlayerList(param));   // Оставляем для совместимости со старым вариантом

		if (target.isEmpty() && !param.hasAnyParam(SelectorsManager.getAllKeys())) target.add(senderPlayer);

		for (int i = 0; i < repeat; i++) {
			Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
				for (Player player : target) {
					if (ActivatorsManager.isStopped(player, id, true)) continue;
					ExecStorage ce = new ExecStorage(senderPlayer, player, id, tempVars);
					ActivatorsManager.activate(ce);
				}
			}, delay * repeat);
		}
		return true;
	}

	// Plate Event
	public static boolean raisePlateActivator(PlayerInteractEvent event) {
		if (event.getAction() != Action.PHYSICAL) return false;
		if (!(event.getClickedBlock().getType().name().endsWith("_PRESSURE_PLATE"))) {
			return false;
		}
		final Player p = event.getPlayer();
		final Location l = event.getClickedBlock().getLocation();
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
			PlateStorage pe = new PlateStorage(p, l);
			ActivatorsManager.activate(pe);
		}, 1);
		return false;
	}

	public static void raiseCuboidActivator(final Player player) {
		ActivatorsManager.activate(new CuboidStorage(player));
	}

	public static void raiseAllRegionActivators(final Player player, final Location to, final Location from) {
		if (!RaWorldGuard.isConnected()) return;
		Bukkit.getScheduler().runTaskLaterAsynchronously(ReActions.getPlugin(), () -> {

			final Set<String> regionsTo = RaWorldGuard.getRegions(to);
			final Set<String> regionsFrom = RaWorldGuard.getRegions(from);

			Bukkit.getScheduler().runTask(ReActions.getPlugin(), () -> {
				raiseRegionActivator(player, regionsTo);
				raiseRgEnterActivator(player, regionsTo, regionsFrom);
				raiseRgLeaveActivator(player, regionsTo, regionsFrom);
			});
		}, 1);
	}

	private static void raiseRgEnterActivator(Player player, Set<String> regionTo, Set<String> regionFrom) {
		if (regionTo.isEmpty()) return;
		for (String rg : regionTo)
			if (!regionFrom.contains(rg)) {
				RegionEnterStorage wge = new RegionEnterStorage(player, rg);
				ActivatorsManager.activate(wge);
			}
	}

	private static void raiseRgLeaveActivator(Player player, Set<String> regionTo, Set<String> regionFrom) {
		if (regionFrom.isEmpty()) return;
		for (String rg : regionFrom)
			if (!regionTo.contains(rg)) {
				RegionLeaveStorage wge = new RegionLeaveStorage(player, rg);
				ActivatorsManager.activate(wge);
			}
	}

	private static void raiseRegionActivator(Player player, Set<String> to) {
		if (to.isEmpty()) return;
		for (String region : to) {
			setFutureRegionCheck(player.getName(), region, false);
		}
	}

	private static void setFutureRegionCheck(final String playerName, final String region, boolean repeat) {
		Player player = Util.getPlayerExact(playerName);
		if (player == null) return;
		if (!player.isOnline()) return;
		if (player.isDead()) return;
		if (!RaWorldGuard.isPlayerInRegion(player, region)) return;
		String rg = "rg-" + region;
		if (!isTimeToRaiseEvent(player, rg, Cfg.worldguardRecheck, repeat)) return;

		RegionStorage wge = new RegionStorage(player, region);
		ActivatorsManager.activate(wge);

		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> setFutureRegionCheck(playerName, region, true), 20 * Cfg.worldguardRecheck);
	}


	private static void setFutureItemWearCheck(final UUID playerId, final String itemStr, boolean repeat) {
		Player player = Bukkit.getPlayer(playerId);
		if (player == null) return;
		if (!player.isOnline()) return;
		String rg = "iw-" + itemStr;
		if (!isTimeToRaiseEvent(player, rg, Cfg.itemWearRecheck, repeat)) return;
		ItemWearStorage iwe = new ItemWearStorage(player);
		if (!iwe.isItemWeared(itemStr)) return;
		ActivatorsManager.activate(iwe);
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> setFutureItemWearCheck(playerId, itemStr, true), 20 * Cfg.itemWearRecheck);
	}


	public static void raiseItemWearActivator(Player player) {
		final UUID playerId = player.getUniqueId();
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
			for (Activator iw : ActivatorsManager.getActivators(ActivatorType.ITEM_WEAR))
				setFutureItemWearCheck(playerId, ((ItemWearActivator) iw).getItemStr(), false);
		}, 1);
	}

	public static void raiseItemHoldActivator(Player player) {
		final UUID playerId = player.getUniqueId();
		Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), () -> {
			for (Activator ih : ActivatorsManager.getActivators(ActivatorType.ITEM_HOLD))
				setFutureItemHoldCheck(playerId, ((ItemHoldActivator)ih).getItemStr(), false);
		}, 1);
	}

	// TODO: Second hand
	private static boolean setFutureItemHoldCheck(final UUID playerId, final String itemStr, boolean repeat) {
		Player player = Bukkit.getPlayer(playerId);
		if (player == null || !player.isOnline() || player.isDead()) return false;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!ItemUtil.isExist(item)) return false;
		String rg = "ih-" + itemStr;
		if (!isTimeToRaiseEvent(player, rg, Cfg.itemHoldRecheck, repeat)) return false;
		if (!ItemUtil.compareItemStr(item, itemStr)) return false;
		ItemHoldStorage ihe = new ItemHoldStorage(player, item, true);
		ActivatorsManager.activate(ihe);

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

	public static boolean raiseMessageActivator(CommandSender sender, MessageActivator.Source source, String message) {
		Player player = (sender instanceof Player) ? (Player) sender : null;
		for (Activator act : ActivatorsManager.getActivators(ActivatorType.MESSAGE)) {
			MessageActivator a = (MessageActivator) act;
			if (a.filterMessage(source, message)) {
				MessageStorage me = new MessageStorage(player, a, message);
				ActivatorsManager.activate(me);
				return me.isCancelled();
			}
		}
		return false;
	}

	public static void raiseVariableActivator(String var, String playerName, String newValue, String prevValue) {
		if (newValue.equalsIgnoreCase(prevValue)) return;
		Player player = Util.getPlayerExact(playerName);
		if (!playerName.isEmpty() && player == null) return;
		VariableStorage ve = new VariableStorage(player, var, newValue, prevValue);
		ActivatorsManager.activate(ve);
	}

	public static boolean raiseMobDamageActivator(EntityDamageEvent event, Player damager) {
		if (damager == null) return false;
		if (!(event.getEntity() instanceof LivingEntity)) return false;
		double damage = event.getDamage();
		MobDamageStorage mde = new MobDamageStorage((LivingEntity) event.getEntity(), damager, damage, event.getCause());
		ActivatorsManager.activate(mde);
		event.setDamage(mde.getDamage());
		return mde.isCancelled();
	}

	public static void raiseQuitActivator(PlayerQuitEvent event) {
		QuitStorage qu = new QuitStorage(event.getPlayer(), event.getQuitMessage());
		ActivatorsManager.activate(qu);
		event.setQuitMessage(qu.getQuitMessage() == null || qu.getQuitMessage().isEmpty() ? null : ChatColor.translateAlternateColorCodes('&', qu.getQuitMessage()));
	}

	public static boolean raiseBlockClickActivator(PlayerInteractEvent event) {
		boolean leftClick;
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) leftClick = false;
		else if (event.getAction() == Action.LEFT_CLICK_BLOCK) leftClick = true;
		else return false;
		if (event.getHand() != EquipmentSlot.HAND) {
			return false;
		}
		BlockClickStorage e = new BlockClickStorage(event.getPlayer(), event.getClickedBlock(), leftClick);
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}

	public static boolean raiseInventoryClickActivator(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		ItemStack oldItem = event.getCurrentItem();
		InventoryClickStorage e = new InventoryClickStorage(p, event.getAction(), event.getClick(), event.getInventory(), event.getSlotType(), event.getCurrentItem(), event.getHotbarButton(), event.getView(), event.getSlot());
		ActivatorsManager.activate(e);
		ItemStack newItemStack = e.getItem();
		if (newItemStack != null) {
			if (newItemStack.getType() != Material.AIR && newItemStack.getAmount() <= 1 && oldItem != null) {
				newItemStack.setAmount(oldItem.getAmount());
			}
			if (!(event instanceof InventoryCreativeEvent)) event.setCurrentItem(newItemStack);
		}
		return e.isCancelled();
	}

	public static boolean raiseDropActivator(PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		Player player = event.getPlayer();
		int pickupDelay = item.getPickupDelay();
		DropStorage e = new DropStorage(player, event.getItemDrop(), pickupDelay);
		ActivatorsManager.activate(e);
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

	public static boolean raiseFlightActivator(PlayerToggleFlightEvent event) {
		FlightStorage e = new FlightStorage(event.getPlayer(), event.isFlying());
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}

	public static boolean raiseEntityClickActivator(PlayerInteractEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return false;
		EntityClickStorage e = new EntityClickStorage(event.getPlayer(), event.getRightClicked());
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}

	public static boolean raiseBlockBreakActivator(org.bukkit.event.block.BlockBreakEvent event) {
		boolean isDropItems = event.isDropItems();
		BlockBreakStorage e = new BlockBreakStorage(event.getPlayer(), event.getBlock(), isDropItems);
		ActivatorsManager.activate(e);
		event.setDropItems(e.isDropItems());
		return e.isCancelled();
	}

	public static boolean raiseSneakActivator(PlayerToggleSneakEvent event) {
		SneakStorage e = new SneakStorage(event.getPlayer(), event.isSneaking());
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}

	public static boolean raiseDamageByMobActivator(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) return false;
		DamageByMobStorage dm = new DamageByMobStorage((Player) event.getEntity(), event.getDamager(), event.getDamage(), event.getCause());
		ActivatorsManager.activate(dm);
		event.setDamage(dm.getDamage());
		return dm.isCancelled();
	}

	public static boolean raiseDamageByBlockActivator(EntityDamageByBlockEvent event, Block blockDamager) {
		if (!(event.getEntity() instanceof LivingEntity))
			return false;
		double damage = event.getDamage();
		DamageByBlockStorage db = new DamageByBlockStorage((Player) event.getEntity(), blockDamager, damage, event.getCause());
		ActivatorsManager.activate(db);
		event.setDamage(db.getDamage());
		return db.isCancelled();
	}

	public static boolean raiseDamageActivator(EntityDamageEvent event, String source) {
		if (!(event.getEntity() instanceof LivingEntity))
			return false;
		double damage = event.getDamage();
		DamageStorage de = new DamageStorage((Player) event.getEntity(), damage, event.getCause(), source);
		ActivatorsManager.activate(de);
		event.setDamage(de.getDamage());
		return de.isCancelled();
	}

	public static boolean raiseEntityChangeBlockActivator(EntityChangeBlockEvent event) {
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

	public static boolean raiseProjectileHitActivator(ProjectileHitEvent event) {
		if(!(event.getEntity().getShooter() instanceof Player)) return false;
		ProjectileHitStorage ph = new ProjectileHitStorage((Player)event.getEntity().getShooter(),
															event.getEntityType(),
															event.getHitBlock(), event.getHitBlockFace(),
															event.getHitEntity());
		ActivatorsManager.activate(ph);
		return ph.isCancelled();
	}

	public static boolean raisePickupItemActivator(EntityPickupItemEvent event) {
		Item item = event.getItem();
		if(event.getEntityType() != EntityType.PLAYER)
			return false;
		Player player = (Player) event.getEntity();
		int pickupDelay = item.getPickupDelay();
		PickupItemStorage e = new PickupItemStorage(player, event.getItem(), pickupDelay);
		ActivatorsManager.activate(e);
		item.setPickupDelay(e.getPickupDelay());
		ItemStack newItemStack = e.getItem();
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

	public static boolean raiseGamemodeActivator(PlayerGameModeChangeEvent event) {
		GameModeStorage e = new GameModeStorage(event.getPlayer(), event.getNewGameMode());
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}

	public static boolean raiseGodActivator(Player player, boolean god) {
		GodStorage e = new GodStorage(player, god);
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}

	public static boolean raiseItemHeldActivator(Player player, int newSlot, int previousSlot) {
		ItemHeldStorage e = new ItemHeldStorage(player, newSlot, previousSlot);
		ActivatorsManager.activate(e);
		return e.isCancelled();
	}
}
