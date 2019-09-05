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
import me.fromgate.reactions.util.RaFunction;
import me.fromgate.reactions.util.message.BukkitMessenger;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Will be irrelevant because of modules(externals) system
public enum ActivatorType {
	// TODO: GlideActivator, MoneyTransactionActivator, PotionSplashActivator, ProjectileHitActivator
	EXEC("exe", ExecActivator::create, ExecActivator::load),
	BUTTON("b", ButtonActivator::create, ButtonActivator::load, true, true),
	PLATE("plt", PlateActivator::create, PlateActivator::load, true, true),
	TELEPORT("tp", TeleportActivator::create, TeleportActivator::load, true),
	PRECOMMAND("cmd", PrecommandActivator::create, PrecommandActivator::load),
	MESSAGE("msg", MessageActivator::create, MessageActivator::load),
	PVP_KILL("pvpkill", PvpKillActivator::create, PvpKillActivator::load),
	DEATH("player_death", DeathActivator::create, DeathActivator::load),
	RESPAWN("player_respawn", RespawnActivator::create, RespawnActivator::load),
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
	private final RaFunction<Param> creator;
	private final RaFunction<ConfigurationSection> loader;
	@Getter private final boolean needBlock;
	@Getter private final boolean locatable;

	private final static Map<String, ActivatorType> BY_NAME;
	static {
		Map<String, ActivatorType> byName = new HashMap<>();
		for(ActivatorType act : ActivatorType.values()) {
			byName.put(act.name(), act);
			byName.put(act.alias.toUpperCase(), act);
		}
		BY_NAME = Collections.unmodifiableMap(byName);
	}

	ActivatorType(String alias, RaFunction<Param> creator, RaFunction<ConfigurationSection> loader, boolean locatable, boolean needBlock) {
		this.alias = alias.toUpperCase();
		this.creator = creator;
		this.loader = loader;
		this.needBlock = needBlock;
		this.locatable = locatable;
	}

	ActivatorType(String alias, RaFunction<Param> creator, RaFunction<ConfigurationSection> loader, boolean locatable) {
		this(alias, creator, loader, locatable, false);
	}

	ActivatorType(String alias, RaFunction<Param> creator, RaFunction<ConfigurationSection> loader) {
		this(alias, creator, loader, false);
	}

	public Activator create(String name, String group, Param param) {
		ActivatorBase base = new ActivatorBase(name, group);
		return creator.generate(base, param);
	}

	public Activator load(String name, String group, ConfigurationSection cfg) {
		ActivatorBase base = new ActivatorBase(name, group, cfg);
		return loader.generate(base, cfg);
	}

	@SuppressWarnings("unused")
	public static boolean isValid(String str) {
		return getByName(str) != null;
	}

	public static ActivatorType getByName(String name) {
		return BY_NAME.get(name.toUpperCase());
	}

	public static void listActivators(CommandSender sender, int pageNum) {
		List<String> activatorList = new ArrayList<>();
		for (ActivatorType activatorType : ActivatorType.values()) {
			String name = activatorType.name();
			String alias = activatorType.alias.equalsIgnoreCase(name) ? " " : " (" + activatorType.alias + ") ";
			Msg activatorDesc = Msg.getByName("ACTIVATOR_" + name);
			if (activatorDesc == null) {
				Msg.LNG_MISSED_ACTIVATOR_DESC.log(name);
			} else {
				activatorList.add("&6" + name + "&e" + alias + "&3: &a" + activatorDesc.getText("NOCOLOR"));
			}
		}
		BukkitMessenger.printPage(sender, activatorList, Msg.MSG_ACTIVATORLISTTITLE, pageNum);
	}

}
