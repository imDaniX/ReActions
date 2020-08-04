package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.PickupItemStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-09-04.
 */
public class PickupItemTrigger extends Trigger {
    // TODO: Store VirtualItem
    private final String itemStr;

    private PickupItemTrigger(ActivatorBase base, String item) {
        super(base);
        this.itemStr = item;
    }

    public static PickupItemTrigger create(ActivatorBase base, Parameters param) {
        String item = param.getString("item", param.toString());
        return new PickupItemTrigger(base, item);
    }

    public static PickupItemTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        String item = cfg.getString("item", "");
        return new PickupItemTrigger(base, item);
    }

    @Override
    public boolean proceed(Storage event) {
        PickupItemStorage pie = (PickupItemStorage) event;
        return checkItem(pie.getItem());
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
        cfg.set("item", this.itemStr);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.PICKUP_ITEM;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private boolean checkItem(ItemStack item) {
        if (this.itemStr.isEmpty()) return true;
        return ItemUtils.compareItemStr(item, this.itemStr, true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("item:").append(this.itemStr);
        sb.append(")");
        return sb.toString();
    }
}
