package me.fromgate.reactions.listeners;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.externals.RaEconomics;
import me.fromgate.reactions.externals.RaVault;
import me.fromgate.reactions.storage.StorageManager;
import me.fromgate.reactions.time.waiter.WaitingManager;
import me.fromgate.reactions.util.BlockUtil;
import me.fromgate.reactions.util.TemporaryOp;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.location.PlayerRespawner;
import me.fromgate.reactions.util.location.Teleporter;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.message.RaDebug;
import me.fromgate.reactions.util.mob.EntityUtil;
import me.fromgate.reactions.util.mob.MobSpawn;
import org.bukkit.Bukkit;
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
	public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return;
		if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;
		StorageManager.raiseMobClickActivator(event.getPlayer(), (LivingEntity) event.getRightClicked());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		// TODO: That's not really good solution
		/*
		Bukkit.getScheduler().runTask(ReActions.getPlugin(), () -> {
			if (StorageManager.raiseMessageActivator(event.getPlayer(), MessageActivator.Source.CHAT_INPUT, event.getMessage())) {
				event.setCancelled(true);
			}
		});*/
		try{
			if (StorageManager.raiseMessageActivator(event.getPlayer(), MessageActivator.Source.CHAT_INPUT, event.getMessage())) {
				event.setCancelled(true);
			}
		} catch(IllegalStateException ignore) {
			Msg.logOnce("asyncchaterror", "Chat is in async thread. Because of that you should use " +
					"additional EXEC activator in some cases, like teleportation, setting blocks etc.");
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onServerCommand(ServerCommandEvent event) {
		StorageManager.raiseMessageActivator(Bukkit.getConsoleSender(), MessageActivator.Source.CONSOLE_INPUT, event.getCommand());
		if (StorageManager.raiseCommandActivator(null, event.getCommand(), event.isCancelled())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (StorageManager.raiseCommandActivator(event.getPlayer(), event.getMessage().replaceFirst("/", ""), event.isCancelled())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		for (Activator activator : ActivatorsManager.getActivators(ActivatorType.SIGN)) {
			SignActivator signAct = (SignActivator) activator;
			if (!signAct.checkMask(event.getLines())) continue;
			if (event.getPlayer().hasPermission("reactions.sign." + signAct.getBase().getName().toLowerCase())) return;
			Msg.MSG_SIGNFORBIDDEN.print(event.getPlayer(), '4', 'c', signAct.getBase().getName());
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onItemHeld(PlayerItemHeldEvent event) {
		StorageManager.raiseItemHoldActivator(event.getPlayer());
		StorageManager.raiseItemWearActivator(event.getPlayer());
		if (StorageManager.raiseItemHeldActivator(event.getPlayer(), event.getNewSlot(), event.getPreviousSlot()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryInteract(InventoryInteractEvent event) {
		StorageManager.raiseItemHoldActivator((Player) event.getWhoClicked());
		StorageManager.raiseItemWearActivator((Player) event.getWhoClicked());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		StorageManager.raiseItemHoldActivator((Player) event.getPlayer());
		StorageManager.raiseItemWearActivator((Player) event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPickupItem(EntityPickupItemEvent event) {
		if(event.getEntityType() != EntityType.PLAYER) return;
		Player player = (Player) event.getEntity();
		StorageManager.raiseItemHoldActivator(player);
		StorageManager.raiseItemWearActivator(player);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		PlayerRespawner.addPlayerRespawn(event);
		StorageManager.raisePvpKillActivator(event);
		StorageManager.raisePvpDeathActivator(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onItemConsume(PlayerItemConsumeEvent event) {
		event.setCancelled(StorageManager.raiseItemConsumeActivator(event));
	}


	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerClickMob(PlayerInteractEntityEvent event) {
		StorageManager.raiseItemClickActivator(event);
		if (!(event.getRightClicked() instanceof LivingEntity)) return;
		if (event.getHand() != EquipmentSlot.HAND) return;
		StorageManager.raiseMobClickActivator(event.getPlayer(), (LivingEntity) event.getRightClicked());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		PlayerRespawner.raisePlayerRespawnActivator(event.getPlayer());
		StorageManager.raiseAllRegionActivators(event.getPlayer(), event.getRespawnLocation(), event.getPlayer().getLocation());
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
			StorageManager.raiseExecActivator(killer, exec + " player:" + killer.getName());
		} else StorageManager.raiseMobKillActivator(killer, event.getEntity());

	}


	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMobGrowl(EntityDamageEvent event) {
		if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
				(event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
		LivingEntity damager = EntityUtil.getDamagerEntity(evdmg);
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
		if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
				(event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) &&
				(event.getCause() != EntityDamageEvent.DamageCause.MAGIC)) return;
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
		LivingEntity damager = EntityUtil.getDamagerEntity(evdmg);
		if (damager == null) return;
		if (damager.getType() != EntityType.PLAYER) return;
		if (StorageManager.raiseMobDamageActivator(event, (Player) damager)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDamageByMob(EntityDamageEvent event) {
		if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
				(event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) &&
				(event.getCause() != EntityDamageEvent.DamageCause.MAGIC))
			return;
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
		LivingEntity damager = EntityUtil.getDamagerEntity(evdmg);
		if (damager == null) return;
		if (damager.getType() == EntityType.PLAYER) return;
		if (!damager.hasMetadata("ReActions-dmg")) return;
		double dmg = damager.getMetadata("ReActions-dmg").get(0).asDouble();
		if (dmg < 0) return;
		event.setDamage(event.getDamage() * dmg);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCheckGodEvent(EntityDamageEvent event) {
		GodModeListener.cancelGodEvent(event);
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
			if (StorageManager.raiseDamageByMobActivator(evdmg))
				event.setCancelled(true);
		} else if ((event instanceof EntityDamageByBlockEvent)) {
			source = "BLOCK";
			EntityDamageByBlockEvent evdmg = (EntityDamageByBlockEvent) event;
			Block blockDamager = evdmg.getDamager();
			if (StorageManager.raiseDamageByBlockActivator(evdmg, blockDamager)) event.setCancelled(true);
		} else {
			source = "OTHER";
		}

		if (StorageManager.raiseDamageActivator(event, source)) event.setCancelled(true);
	}

	/* TODO PotionSplashbyMob and PotionSplashbyPlayer activators
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent event) {
	}*/

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (StorageManager.raiseEntityChangeBlockActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onProjectileHit(ProjectileHitEvent event) {
		StorageManager.raiseProjectileHitActivator(event);
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


	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		WaitingManager.refreshPlayer(player);
		TemporaryOp.removeTempOp(player);
		RaDebug.offPlayerDebug(player);
		MoveListener.initLocation(player);

		StorageManager.raiseJoinActivator(player, !player.hasPlayedBefore());
		StorageManager.raiseAllRegionActivators(player, player.getLocation(), null);
		StorageManager.raiseCuboidActivator(player);
		StorageManager.raiseItemHoldActivator(player);
		StorageManager.raiseItemWearActivator(player);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignClick(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (!BlockUtil.isSign(event.getClickedBlock())) return;
		Sign sign = (Sign) event.getClickedBlock().getState();
		if (sign == null) return;
		StorageManager.raiseSignActivator(event.getPlayer(), sign.getLines(), event.getClickedBlock().getLocation(), event.getAction() == Action.LEFT_CLICK_BLOCK);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		StorageManager.raiseItemClickActivator(event);
		StorageManager.raiseItemWearActivator(event.getPlayer());
		if (StorageManager.raiseBlockClickActivator(event)) event.setCancelled(true);
		if (StorageManager.raiseButtonActivator(event)) event.setCancelled(true);
		StorageManager.raisePlateActivator(event);
		if (StorageManager.raiseLeverActivator(event)) event.setCancelled(true);
		if (StorageManager.raiseDoorActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Teleporter.startTeleport(event);
		StorageManager.raiseCuboidActivator(event.getPlayer());
		StorageManager.raiseAllRegionActivators(event.getPlayer(), event.getTo(), event.getFrom());
		Teleporter.stopTeleport(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInventoryClick(InventoryClickEvent event) {
		if (StorageManager.raiseInventoryClickActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInventoryClick(InventoryCreativeEvent event) {
		if (StorageManager.raiseInventoryClickActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDrop(PlayerDropItemEvent event) {
		if (StorageManager.raiseDropActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFlight(PlayerToggleFlightEvent event) {
		if (StorageManager.raiseFlightActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityClick(PlayerInteractEntityEvent event) {
		if (StorageManager.raiseEntityClickActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (StorageManager.raiseBlockBreakActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSneak(PlayerToggleSneakEvent event) {
		if (StorageManager.raiseSneakActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPickupItem(EntityPickupItemEvent event) {
		if (StorageManager.raisePickupItemActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		TemporaryOp.removeTempOp(event.getPlayer());
		StorageManager.raiseQuitActivator(event);
		MoveListener.removeLocation(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onGameModeChange(PlayerGameModeChangeEvent event) {
		if (StorageManager.raiseGamemodeActivator(event)) event.setCancelled(true);
	}
}
