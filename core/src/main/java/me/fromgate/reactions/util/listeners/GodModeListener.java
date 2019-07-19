package me.fromgate.reactions.util.listeners;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.storage.StorageManager;
import me.fromgate.reactions.util.Cfg;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class GodModeListener implements Listener {

	private static Set<EntityDamageEvent> godCheckEvents = new HashSet<>();

	public static void init() {
		if (Cfg.godActivatorEnable) {
			Bukkit.getPluginManager().registerEvents(new GodModeListener(), ReActions.getPlugin());
			new BukkitRunnable() {
				@Override
				public void run() {
					Bukkit.getOnlinePlayers().forEach(GodModeListener::setEventGod);
				}
			}.runTaskTimer(ReActions.getPlugin(), 30, Cfg.godActivatorCheckTicks);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCheckGod(EntityDamageEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			if (event.isCancelled()) {
				if (GodModeListener.checkGod(player) && GodModeListener.setGod(player)) return;
				if (GodModeListener.setGod(player) && StorageManager.raisePlayerGodChangeEvent(player, true)) {
					GodModeListener.removeGod(player);
				}
			} else if (GodModeListener.removeGod(player) && StorageManager.raisePlayerGodChangeEvent(player, false)) {
				GodModeListener.setGod(player);
				event.setCancelled(true);
			}
		}
	}

	public static boolean isGod(Player player) {
		return player.hasMetadata("reactions-god") && player.getMetadata("reactions-god").get(0).asBoolean();
	}

	public static boolean setGod(Player player) {
		if (!isGod(player)) {
			player.setMetadata("reactions-god", new FixedMetadataValue(ReActions.instance, true));
			return true;
		}
		return false;
	}

	public static boolean removeGod(Player player) {
		if (isGod(player)) {
			player.removeMetadata("reactions-god", ReActions.instance);
			return true;
		}
		return false;
	}

	public static void setCheckGod(Player player) {
		player.setMetadata("reactions-god-check", new FixedMetadataValue(ReActions.instance, true));
	}

	public static boolean checkGod(Player player) {
		boolean result = false;
		if (player.hasMetadata("reactions-god-check")) {
			result = player.getMetadata("reactions-god-check").get(0).asBoolean();
			player.removeMetadata("reactions-god-check", ReActions.instance);
		}
		return result;
	}

	public static void setEventGod(Player player) {
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, player, EntityDamageEvent.DamageCause.CUSTOM, 0);
		godCheckEvents.add(event);
		Bukkit.getPluginManager().callEvent(event);
	}

	public static void cancelGodEvent(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent && godCheckEvents.contains(event)) {
			godCheckEvents.remove(event);
			event.setCancelled(true);
		}
	}
}
