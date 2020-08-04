package me.fromgate.reactions.activators.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.triggers.ActivatorType;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaxDikiy on 2017-11-11.
 */
@Getter
public class ItemHeldStorage extends Storage {

    private final int newSlot;
    private final int previousSlot;
    private final ItemStack newItem;
    private final ItemStack previousItem;

    public ItemHeldStorage(Player player, int newSlot, int previousSlot) {
        super(player, ActivatorType.ITEM_HELD);
        this.newSlot = newSlot;
        this.previousSlot = previousSlot;
        this.newItem = this.player.getInventory().getItem(newSlot);
        this.previousItem = this.player.getInventory().getItem(previousSlot);
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        if (newItem != null) {
            VirtualItem vi = VirtualItem.fromItemStack(newItem);
            if (vi != null) {
                tempVars.put("itemnew", vi.toString());
                tempVars.put("itemnew-str", vi.toDisplayString());
            }
        }
        if (previousItem != null) {
            VirtualItem vi = VirtualItem.fromItemStack(previousItem);
            if (vi != null) {
                tempVars.put("itemprev", vi.toString());
                tempVars.put("itemprev-str", vi.toDisplayString());
            }
        }
        tempVars.put("slotNew", Integer.toString(newSlot + 1));
        tempVars.put("slotPrev", Integer.toString(previousSlot + 1));
        return tempVars;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return MapBuilder.single(CANCEL_EVENT, new BooleanValue(false));
    }
}
