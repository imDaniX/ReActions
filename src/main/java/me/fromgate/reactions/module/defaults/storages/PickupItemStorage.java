package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.PickupItemActivator;
import me.fromgate.reactions.util.collections.MapBuilder;
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
public class PickupItemStorage extends Storage {
    public static final String PICKUP_DELAY = "pickupdelay";
    public static final String ITEM = "item";

    private final Location dropLoc;
    private final ItemStack item;
    private final int pickupDelay;

    public PickupItemStorage(Player p, Item item, int pickupDelay) {
        super(p);
        this.item = item.getItemStack();
        this.pickupDelay = pickupDelay;
        this.dropLoc = item.getLocation();
    }

    @Override
    public Class<? extends Activator> getType() {
        return PickupItemActivator.class;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        return MapBuilder.single("droplocation", LocationUtils.locationToString(dropLoc));
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return new MapBuilder<String, DataValue>()
                .put(CANCEL_EVENT, new BooleanValue(false))
                .put(PICKUP_DELAY, new DoubleValue(pickupDelay))
                .put(ITEM, new ItemStackValue(item))
                .build();
    }
}
