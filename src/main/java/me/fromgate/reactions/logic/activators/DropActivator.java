package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.ActivatorType;
import me.fromgate.reactions.logic.storages.DropStorage;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-05-01.
 */
public class DropActivator extends Activator {

    private final String itemStr;

    private DropActivator(ActivatorBase base, String itemStr) {
        super(base);
        this.itemStr = itemStr;
    }

    public static DropActivator create(ActivatorBase base, Parameters param) {
        String itemStr = param.getParam("item", param.toString());
        return new DropActivator(base, itemStr);
    }

    public static DropActivator load(ActivatorBase base, ConfigurationSection cfg) {
        String itemStr = cfg.getString("item", "");
        return new DropActivator(base, itemStr);
    }

    @Override
    public boolean activate(Storage event) {
        DropStorage de = (DropStorage) event;
        return checkItem(de.getItem());
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("item", this.itemStr);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.DROP;
    }

    private boolean checkItem(ItemStack item) {
        if (this.itemStr.isEmpty()) return true;
        return ItemUtil.compareItemStr(item, this.itemStr, true);
    }
}
