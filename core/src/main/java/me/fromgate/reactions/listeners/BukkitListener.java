package me.fromgate.reactions.listeners;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.externals.RaEconomics;
import me.fromgate.reactions.externals.RaVault;
import me.fromgate.reactions.storages.StoragesManager;
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

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if(StoragesManager.raiseTeleportActivator(event.getPlayer(), event.getCause(), event.getTo().getWorld().getName()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return;
		if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;
		StoragesManager.raiseMobClickActivator(event.getPlayer(), (LivingEntity) event.getRightClicked());
	}

	@EventHandler(ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		// TODO: That's not really good solution
		/*
		Bukkit.getScheduler().runTask(ReActions.getPlugin(), () -> {
			if (StorageManager.raiseMessageActivator(event.getPlayer(), MessageActivator.Source.CHAT_INPUT, event.getMessage())) {
				event.setCancelled(true);
			}
		});*/
		try{
			if (StoragesManager.raiseMessageActivator(event.getPlayer(), MessageActivator.Source.CHAT_INPUT, event.getMessage())) {
				event.setCancelled(true);
			}
		} catch(IllegalStateException ignore) {
			Msg.logOnce("asyncchaterror", "Chat is in async thread. Because of that you should use " +
					"additional EXEC activator in some cases, like teleportation, setting blocks etc.");
		}
	}

	@EventHandler
	public void onServerCommand(ServerCommandEvent event) {
		if(StoragesManager.raisePrecommandActivator(null, event.getSender(), event.getCommand()))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if(StoragesManager.raisePrecommandActivator(event.getPlayer(), event.getPlayer(), event.getMessage()))
			event.setCancelled(true);
	}

	// TODO: Optimize a bit
	@EventHandler(ignoreCancelled = true)
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

	@EventHandler(ignoreCancelled = true)
	public void onItemHeld(PlayerItemHeldEvent event) {
		StoragesManager.raiseItemHoldActivator(event.getPlayer());
		StoragesManager.raiseItemWearActivator(event.getPlayer());
		if (StoragesManager.raiseItemHeldActivator(event.getPlayer(), event.getNewSlot(), event.getPreviousSlot()))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryInteract(InventoryInteractEvent event) {
		StoragesManager.raiseItemHoldActivator((Player) event.getWhoClicked());
		StoragesManager.raiseItemWearActivator((Player) event.getWhoClicked());
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		StoragesManager.raiseItemHoldActivator((Player) event.getPlayer());
		StoragesManager.raiseItemWearActivator((Player) event.getPlayer());
	}

	@EventHandler(ignoreCancelled = true)
	public void onPickupItem(EntityPickupItemEvent event) {
		if(event.getEntityType() != EntityType.PLAYER) return;
		Player player = (Player) event.getEntity();
		StoragesManager.raiseItemHoldActivator(player);
		StoragesManager.raiseItemWearActivator(player);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		PlayerRespawner.addPlayerRespawn(event);
		StoragesManager.raisePvpKillActivator(event);
		StoragesManager.raisePvpDeathActivator(event);
	}

	@EventHandler(ignoreCancelled = true)
	public void onItemConsume(PlayerItemConsumeEvent event) {
		event.setCancelled(StoragesManager.raiseItemConsumeActivator(event));
	}


	@EventHandler(ignoreCancelled = true)
	public void onPlayerClickMob(PlayerInteractEntityEvent event) {
		StoragesManager.raiseItemClickActivator(event);
		if (!(event.getRightClicked() instanceof LivingEntity)) return;
		if (event.getHand() != EquipmentSlot.HAND) return;
		StoragesManager.raiseMobClickActivator(event.getPlayer(), (LivingEntity) event.getRightClicked());
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		// TODO: Set respawn location
		PlayerRespawner.raisePlayerRespawnActivator(event.getPlayer());
		StoragesManager.raiseAllRegionActivators(event.getPlayer(), event.getRespawnLocation(), event.getPlayer().getLocation());
	}


	@EventHandler(ignoreCancelled = true)
	public void onDropLoot(EntityDeathEvent event) {
		Player killer = EntityUtil.getKiller(event.getEntity().getLastDamageCause());

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
			StoragesManager.raiseExecActivator(killer, exec + " player:" + killer.getName());
		} else StoragesManager.raiseMobKillActivator(killer, event.getEntity());

	}


	@EventHandler(ignoreCancelled = true)
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
		if (StoragesManager.raiseMobDamageActivator(event, (Player) damager)) event.setCancelled(true);
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
			if (StoragesManager.raiseDamageByMobActivator(evdmg))
				event.setCancelled(true);
		} else if ((event instanceof EntityDamageByBlockEvent)) {
			source = "BLOCK";
			EntityDamageByBlockEvent evdmg = (EntityDamageByBlockEvent) event;
			Block blockDamager = evdmg.getDamager();
			if (StoragesManager.raiseDamageByBlockActivator(evdmg, blockDamager)) event.setCancelled(true);
		} else {
			source = "OTHER";
		}

		if (StoragesManager.raiseDamageActivator(event, source)) event.setCancelled(true);
	}

	/* TODO PotionSplashbyMob and PotionSplashbyPlayer activators
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent event) {
	}*/

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (StoragesManager.raiseEntityChangeBlockActivator(event)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onProjectileHit(ProjectileHitEvent event) {
		StoragesManager.raiseProjectileHitActivator(event);
	}

	@EventHandler(ignoreCancelled = true)
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

	@EventHandler(ignoreCancelled = true)
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


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		WaitingManager.refreshPlayer(player);
		TemporaryOp.removeTempOp(player);
		RaDebug.offPlayerDebug(player);
		MoveListener.initLocation(player);

		StoragesManager.raiseJoinActivator(player, !player.hasPlayedBefore());
		StoragesManager.raiseAllRegionActivators(player, player.getLocation(), null);
		StoragesManager.raiseCuboidActivator(player);
		StoragesManager.raiseItemHoldActivator(player);
		StoragesManager.raiseItemWearActivator(player);
	}

	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (!BlockUtil.isSign(event.getClickedBlock())) return;
		Sign sign = (Sign) event.getClickedBlock().getState();
		if (sign == null) return;
		StoragesManager.raiseSignActivator(event.getPlayer(), sign.getLines(), event.getClickedBlock().getLocation(), event.getAction() == Action.LEFT_CLICK_BLOCK);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		StoragesManager.raiseItemClickActivator(event);
		StoragesManager.raiseItemWearActivator(event.getPlayer());
		if (StoragesManager.raiseBlockClickActivator(event)) event.setCancelled(true);
		if (StoragesManager.raiseButtonActivator(event)) event.setCancelled(true);
		StoragesManager.raisePlateActivator(event);
		if (StoragesManager.raiseLeverActivator(event)) event.setCancelled(true);
		if (StoragesManager.raiseDoorActivator(event)) event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Teleporter.startTeleport(event);
		StoragesManager.raiseCuboidActivator(event.getPlayer());
		StoragesManager.raiseAllRegionActivators(event.getPlayer(), event.getTo(), event.getFrom());
		Teleporter.stopTeleport(event.getPlayer());
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (StoragesManager.raiseInventoryClickActivator(event)) event.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClick(InventoryCreativeEvent event) {
		if (StoragesManager.raiseInventoryClickActivator(event)) event.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (StoragesManager.raiseDropActivator(event)) event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onFlight(PlayerToggleFlightEvent event) {
		if (StoragesManager.raiseFlightActivator(event)) event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityClick(PlayerInteractEntityEvent event) {
		if (StoragesManager.raiseEntityClickActivator(event)) event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (StoragesManager.raiseBlockBreakActivator(event)) event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onSneak(PlayerToggleSneakEvent event) {
		if (StoragesManager.raiseSneakActivator(event)) event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPickupItem(EntityPickupItemEvent event) {
		if (StoragesManager.raisePickupItemActivator(event)) event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		TemporaryOp.removeTempOp(event.getPlayer());
		StoragesManager.raiseQuitActivator(event);
		MoveListener.removeLocation(event.getPlayer());
	}

	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent event) {
		if (StoragesManager.raiseGamemodeActivator(event)) event.setCancelled(true);
	}
}
