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

package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.ItemStackValue;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map;


public class InventoryClickStorage extends Storage {
    public static final String ITEM = "item";

    @Getter
    private final ItemStack item;
    @Getter
    private final InventoryAction action;
    @Getter
    private final ClickType clickType;
    @Getter
    private final SlotType slotType;
    @Getter
    private final InventoryType inventoryType;
    @Getter
    private final int numberKey;
    @Getter
    private final int slot;
    @Getter
    private final String inventoryName;
    private final InventoryView inventoryView;

    public InventoryClickStorage(Player p, InventoryAction action, ClickType clickType, Inventory inventory, SlotType
            slotType, ItemStack item, int numberKey, InventoryView inventoryView, int slot) {
        super(p, ActivatorType.INVENTORY_CLICK);
        this.inventoryName = inventoryView.getTitle();
        this.action = action;
        this.clickType = clickType;
        this.inventoryType = inventory.getType();
        this.slotType = slotType;
        this.item = item;
        this.numberKey = numberKey;
        this.slot = slot;
        this.inventoryView = inventoryView;
    }

    public Inventory getBottomInventory() {
        return this.inventoryView.getBottomInventory();
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("name", inventoryName);
        tempVars.put("click", clickType.name());
        tempVars.put("action", action.name());
        tempVars.put("slotType", slotType.name());
        tempVars.put("inventory", inventoryType.name());
        tempVars.put("item", ItemUtil.itemToString(item));
        tempVars.put("key", Integer.toString(numberKey + 1));
        tempVars.put("itemkey", (numberKey > -1) ? ItemUtil.itemToString(getBottomInventory().getItem(numberKey)) : "");
        tempVars.put("slot", Integer.toString(slot));
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
        changeables.put(ITEM, new ItemStackValue(item));
    }
}
