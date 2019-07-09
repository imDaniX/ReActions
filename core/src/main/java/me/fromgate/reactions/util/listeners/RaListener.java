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


package me.fromgate.reactions.util.listeners;


import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.event.BlockClickEvent;
import me.fromgate.reactions.event.ButtonEvent;
import me.fromgate.reactions.event.CommandEvent;
import me.fromgate.reactions.event.DamageByBlockEvent;
import me.fromgate.reactions.event.DamageByMobEvent;
import me.fromgate.reactions.event.DamageEvent;
import me.fromgate.reactions.event.DoorEvent;
import me.fromgate.reactions.event.DropEvent;
import me.fromgate.reactions.event.EntityClickEvent;
import me.fromgate.reactions.event.ExecEvent;
import me.fromgate.reactions.event.FactionCreateEvent;
import me.fromgate.reactions.event.FactionDisbandEvent;
import me.fromgate.reactions.event.FactionChangeEvent;
import me.fromgate.reactions.event.FactionRelationEvent;
import me.fromgate.reactions.event.FlightEvent;
import me.fromgate.reactions.event.GameModeEvent;
import me.fromgate.reactions.event.GodEvent;
import me.fromgate.reactions.event.ItemClickEvent;
import me.fromgate.reactions.event.ItemConsumeEvent;
import me.fromgate.reactions.event.ItemHeldEvent;
import me.fromgate.reactions.event.ItemHoldEvent;
import me.fromgate.reactions.event.ItemWearEvent;
import me.fromgate.reactions.event.JoinEvent;
import me.fromgate.reactions.event.LeverEvent;
import me.fromgate.reactions.event.MessageEvent;
import me.fromgate.reactions.event.MobClickEvent;
import me.fromgate.reactions.event.MobDamageEvent;
import me.fromgate.reactions.event.MobKillEvent;
import me.fromgate.reactions.event.PickupItemEvent;
import me.fromgate.reactions.event.PlateEvent;
import me.fromgate.reactions.event.BlockBreakEvent;
import me.fromgate.reactions.event.InventoryClickEvent;
import me.fromgate.reactions.event.RespawnedEvent;
import me.fromgate.reactions.event.DeathEvent;
import me.fromgate.reactions.event.PvpKillEvent;
import me.fromgate.reactions.event.QuitEvent;
import me.fromgate.reactions.event.RegionEnterEvent;
import me.fromgate.reactions.event.RegionEvent;
import me.fromgate.reactions.event.RegionLeaveEvent;
import me.fromgate.reactions.event.SignEvent;
import me.fromgate.reactions.event.SneakEvent;
import me.fromgate.reactions.event.VariableEvent;
import me.fromgate.reactions.event.WeChangeEvent;
import me.fromgate.reactions.event.WeSelectionRegionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RaListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onButton(ButtonEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlate(PlateEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onRegion(RegionEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onRegionEnter(RegionEnterEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onRegionLeave(RegionLeaveEvent event) {
		event.setCancelled(Activators.activate(event));
	}


	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onRegionLeave(ExecEvent event) {
		event.setCancelled(Activators.activate(event));
	}


	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCommandActivator(CommandEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPvpKillActivator(PvpKillEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPvpDeathActivator(DeathEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPvpRespawnActivator(RespawnedEvent event) {
		event.setCancelled(Activators.activate(event));
	}


	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onLeverActivator(LeverEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDoorActivator(DoorEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onJoinActivator(JoinEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onQuitActivator(QuitEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMobClickActivator(MobClickEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMobKillActivator(MobKillEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMobDamageActivator(MobDamageEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemClickActivator(ItemClickEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onConsume(ItemConsumeEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onItemHold(ItemHoldEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onItemWear(ItemWearEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSignClick(SignEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFactionEvent(FactionChangeEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFactionRelationEvent(FactionRelationEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFactionCreateEvent(FactionCreateEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFactionDisbandEvent(FactionDisbandEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onMessageEvent(MessageEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockClickActivator(BlockClickEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryClickActivator(InventoryClickEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDropActivator(DropEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPickupItemActivator(PickupItemEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onFlightActivator(FlightEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityClickActivator(EntityClickEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreakActivator(BlockBreakEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSneakActivator(SneakEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDamage(DamageEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDamageByMob(DamageByMobEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDamageByBlock(DamageByBlockEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onVariableEvent(VariableEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onWESelectionRegionEvent(WeSelectionRegionEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onWEChangeEvent(WeChangeEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onGameModeChangeEvent(GameModeEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onGodChangeEvent(GodEvent event) {
		event.setCancelled(Activators.activate(event));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onItemHeldEvent(ItemHeldEvent event) {
		event.setCancelled(Activators.activate(event));
	}

}