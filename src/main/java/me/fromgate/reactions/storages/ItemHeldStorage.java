package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-11-11.
 */
public class ItemHeldStorage extends Storage {
    @Getter
    private final int newSlot;
    @Getter
    private final int previousSlot;
    @Getter
    private final ItemStack newItem;
    @Getter
    private final ItemStack previousItem;

    public ItemHeldStorage(Player player, int newSlot, int previousSlot) {
        super(player, ActivatorType.ITEM_HELD);
        this.newSlot = newSlot;
        this.previousSlot = previousSlot;
        this.newItem = this.getPlayer().getInventory().getItem(newSlot);
        this.previousItem = this.getPlayer().getInventory().getItem(previousSlot);
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        if(newItem != null) {
            VirtualItem vi = VirtualItem.fromItemStack(newItem);
            if(vi != null) {
                tempVars.put("itemnew", vi.toString());
                tempVars.put("itemnew-str", vi.toDisplayString());
            }
        }
        if(previousItem != null) {
            VirtualItem vi = VirtualItem.fromItemStack(previousItem);
            if(vi != null) {
                tempVars.put("itemprev", vi.toString());
                tempVars.put("itemprev-str", vi.toDisplayString());
            }
        }
        tempVars.put("slotNew", Integer.toString(newSlot + 1));
        tempVars.put("slotPrev", Integer.toString(previousSlot + 1));
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
    }
}
