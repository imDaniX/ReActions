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
import me.fromgate.reactions.event.ItemClickEvent;
import me.fromgate.reactions.event.RAEvent;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.message.Msg;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemClickActivator extends Activator {
	private String item;
	private boolean mainHand;

	public ItemClickActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	public ItemClickActivator(String name, String item) {
		super(name, "activators");
		this.item = item;
	}


	@Override
	public boolean activate(RAEvent event) {
		if (item.isEmpty() || (ItemUtil.parseItemStack(item) == null)) {
			Msg.logOnce(this.name + "activatoritemempty", "Failed to parse item of activator " + this.name);
			return false;
		}
		if (event instanceof ItemClickEvent) {
			ItemClickEvent ie = (ItemClickEvent) event;
			if(ie.isMainHand() != mainHand)
				return false;
			if (ItemUtil.compareItemStr(ie.getItem(), this.item)) {
				VirtualItem vi = ItemUtil.itemFromItemStack(ie.getItem());
				if (vi != null) {
					Variables.setTempVar("item", vi.toString());
					Variables.setTempVar("item-str", vi.toDisplayString());
				}
				Variables.setTempVar("hand", ie.isMainHand() ? "MAIN" : "OFF");
				return Actions.executeActivator(ie.getPlayer(), this);
			}

		}
		return false;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("item", this.item);
	}

	@Override
	public void load(ConfigurationSection cfg) {
		this.item = cfg.getString("item");
		this.mainHand = cfg.getString("hand", "main").equalsIgnoreCase("main");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.ITEM_CLICK;
	}

	@Override
	public boolean isValid() {
		return !Util.emptySting(item);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (").append(this.item).append(")");
		return sb.toString();
	}

}
