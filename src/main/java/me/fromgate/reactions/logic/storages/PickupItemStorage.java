package me.fromgate.reactions.logic.storages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.DoubleValue;
import me.fromgate.reactions.util.data.ItemStackValue;
import me.fromgate.reactions.util.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-09-04.
 */
@Getter
@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class PickupItemStorage extends Storage {
    public static final String PICKUP_DELAY = "pickupdelay";
    public static final String ITEM = "item";

    Location dropLoc;
    ItemStack item;
    int pickupDelay;

    public PickupItemStorage(Player p, Item item, int pickupDelay) {
        super(p, ActivatorType.PICKUP_ITEM);
        this.item = item.getItemStack();
        this.pickupDelay = pickupDelay;
        this.dropLoc = item.getLocation();
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("droplocation", LocationUtils.locationToString(dropLoc));
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
        changeables.put(PICKUP_DELAY, new DoubleValue(pickupDelay));
        changeables.put(ITEM, new ItemStackValue(item));
    }
}
