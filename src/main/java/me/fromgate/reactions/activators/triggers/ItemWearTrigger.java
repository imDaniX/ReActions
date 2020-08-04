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

package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.ItemWearStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class ItemWearTrigger extends Trigger /*implements Manageable*/ {
    // TODO: Store VirtualItem
    private final String item;
    // private final WearSlot slot;

    private ItemWearTrigger(ActivatorBase base, String item/*, WearSlot slot*/) {
        super(base);
        this.item = item;
        // this.slot = slot;
    }

    public static ItemWearTrigger create(ActivatorBase base, Parameters param) {
        String item = param.getString("item", "param-line");
        // WearSlot slot = WearSlot.getByName(param.getParam("slot", "any"));
        return new ItemWearTrigger(base, item/*, slot*/);
    }

    public static ItemWearTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        String item = cfg.getString("item");
        // WearSlot slot = WearSlot.getByName(cfg.getString("wear-slot", "any"));
        return new ItemWearTrigger(base, item/*, slot*/);
    }

    @Override
    public boolean proceed(Storage event) {
        if (item.isEmpty() || (VirtualItem.fromString(item) == null)) {
            Msg.logOnce(base.getName() + "activatorwearempty", "Failed to parse item of activator " + base.getName());
            return false;
        }
        ItemWearStorage iw = (ItemWearStorage) event;
        return iw.isItemWeared(this.item);
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
        cfg.set("item", this.item);
        // cfg.set("wear-slot", this.slot.name());
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
        return !Utils.isStringEmpty(item);
    }

	/*
	public enum WearSlot {
		HEAD, CHEST, LEGS, FEET, ANY;

		public static WearSlot getByName(String name) {
			if(Util.isStringEmpty(name)) return ANY;
			switch(name.toLowerCase(Locale.ENGLISH)) {
				case "head": return HEAD;
				case "chest": return CHEST;
				case "legs": return LEGS;
				case "feet": return FEET;
				default: return ANY;
			}
		}
	}
	*/
}

