package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.PickupItemStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-09-04.
 */
public class PickupItemActivator extends Activator {
    // TODO: Store VirtualItem
    private final String itemStr;

    private PickupItemActivator(ActivatorLogic base, String item) {
        super(base);
        this.itemStr = item;
    }

    public static PickupItemActivator create(ActivatorLogic base, Parameters param) {
        String item = param.getString("item", param.toString());
        return new PickupItemActivator(base, item);
    }

    public static PickupItemActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        String item = cfg.getString("item", "");
        return new PickupItemActivator(base, item);
    }

    @Override
    public boolean check(Storage event) {
        PickupItemStorage pie = (PickupItemStorage) event;
        return checkItem(pie.getItem());
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("item", itemStr);
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
