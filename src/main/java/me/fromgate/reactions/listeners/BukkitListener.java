package me.fromgate.reactions.listeners;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.ActivatorsManager;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.events.PlayerAttacksEntityEvent;
import me.fromgate.reactions.events.PlayerPickupItemEvent;
import me.fromgate.reactions.externals.RaEconomics;
import me.fromgate.reactions.externals.RaVault;
import me.fromgate.reactions.storages.BlockBreakStorage;
import me.fromgate.reactions.storages.DamageStorage;
import me.fromgate.reactions.storages.DropStorage;
import me.fromgate.reactions.storages.InventoryClickStorage;
import me.fromgate.reactions.storages.ItemStoragesManager;
import me.fromgate.reactions.storages.MessageStorage;
import me.fromgate.reactions.storages.MobDamageStorage;
import me.fromgate.reactions.storages.PickupItemStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.storages.StoragesManager;
import me.fromgate.reactions.storages.TeleportStorage;
import me.fromgate.reactions.time.waiter.WaitingManager;
import me.fromgate.reactions.util.BlockUtil;
import me.fromgate.reactions.util.TemporaryOp;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.location.PlayerRespawner;
import me.fromgate.reactions.util.location.Teleporter;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.message.RaDebug;
import me.fromgate.reactions.util.mob.EntityUtil;
import me.fromgate.reactions.util.mob.MobSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
import java.util.Map;

public class BukkitListener implements Listener {

    // TODO: I really don't like it
    private static boolean raiseEntityChangeBlockActivator(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            Location loc = event.getEntity().getLocation();
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.getLocation().distanceSquared(loc) > 0.7) continue;
                EntityDamageByEntityEvent ev = new EntityDamageByEntityEvent(event.getEntity(), p, EntityDamageEvent.DamageCause.FALLING_BLOCK, 0);
                Bukkit.getPluginManager().callEvent(ev);
                return ev.isCancelled();
            }
        }
        return false;
    }

    @EventHandler
    public void raisePickupEvent(EntityPickupItemEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        PlayerPickupItemEvent plEvent = new PlayerPickupItemEvent((Player) event.getEntity(), event.getItem());
        Bukkit.getPluginManager().callEvent(plEvent);
        event.setCancelled(plEvent.isCancelled());
    }

    @EventHandler
    public void raiseAttackEvent(EntityDamageByEntityEvent event) {
        LivingEntity damager = EntityUtil.getDamagerEntity(event);
        if (damager == null || damager.getType() != EntityType.PLAYER) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        PlayerAttacksEntityEvent plEvent = new PlayerAttacksEntityEvent((Player) damager,
                (LivingEntity) event.getEntity(),
                event.getDamage(),
                event.getCause());
        Bukkit.getPluginManager().callEvent(plEvent);
        event.setDamage(plEvent.getDamage());
        event.setCancelled(plEvent.isCancelled());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Map<String, DataValue> changeables = StoragesManager.raiseTeleportActivator(
                event.getPlayer(),
                event.getCause(),
                event.getTo());
        event.setTo(changeables.get(TeleportStorage.LOCATION_TO).asLocation());
        event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;
        if (StoragesManager.raiseMobClickActivator(event.getPlayer(), (LivingEntity) event.getRightClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        // TODO: That's not really good solution
		/*
		Bukkit.getScheduler().runTask(ReActions.getPlugin(), () -> {
			if (StorageManager.raiseMessageActivator(event.getPlayer(), MessageActivator.Source.CHAT_INPUT, event.getMessage())) {
				event.setCancelled(true);
			}
		});*/
        try {
            Map<String, DataValue> changeables = StoragesManager.raiseMessageActivator(event.getPlayer(),
                    MessageActivator.Source.CHAT_INPUT,
                    event.getMessage());
            if (changeables == null) return;
            event.setMessage(changeables.get(MessageStorage.MESSAGE).asString());
            event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
        } catch (IllegalStateException ignore) {
            Msg.logOnce("asyncchaterror", "Chat is in async thread. Because of that you should use " +
                    "additional EXEC activator in some cases, like teleportation, setting blocks etc.");
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        if (StoragesManager.raisePrecommandActivator(null, event.getSender(), event.getCommand()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (StoragesManager.raisePrecommandActivator(null, event.getPlayer(), event.getMessage().substring(1)))
            event.setCancelled(true);
    }

    // TODO: All the checks should be inside activator
    @EventHandler
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

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        if (StoragesManager.raiseItemHeldActivator(event.getPlayer(), event.getNewSlot(), event.getPreviousSlot()))
            event.setCancelled(true);
        else {
            ItemStoragesManager.raiseItemHoldActivator(event.getPlayer());
            ItemStoragesManager.raiseItemWearActivator(event.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        ItemStoragesManager.raiseItemHoldActivator((Player) event.getWhoClicked());
        ItemStoragesManager.raiseItemWearActivator((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        ItemStoragesManager.raiseItemHoldActivator((Player) event.getPlayer());
        ItemStoragesManager.raiseItemWearActivator((Player) event.getPlayer());
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Map<String, DataValue> changeables = StoragesManager.raisePickupItemActivator(event.getPlayer(), event.getItem(), event.getItem().getPickupDelay());
        event.getItem().setPickupDelay((int) changeables.get(PickupItemStorage.PICKUP_DELAY).asDouble());
        event.getItem().setItemStack(changeables.get(PickupItemStorage.ITEM).asItemStack());
        event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
        if (event.isCancelled()) return;
        ItemStoragesManager.raiseItemHoldActivator(event.getPlayer());
        ItemStoragesManager.raiseItemWearActivator(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PlayerRespawner.addPlayerRespawn(event);
        StoragesManager.raisePvpKillActivator(event);
        StoragesManager.raisePvpDeathActivator(event);
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if (StoragesManager.raiseItemConsumeActivator(event))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerClickMob(PlayerInteractEntityEvent event) {
        StoragesManager.raiseItemClickActivator(event);
        if (!(event.getRightClicked() instanceof LivingEntity)) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        StoragesManager.raiseMobClickActivator(event.getPlayer(), (LivingEntity) event.getRightClicked());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // TODO: Set respawn location
        PlayerRespawner.raisePlayerRespawnActivator(event.getPlayer(), event.getRespawnLocation());
        StoragesManager.raiseAllRegionActivators(event.getPlayer(), event.getRespawnLocation(), event.getPlayer().getLocation());
    }

    @EventHandler
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

        if (event.getEntity().hasMetadata("ReActions-activator") && (killer != null)) {
            String exec = event.getEntity().getMetadata("ReActions-activator").get(0).asString();
            StoragesManager.raiseExecActivator(killer, exec, null);
        } else StoragesManager.raiseMobKillActivator(killer, event.getEntity());

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (raiseEntityChangeBlockActivator(event)) event.setCancelled(true);
    }

	/*
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onProjectileHit(ProjectileHitEvent event) {
		StoragesManager.raiseProjectileHitActivator(event);
	}
	*/

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCheckGodEvent(EntityDamageEvent event) {
        GodModeListener.cancelGodEvent(event);
    }

    @EventHandler
    public void onMobGrowl(PlayerAttacksEntityEvent event) {
        LivingEntity damager = event.getPlayer();
        if (!damager.hasMetadata("ReActions-growl")) return;
        String growl = damager.getMetadata("ReActions-growl").get(0).asString();
        if (Util.isStringEmpty(growl)) return;
        Util.soundPlay(damager.getLocation(), growl);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMobDamageByPlayer(PlayerAttacksEntityEvent event) {
        Map<String, DataValue> changeables = StoragesManager.raiseMobDamageActivator(event.getPlayer(), event.getEntity(), event.getDamage(), event.getCause());
        event.setDamage(changeables.get(MobDamageStorage.DAMAGE).asDouble());
        event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamageByMob(EntityDamageByEntityEvent event) {
        LivingEntity damager = EntityUtil.getDamagerEntity(event);
        if (damager == null || !damager.hasMetadata("ReActions-dmg")) return;
        double dmg = damager.getMetadata("ReActions-dmg").get(0).asDouble();
        if (dmg < 0) return;
        event.setDamage(event.getDamage() * dmg);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event) {
        String source;
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM && Math.round(event.getDamage()) == 0) return;
        if ((event instanceof EntityDamageByEntityEvent)) {
            source = "ENTITY";
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
            Map<String, DataValue> changeables = StoragesManager.raiseDamageByMobActivator(evdmg);
            event.setDamage(changeables.get(DamageStorage.DAMAGE).asDouble());
            event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
        } else if ((event instanceof EntityDamageByBlockEvent)) {
            source = "BLOCK";
            EntityDamageByBlockEvent evdmg = (EntityDamageByBlockEvent) event;
            Block blockDamager = evdmg.getDamager();
            Map<String, DataValue> changeables = StoragesManager.raiseDamageByBlockActivator(evdmg, blockDamager);
            event.setDamage(changeables.get(DamageStorage.DAMAGE).asDouble());
            event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
        } else {
            source = "OTHER";
        }

        Map<String, DataValue> changeables = StoragesManager.raiseDamageActivator(event, source);
        event.setDamage(changeables.get(DamageStorage.DAMAGE).asDouble());
        event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
    }

    @EventHandler
    public void onMobCry(EntityDamageEvent event) {
        if ((event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) && (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE))
            return;
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

    @EventHandler
    public void onPvPDamage(PlayerAttacksEntityEvent event) {
        LivingEntity target = event.getEntity();
        if (target.getType() != EntityType.PLAYER) return;
        Player damager = event.getPlayer();
        long time = System.currentTimeMillis();
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
        ItemStoragesManager.raiseItemHoldActivator(player);
        ItemStoragesManager.raiseItemWearActivator(player);
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!BlockUtil.isSign(event.getClickedBlock())) return;
        Sign sign = (Sign) event.getClickedBlock().getState();
        if (sign == null) return;
        if (StoragesManager.raiseSignActivator(event.getPlayer(), sign.getLines(), event.getClickedBlock().getLocation(), event.getAction() == Action.LEFT_CLICK_BLOCK))
            event.setCancelled(true);
    }

    // TODO: Rework
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        StoragesManager.raiseItemClickActivator(event);
        ItemStoragesManager.raiseItemWearActivator(event.getPlayer());
        if (StoragesManager.raiseBlockClickActivator(event)) event.setCancelled(true);
        if (StoragesManager.raiseButtonActivator(event)) event.setCancelled(true);
        if (StoragesManager.raisePlateActivator(event)) event.setCancelled(true);
        if (StoragesManager.raiseLeverActivator(event)) event.setCancelled(true);
        if (StoragesManager.raiseDoorActivator(event)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Teleporter.startTeleport(event);
        StoragesManager.raiseCuboidActivator(event.getPlayer());
        StoragesManager.raiseAllRegionActivators(event.getPlayer(), event.getTo(), event.getFrom());
        Teleporter.stopTeleport(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Map<String, DataValue> changeables = StoragesManager.raiseInventoryClickActivator(event);
        event.setCurrentItem(changeables.get(InventoryClickStorage.ITEM).asItemStack());
        event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Map<String, DataValue> changeables = StoragesManager.raiseDropActivator(event.getPlayer(), event.getItemDrop(), event.getItemDrop().getPickupDelay());
        event.getItemDrop().setPickupDelay((int) changeables.get(DropStorage.PICKUP_DELAY).asDouble());
        event.getItemDrop().setItemStack(changeables.get(DropStorage.ITEM).asItemStack());
        event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        if (StoragesManager.raiseFlightActivator(event.getPlayer(), event.isFlying())) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (StoragesManager.raiseEntityClickActivator(event.getPlayer(), event.getRightClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Map<String, DataValue> changeables = StoragesManager.raiseBlockBreakActivator(event.getPlayer(), event.getBlock(), event.isDropItems());
        event.setDropItems(changeables.get(BlockBreakStorage.DO_DROP).asBoolean());
        event.setCancelled(changeables.get(Storage.CANCEL_EVENT).asBoolean());
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        StoragesManager.raiseSneakActivator(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TemporaryOp.removeTempOp(event.getPlayer());
        event.setQuitMessage(StoragesManager.raiseQuitActivator(event));
        MoveListener.removeLocation(event.getPlayer());
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if (StoragesManager.raiseGamemodeActivator(event.getPlayer(), event.getNewGameMode())) event.setCancelled(true);
    }
}
