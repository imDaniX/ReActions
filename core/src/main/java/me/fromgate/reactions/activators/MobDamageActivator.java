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

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storage.MobDamageStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.location.Locator;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class MobDamageActivator extends Activator {
	private final String mobName;
	private final String mobType;
	private final String itemStr;

	public MobDamageActivator(ActivatorBase base, String type, String name, String item) {
		super(base);
		this.mobType = type;
		this.mobName = name;
		this.itemStr = item;
	}

	@Override
	public boolean activate(RAStorage event) {
		MobDamageStorage me = (MobDamageStorage) event;
		if (mobType.isEmpty()) return false;
		if (me.getEntity() == null) return false;
		if (!isActivatorMob(me.getEntity())) return false;
		if (!checkItem(me.getPlayer())) return false;
		Variables.setTempVar("moblocation", Locator.locationToString(me.getEntity().getLocation()));
		Variables.setTempVar("mobdamager", me.getPlayer() == null ? "" : me.getPlayer().getName());
		Variables.setTempVar("mobtype", me.getEntity().getType().name());
		LivingEntity mob = me.getEntity();
		Player player = mob instanceof Player ? (Player) mob : null;
		String mobName = (player == null) ? me.getEntity().getCustomName() : player.getName();
		Variables.setTempVar("mobname", mobName != null && !mobName.isEmpty() ? mobName : me.getEntity().getType().name());
		Variables.setTempVar("damage", Double.toString(me.getDamage()));
		boolean result = Actions.executeActivator(me.getPlayer(), getBase());
		String dmgStr = Variables.getTempVar("damage");
		if (Util.FLOAT.matcher(dmgStr).matches()) me.setDamage(Double.parseDouble(dmgStr));
		return result;
	}

	private boolean checkItem(Player player) {
		if (this.itemStr.isEmpty()) return true;
		return ItemUtil.compareItemStr(player.getInventory().getItemInMainHand(), this.itemStr, true);
	}

	private boolean isActivatorMob(LivingEntity mob) {
		if (!mobName.isEmpty()) {
			if (!ChatColor.translateAlternateColorCodes('&', mobName.replace("_", " ")).equals(getMobName(mob)))
				return false;
		} else if (!getMobName(mob).isEmpty()) return false;
		return mob.getType().name().equalsIgnoreCase(this.mobType);
	}


	private String getMobName(LivingEntity mob) {
		if (mob.getCustomName() == null) return "";
		return mob.getCustomName();
	}


	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("mob-type", this.mobType);
		cfg.set("mob-name", this.mobName);
		cfg.set("item", this.itemStr);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.MOB_DAMAGE;
	}

	@Override
	public boolean isValid() {
		return !Util.emptyString(mobType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("type:").append(mobType.isEmpty() ? "-" : mobType.toUpperCase());
		sb.append(" name:").append(mobName.isEmpty() ? "-" : mobName.isEmpty());
		sb.append(")");
		return sb.toString();
	}

	public static MobDamageActivator create(ActivatorBase base, Param param) {
		String type = param.toString();
		String name = "";
		String itemStr = "";
		if (param.isParamsExists("type")) {
			type = param.getParam("type");
			name = param.getParam("name");
			itemStr = param.getParam("item");
		} else if (param.toString().contains("$")) {
			name = type.substring(0, type.indexOf("$"));
			type = type.substring(name.length() + 1);
		}
		return new MobDamageActivator(base, type, name, itemStr);
	}

	public static MobDamageActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String type = cfg.getString("mob-type", "");
		String name = cfg.getString("mob-name", "");
		String itemStr = cfg.getString("item", "");
		return new MobDamageActivator(base, type, name, itemStr);
	}
}
