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

package me.fromgate.reactions.module.defaults.activators;

import lombok.Getter;
import me.fromgate.reactions.logic.ActivatorLogic;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.storages.ItemHoldStorage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.item.VirtualItem;
import me.fromgate.reactions.util.message.Msg;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class ItemHoldActivator extends Activator /*implements Manageable*/ {
    // TODO: Store VirtualItem
    private final String itemStr;
    // TODO: Hand option

    private ItemHoldActivator(ActivatorLogic base, String item) {
        super(base);
        this.itemStr = item;
    }

    public static ItemHoldActivator create(ActivatorLogic base, Parameters param) {
        String item = param.getString("item", "");
        return new ItemHoldActivator(base, item);
    }

    public static ItemHoldActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        String item = cfg.getString("item", "");
        return new ItemHoldActivator(base, item);
    }

    @Override
    public boolean checkStorage(Storage event) {
        if (itemStr.isEmpty() || (VirtualItem.fromString(itemStr) == null)) {
            Msg.logOnce(logic.getName() + "activatorholdempty", "Failed to parse itemStr of activator " + logic.getName());
            return false;
        }
        ItemHoldStorage ie = (ItemHoldStorage) event;
        return ItemUtils.compareItemStr(ie.getItem(), this.itemStr);
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("item", itemStr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (").append(this.itemStr).append(")");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        return !Utils.isStringEmpty(itemStr);
    }
}

