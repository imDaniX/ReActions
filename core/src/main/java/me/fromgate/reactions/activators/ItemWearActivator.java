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
import me.fromgate.reactions.storage.ItemWearStorage;
import me.fromgate.reactions.storage.RAStorage;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class ItemWearActivator extends Activator {
	private String item;

	public ItemWearActivator(ActivatorBase base, String item) {
		super(base);
		this.item = item;
	}

	@Override
	public boolean activate(RAStorage event) {
		if (item.isEmpty() || (ItemUtil.parseItemStack(item) == null)) {
			Msg.logOnce(getBase().getName() + "activatorwearempty", "Failed to parse item of activator " + getBase().getName());
			return false;
		}
		ItemWearStorage iw = (ItemWearStorage) event;
		if (iw.isItemWeared(this.item)) {
			VirtualItem vi = VirtualItem.fromItemStack(iw.getFoundedItem(this.item));
			if (vi != null && vi.getType() != Material.AIR) {
				Variables.setTempVar("item", vi.toString());
				Variables.setTempVar("item-str", vi.toDisplayString());
			}
			return Actions.executeActivator(iw.getPlayer(), getBase());
		}
		return false;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("item", this.item);
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.ITEM_WEAR;
	}

	public String getItemStr() {
		return this.item;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (").append(this.item).append(")");
		return sb.toString();
	}

	@Override
	public boolean isValid() {
		return !Util.emptyString(item);
	}

	public static ItemWearActivator create(ActivatorBase base, Param param) {
		String item = param.getParam("item", "param-line");
		return new ItemWearActivator(base, item);
	}

	public static ItemWearActivator load(ActivatorBase base, ConfigurationSection cfg) {
		String item = cfg.getString("item");
		return new ItemWearActivator(base, item);
	}
}

