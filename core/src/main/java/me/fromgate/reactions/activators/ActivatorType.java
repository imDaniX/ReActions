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


import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public enum ActivatorType {
	// алиас, класс активатора, блокозависимый, локационный
	BUTTON("b", ButtonActivator.class, true, true),
	PLATE("plt", PlateActivator.class, true, true),
	REGION("rg", RegionActivator.class, false, true),
	REGION_ENTER("rgenter", RegionEnterActivator.class, false, true),
	REGION_LEAVE("rgleave", RegionLeaveActivator.class, false, true),
	EXEC("exe", ExecActivator.class),
	COMMAND("cmd", CommandActivator.class),
	MESSAGE("msg", MessageActivator.class),
	PVP_KILL("pvpkill", PvpKillActivator.class),
	PLAYER_DEATH("PVP_DEATH", PlayerDeathActivator.class),
	PLAYER_RESPAWN("PVP_RESPAWN", PlayerRespawnActivator.class),
	LEVER("lvr", LeverActivator.class, true, true),
	DOOR("door", DoorActivator.class, true, true),
	JOIN("join", JoinActivator.class),
	QUIT("quit", QuitActivator.class),
	MOB_CLICK("mobclick", MobClickActivator.class, false, true),
	MOB_KILL("mobkill", MobKillActivator.class),
	MOB_DAMAGE("mobdamage", MobDamageActivator.class),
	ITEM_CLICK("itemclick", ItemClickActivator.class),
	ITEM_CONSUME("consume", ItemConsumeActivator.class),
	ITEM_HOLD("itemhold", ItemHoldActivator.class),
	ITEM_HELD("itemheld", ItemHeldActivator.class),
	ITEM_WEAR("itemwear", ItemWearActivator.class),
	FCT_CHANGE("faction", FactionActivator.class),
	FCT_RELATION("fctrelation", FactionRelationActivator.class),
	FCT_CREATE("fctcreate", FactionCreateActivator.class),
	FCT_DISBAND("fctdisband", FactionDisbandActivator.class),
	SIGN("sign", SignActivator.class, true, true),
	BLOCK_CLICK("blockclick", BlockClickActivator.class, true, true),
	INVENTORY_CLICK("inventoryclick", InventoryClickActivator.class),
	DROP("drop", DropActivator.class),
	PICKUP_ITEM("pickupitem", PickupItemActivator.class),
	FLIGHT("flight", FlightActivator.class),
	ENTITY_CLICK("entityclick", EntityClickActivator.class),
	BLOCK_BREAK("blockbreak", BlockBreakActivator.class, true, true),
	SNEAK("sneak", SneakActivator.class),
	DAMAGE("damage", DamageActivator.class),
	DAMAGE_BY_MOB("damagebymob", DamageByMobActivator.class),
	DAMAGE_BY_BLOCK("damagebyblock", DamageByBlockActivator.class, true, true),
	VARIABLE("var", VariableActivator.class),
	WE_SELECTION_REGION("weselectionregion", WeSelectionRegionActivator.class),
	WE_CHANGE("wechange", WeChangeActivator.class),
	GAMEMODE("gamemode", GamemodeActivator.class),
	GOD("god", GodActivator.class),
	CUBOID("cuboid", CuboidActivator.class, false, true);

	private String alias;
	private Class<? extends Activator> aClass;
	private boolean needTargetBlock;
	private boolean located;

	ActivatorType(String alias, Class<? extends Activator> aClass, boolean needTargetBlock, boolean located) {
		this.alias = alias;
		this.aClass = aClass;
		this.needTargetBlock = needTargetBlock;
		this.located = located;
	}

	ActivatorType(String alias, Class<? extends Activator> aClass, boolean needTargetBlock) {
		this(alias, aClass, needTargetBlock, false);
	}

	ActivatorType(String alias, Class<? extends Activator> aClass) {
		this(alias, aClass, false);
	}

	public Class<? extends Activator> getActivatorClass() {
		return aClass;
	}

	public Activator create(String name, Block targetBlock, String param) {
		Constructor<? extends Activator> constructor;
		Activator activator = null;
		try {
			if (this.needTargetBlock) {
				constructor = aClass.getConstructor(String.class, Block.class, String.class);
				activator = constructor.newInstance(name, targetBlock, param);
			} else {
				constructor = aClass.getConstructor(String.class, String.class);
				activator = constructor.newInstance(name, param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activator;
	}


	public String getAlias() {
		return this.alias;
	}

	public static boolean isValid(String str) {
		for (ActivatorType at : ActivatorType.values())
			if (at.name().equalsIgnoreCase(str) || at.alias.equalsIgnoreCase(str)) return true;
		return false;
	}

	public static ActivatorType getByName(String name) {
		for (ActivatorType at : ActivatorType.values())
			if (at.name().equalsIgnoreCase(name) || at.getAlias().equalsIgnoreCase(name)) return at;
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

	public boolean isLocated() {
		return located;
	}
}
