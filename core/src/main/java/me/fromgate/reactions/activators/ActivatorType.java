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

package me.fromgate.reactions.activators;


import lombok.Getter;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public enum ActivatorType {
	// Actually all the constructors can be private. Maybe use factory instead of lambda'n'interface abuse?
	// alias, create method, load method, can be located, need target block
	EXEC("exe", Activator::create, Activator::load),
	BUTTON("b", ButtonActivator::create, ButtonActivator::load, true, true),
	PLATE("plt", PlateActivator::create, PlateActivator::load, true, true),
	COMMAND("cmd", CommandActivator::create, CommandActivator::load),
	MESSAGE("msg", MessageActivator::create, MessageActivator::load),
	PVP_KILL("pvpkill", PvpKillActivator::create, PvpKillActivator::load),
	PLAYER_DEATH("PVP_DEATH", PlayerDeathActivator::create, PlayerDeathActivator::load),
	PLAYER_RESPAWN("PVP_RESPAWN", PlayerRespawnActivator::create, PlayerRespawnActivator::load),
	LEVER("lvr", LeverActivator::create, LeverActivator::load, true, true),
	DOOR("door", DoorActivator::create, DoorActivator::load, true, true),
	JOIN("join", JoinActivator::create, JoinActivator::load),
	QUIT("quit", QuitActivator::create, QuitActivator::load),
	MOB_CLICK("mobclick", MobClickActivator::create, MobClickActivator::load, true),
	MOB_KILL("mobkill", MobKillActivator::create, MobKillActivator::load),
	MOB_DAMAGE("mobdamage", MobDamageActivator::create, MobDamageActivator::load),
	ITEM_CLICK("itemclick", ItemClickActivator::create, ItemClickActivator::load),
	ITEM_CONSUME("consume", ItemConsumeActivator::create, ItemConsumeActivator::load),
	ITEM_HOLD("itemhold", ItemHoldActivator::create, ItemHoldActivator::load),
	ITEM_HELD("itemheld", ItemHeldActivator::create, ItemHeldActivator::load),
	ITEM_WEAR("itemwear", ItemWearActivator::create, ItemWearActivator::load),
	SIGN("sign", SignActivator::create, SignActivator::load, true, true),
	BLOCK_CLICK("blockclick", BlockClickActivator::create, BlockClickActivator::load, true, true),
	INVENTORY_CLICK("inventoryclick", InventoryClickActivator::create, InventoryClickActivator::load),
	DROP("drop", DropActivator::create, DropActivator::load),
	PICKUP_ITEM("pickupitem", PickupItemActivator::create, PickupItemActivator::load),
	FLIGHT("flight", FlightActivator::create, FlightActivator::load),
	ENTITY_CLICK("entityclick", EntityClickActivator::create, EntityClickActivator::load),
	BLOCK_BREAK("blockbreak", BlockBreakActivator::create, BlockBreakActivator::load, true, true),
	SNEAK("sneak", SneakActivator::create, SneakActivator::load),
	DAMAGE("damage", DamageActivator::create, DamageActivator::load),
	DAMAGE_BY_MOB("damagebymob", DamageByMobActivator::create, DamageByMobActivator::load),
	DAMAGE_BY_BLOCK("damagebyblock", DamageByBlockActivator::create, DamageByBlockActivator::load, true, true),
	VARIABLE("var", VariableActivator::create, VariableActivator::load),
	GAMEMODE("gamemode", GamemodeActivator::create, GamemodeActivator::load),
	GOD("god", GodActivator::create, GodActivator::load),
	CUBOID("cube", CuboidActivator::create, CuboidActivator::load, true),
	PROJECTILE_HIT("projhit", ProjectileHitActivator::create, ProjectileHitActivator::load),
	/* WorldGuard */
	REGION("rg", RegionActivator::create, RegionActivator::load, true),
	REGION_ENTER("rgenter", RegionEnterActivator::create, RegionEnterActivator::load, true),
	REGION_LEAVE("rgleave", RegionLeaveActivator::create, RegionLeaveActivator::load, true),
	/* WorldEdit */
	WE_SELECTION_REGION("weselectionregion", WeSelectionActivator::create, WeSelectionActivator::load),
	WE_CHANGE("wechange", WeChangeActivator::create, WeChangeActivator::load),
	/* Factions */
	FCT_CHANGE("faction", FactionActivator::create, FactionActivator::load),
	FCT_RELATION("fctrelation", FactionRelationActivator::create, FactionRelationActivator::load),
	FCT_CREATE("fctcreate", FactionCreateActivator::create, FactionCreateActivator::load),
	FCT_DISBAND("fctdisband", FactionDisbandActivator::create, FactionDisbandActivator::load);

	private final String alias;
	private final RaSupplier<Param> create;
	private final RaSupplier<ConfigurationSection> load;
	@Getter private final boolean needTargetBlock;
	@Getter private final boolean located;

	ActivatorType(String alias, RaSupplier<Param> create, RaSupplier<ConfigurationSection> load, boolean located, boolean needTargetBlock) {
		this.alias = alias.toUpperCase();
		this.create = create;
		this.load = load;
		this.needTargetBlock = needTargetBlock;
		this.located = located;
	}

	ActivatorType(String alias, RaSupplier<Param> create, RaSupplier<ConfigurationSection> load, boolean located) {
		this(alias, create, load, located, false);
	}

	ActivatorType(String alias, RaSupplier<Param> create, RaSupplier<ConfigurationSection> load) {
		this(alias, create, load, false);
	}

	public Activator create(String name, String group, Param param) {
		ActivatorBase base = new ActivatorBase(name, group);
		return create.get(base, param);
	}

	public Activator load(String name, String group, ConfigurationSection cfg) {
		ActivatorBase base = new ActivatorBase(name, group, cfg);
		return load.get(base, cfg);
	}

	public String getAlias() {
		return this.alias;
	}

	public static boolean isValid(String str) {
		str = str.toUpperCase();
		for (ActivatorType at : ActivatorType.values())
			if (at.name().equals(str) || at.alias.equals(str)) return true;
		return false;
	}

	public static ActivatorType getByName(String name) {
		name = name.toUpperCase();
		for (ActivatorType at : ActivatorType.values())
			if (at.name().equals(name) || at.getAlias().equals(name)) return at;
		return null;
	}

	public static void listActivators(CommandSender sender, int pageNum) {
		List<String> activatorList = new ArrayList<>();
		for (ActivatorType activatorType : ActivatorType.values()) {
			String name = activatorType.name();
			String alias = activatorType.getAlias().equalsIgnoreCase(name) ? " " : " (" + activatorType.getAlias() + ") ";
			Msg activatorDesc = Msg.getByName("ACTIVATOR_" + name);
			if (activatorDesc == null) {
				Msg.LNG_MISSED_ACTIVATOR_DESC.log(name);
			} else {
				activatorList.add("&6" + name + "&e" + alias + "&3: &a" + activatorDesc.getText("NOCOLOR"));
			}
		}
		Util.printPage(sender, activatorList, Msg.MSG_ACTIVATORLISTTITLE, pageNum);
	}

	@FunctionalInterface
	public interface RaSupplier<T> {
		Activator get(ActivatorBase base, T t);
	}
}
