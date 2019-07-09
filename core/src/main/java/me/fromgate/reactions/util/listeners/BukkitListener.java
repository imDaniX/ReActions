package me.fromgate.reactions.util.listeners;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.externals.RaEconomics;
import me.fromgate.reactions.externals.RaVault;
import me.fromgate.reactions.util.PlayerRespawner;
import me.fromgate.reactions.util.RaDebug;
import me.fromgate.reactions.util.Teleporter;
import me.fromgate.reactions.util.TemporaryOp;
import me.fromgate.reactions.util.UpdateChecker;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.mob.EntityUtil;
import me.fromgate.reactions.util.mob.MobSpawn;
import me.fromgate.reactions.util.waiter.ActionsWaiter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class BukkitListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return;
		if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;
		EventManager.raiseMobClickEvent(event.getPlayer(), (LivingEntity) event.getRightClicked());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChatCommand(AsyncPlayerChatEvent event) {
		Bukkit.getScheduler().runTask(ReActions.getPlugin(), () -> {
			if (EventManager.raiseMessageEvent(event.getPlayer(), MessageActivator.Source.CHAT_INPUT, event.getMessage())) {
				event.setCancelled(true);
			}
		});
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onServerCommandEvent(ServerCommandEvent event) {
		EventManager.raiseMessageEvent(Bukkit.getConsoleSender(), MessageActivator.Source.CONSOLE_INPUT, event.getCommand());
		if (EventManager.raiseCommandEvent(null, event.getCommand(), event.isCancelled())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		for (Activator activator : Activators.getActivators(ActivatorType.SIGN)) {
			SignActivator signAct = (SignActivator) activator;
			if (!signAct.checkMask(event.getLines())) continue;
			if (event.getPlayer().hasPermission("reactions.sign." + signAct.getName().toLowerCase())) return;
			Msg.MSG_SIGNFORBIDDEN.print(event.getPlayer(), '4', 'c', signAct.getName());
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
		EventManager.raiseItemHoldEvent(event.getPlayer());
		EventManager.raiseItemWearEvent(event.getPlayer());
		if (EventManager.raiseItemHeldEvent(event.getPlayer(), event.getNewSlot(), event.getPreviousSlot()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryInteractEvent(InventoryInteractEvent event) {
		EventManager.raiseItemHoldEvent((Player) event.getWhoClicked());
		EventManager.raiseItemWearEvent((Player) event.getWhoClicked());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		EventManager.raiseItemHoldEvent((Player) event.getPlayer());
		EventManager.raiseItemWearEvent((Player) event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPickupItemEvent(EntityPickupItemEvent event) {
		if(event.getEntityType() != EntityType.PLAYER) return;
		Player player = (Player) event.getEntity();
		EventManager.raiseItemHoldEvent(player);
		EventManager.raiseItemWearEvent(player);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		PlayerRespawner.addPlayerRespawn(event);
		EventManager.raisePvpKillEvent(event);
		EventManager.raisePvpDeathEvent(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		event.setCancelled(EventManager.raiseItemConsumeEvent(event));
	}


	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerClickMob(PlayerInteractEntityEvent event) {
		EventManager.raiseItemClickEvent(event);
		if (!(event.getRightClicked() instanceof LivingEntity)) return;
		if (event.getHand() != EquipmentSlot.HAND) return;
		EventManager.raiseMobClickEvent(event.getPlayer(), (LivingEntity) event.getRightClicked());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		PlayerRespawner.raisePlayerRespawnEvent(event.getPlayer());
		EventManager.raiseAllRegionEvents(event.getPlayer(), event.getRespawnLocation(), event.getPlayer().getLocation());
	}


	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDropLoot(EntityDeathEvent event) {
		Player killer = Util.getKiller(event.getEntity().getLastDamageCause());

		List<ItemStack> stacks = MobSpawn.getMobDrop(event.getEntity());
		if (stacks != null && !stacks.isEmpty()) {
			event.getDrops().clear();
			event.getDrops().addAll(stacks);
		}

		if (event.getEntity().hasMetadata("ReActions-xp")) {
			int xp = Util.getMinMaxRandom(event.getEntity().getMetadata("ReActions-xp").get(0).asString());
			event.setDroppedExp(xp);
		}

		if (event.getEntity().hasMetadata("ReActions-money")) {
			if (!RaVault.isEconomyConnected()) return;
			if (killer != null) {
				int money = Util.getMinMaxRandom(event.getEntity().getMetadata("ReActions-money").get(0).asString());
				RaEconomics.creditAccount(killer.getName(), "", Double.toString(money), "", "");
				Msg.MSG_MOBBOUNTY.print(killer, 'e', '6', RaEconomics.format(money, "", ""), event.getEntity().getType().name());
			}
		}

		if (event.getEntity().hasMetadata("ReActions-deatheffect")) {
			MobSpawn.playMobEffect(event.getEntity().getLocation(), event.getEntity().getMetadata("ReActions-deatheffect").get(0).asString());
		}

		if (event.getEntity().hasMetadata("ReActions-activator") && (killer != null)) {
			String exec = event.getEntity().getMetadata("ReActions-activator").get(0).asString();
			EventManager.raiseExecEvent(killer, exec + " player:" + killer.getName());
		} else EventManager.raiseMobKillEvent(killer, event.getEntity());

	}


	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMobGrowl(EntityDamageEvent event) {
		if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) && (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
		LivingEntity damager = Util.getDamagerEntity(evdmg);
		if (damager == null) return;
		if (damager.getType() == EntityType.PLAYER) return;
		if (!damager.hasMetadata("ReActions-growl")) return;
		String growl = damager.getMetadata("ReActions-growl").get(0).asString();
		if (growl == null) return;
		if (growl.isEmpty()) return;
		Util.soundPlay(damager.getLocation(), growl);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onMobDamageByPlayer(EntityDamageEvent event) {
		if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) && (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) && (event.getCause() != EntityDamageEvent.DamageCause.MAGIC))
			return;
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
		LivingEntity damager = Util.getDamagerEntity(evdmg);
		if (damager == null) return;
		if (damager.getType() != EntityType.PLAYER) return;
		if (EventManager.raiseMobDamageEvent(event, (Player) damager)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDamageByMob(EntityDamageEvent event) {
		if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) && (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) && (event.getCause() != EntityDamageEvent.DamageCause.MAGIC))
			return;
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
		LivingEntity damager = Util.getDamagerEntity(evdmg);
		if (damager == null) return;
		if (damager.getType() == EntityType.PLAYER) return;
		if (!damager.hasMetadata("ReActions-dmg")) return;
		double dmg = damager.getMetadata("ReActions-dmg").get(0).asDouble();
		if (dmg < 0) return;
		event.setDamage(event.getDamage() * dmg);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCheckGodEvent(EntityDamageEvent event) {
		GodMode.cancelGodEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDamage(EntityDamageEvent event) {
		String source;
		if (event.getEntity().getType() != EntityType.PLAYER) return;

		if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM && Math.round(event.getDamage()) == 0) {
			event.setCancelled(true);
			return;
		}
		if ((event instanceof EntityDamageByEntityEvent)) {
			source = "ENTITY";
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
			if (EventManager.raisePlayerDamageByMobEvent(evdmg))
				event.setCancelled(true);
		} else if ((event instanceof EntityDamageByBlockEvent)) {
			source = "BLOCK";
			EntityDamageByBlockEvent evdmg = (EntityDamageByBlockEvent) event;
			Block blockDamager = evdmg.getDamager();
			if (EventManager.raisePlayerDamageByBlockEvent(evdmg, blockDamager)) event.setCancelled(true);
		} else {
			source = "OTHER";
		}

		if (EventManager.raisePlayerDamageEvent(event, source)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent event) {
		// LivingEntity damager = (LivingEntity) event.getPotion().getShooter();
		// TODO PotionSplashbyMob and PotionSplashbyPlayer activators
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if (EventManager.raiseEntityChangeBlockEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		EventManager.raiseProjectileHitEvent(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMobCry(EntityDamageEvent event) {
		if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) && (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)) return;
		if (event.getEntityType() == EntityType.PLAYER) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		LivingEntity le = (LivingEntity) event.getEntity();
		if (!le.hasMetadata("ReActions-cry")) return;
		String cry = le.getMetadata("ReActions-cry").get(0).asString();
		if (cry == null) return;
		if (cry.isEmpty()) return;
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
		if (evdmg.getDamager() instanceof Projectile) {
			Projectile prj = (Projectile) evdmg.getDamager();
			LivingEntity shooter = EntityUtil.getEntityFromProjectile(prj.getShooter());
			if (shooter == null) return;
			if (!(shooter instanceof Player)) return;
		} else if (evdmg.getDamager().getType() != EntityType.PLAYER) return;
		Util.soundPlay(le.getLocation(), cry);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPvPDamage(EntityDamageEvent event) {
		if (event.getEntityType() != EntityType.PLAYER) return;
		Player target = (Player) event.getEntity();
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
		Player damager;
		if (evdmg.getDamager().getType() == EntityType.PLAYER) {
			damager = (Player) evdmg.getDamager();
		} else if (evdmg.getDamager() instanceof Projectile) {
			Projectile prj = (Projectile) evdmg.getDamager();
			LivingEntity shooter = EntityUtil.getEntityFromProjectile(prj.getShooter());
			if (shooter == null) return;
			if (!(shooter instanceof Player)) return;
			damager = (Player) shooter;
		} else return;
		if (damager == null) return;
		Long time = System.currentTimeMillis();
		damager.setMetadata("reactions-pvp-time", new FixedMetadataValue(ReActions.getPlugin(), time));
		target.setMetadata("reactions-pvp-time", new FixedMetadataValue(ReActions.getPlugin(), time));
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (EventManager.raiseCommandEvent(event.getPlayer(), event.getMessage().replaceFirst("/", ""), event.isCancelled())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		TemporaryOp.removeTempOp(player);
		ActionsWaiter.refresh();
		RaDebug.offPlayerDebug(player);
		UpdateChecker.updateMsg(player);
		MoveListener.initLocation(player);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignClick(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if ((event.getClickedBlock().getType() != Material.WALL_SIGN) &&
				(event.getClickedBlock().getType() != Material.SIGN)) return;
		Sign sign = (Sign) event.getClickedBlock().getState();
		if (sign == null) return;
		EventManager.raiseSignEvent(event.getPlayer(), sign.getLines(), event.getClickedBlock().getLocation(), event.getAction() == Action.LEFT_CLICK_BLOCK);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		EventManager.raiseItemClickEvent(event);
		EventManager.raiseItemWearEvent(event.getPlayer());
		if (EventManager.raiseBlockClickEvent(event)) event.setCancelled(true);
		if (EventManager.raiseButtonEvent(event)) event.setCancelled(true);
		EventManager.raisePlateEvent(event);
		if (EventManager.raiseLeverEvent(event)) event.setCancelled(true);
		if (EventManager.raiseDoorEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Teleporter.startTeleport(event);
		EventManager.raiseAllRegionEvents(event.getPlayer(), event.getTo(), event.getFrom());
		Teleporter.stopTeleport(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInventoryClick(InventoryClickEvent event) {
		if (EventManager.raiseInventoryClickEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInventoryClick(InventoryCreativeEvent event) {
		if (EventManager.raiseInventoryClickEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDrop(PlayerDropItemEvent event) {
		if (EventManager.raiseDropEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFlight(PlayerToggleFlightEvent event) {
		if (EventManager.raiseFlightEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityClick(PlayerInteractEntityEvent event) {
		if (EventManager.raiseEntityClickEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (EventManager.raiseBlockBreakEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSneak(PlayerToggleSneakEvent event) {
		if (EventManager.raiseSneakEvent(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPickupItem(EntityPickupItemEvent event) {
		if (EventManager.raisePlayerPickupItemEvent(event)) event.setCancelled(true);
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoinActivators(PlayerJoinEvent event) {
		EventManager.raiseJoinEvent(event.getPlayer(), !event.getPlayer().hasPlayedBefore());
		EventManager.raiseAllRegionEvents(event.getPlayer(), event.getPlayer().getLocation(), null);
		EventManager.raiseItemHoldEvent(event.getPlayer());
		EventManager.raiseItemWearEvent(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuitActivators(PlayerQuitEvent event) {
		TemporaryOp.removeTempOp(event.getPlayer());
		EventManager.raiseQuitEvent(event);
		MoveListener.removeLocation(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		if (EventManager.raisePlayerGameModeChangeEvent(event)) event.setCancelled(true);
	}
}
