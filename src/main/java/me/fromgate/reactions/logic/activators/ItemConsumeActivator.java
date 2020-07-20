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

package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.ActivatorType;
import me.fromgate.reactions.logic.storages.ItemConsumeStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

public class ItemConsumeActivator extends Activator {
    // TODO: Store VirtualItem
    private String item;
    // TODO: Hand option

    private ItemConsumeActivator(ActivatorBase base, String item) {
        super(base);
        this.item = item;
    }

    public static ItemConsumeActivator create(ActivatorBase base, Parameters param) {
        String item = param.getParam("item", param.getParam("param-line"));
        return new ItemConsumeActivator(base, item);
    }

    public static ItemConsumeActivator load(ActivatorBase base, ConfigurationSection cfg) {
        String item = cfg.getString("item", "");
        return new ItemConsumeActivator(base, item);
    }

    public boolean activate(Storage event) {
        if (this.item.isEmpty() || VirtualItem.fromString(this.item) == null) {
            Msg.logOnce(getBase().getName() + "activatoritemempty", "Failed to parse item of activator " + getBase().getName());
            return false;
        }
        ItemConsumeStorage ie = (ItemConsumeStorage) event;
        return ItemUtil.compareItemStr(ie.getItem(), this.item);
    }

    public void save(ConfigurationSection cfg) {
        cfg.set("item", this.item);
    }

    public ActivatorType getType() {
        return ActivatorType.ITEM_CONSUME;
    }

    @Override
    public boolean isValid() {
        return !Util.isStringEmpty(item);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append(this.item);
        sb.append(")");
        return sb.toString();
    }
}
