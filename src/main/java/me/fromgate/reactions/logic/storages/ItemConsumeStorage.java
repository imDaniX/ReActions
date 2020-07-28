package me.fromgate.reactions.logic.storages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class ItemConsumeStorage extends Storage {

    ItemStack item;
    boolean mainHand;

    public ItemConsumeStorage(Player p, ItemStack item, boolean mainHand) {
        super(p, ActivatorType.ITEM_CONSUME);
        this.item = item;
        this.mainHand = mainHand;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        VirtualItem vItem = VirtualItem.fromItemStack(item);
        if (item != null) {
            tempVars.put("item", vItem.toString());
            tempVars.put("item-str", vItem.toDisplayString());
        }
        tempVars.put("hand", mainHand ? "MAIN" : "OFF");
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
    }
}
