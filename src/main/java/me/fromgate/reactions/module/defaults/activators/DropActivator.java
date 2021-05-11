package me.fromgate.reactions.module.defaults.activators;

import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.ActivatorLogic;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.storages.DropStorage;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-05-01.
 */
public class DropActivator extends Activator {

    private final String itemStr;

    private DropActivator(ActivatorLogic base, String itemStr) {
        super(base);
        this.itemStr = itemStr;
    }

    public static DropActivator create(ActivatorLogic base, Parameters param) {
        String itemStr = param.getString("item", param.toString());
        return new DropActivator(base, itemStr);
    }

    public static DropActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        String itemStr = cfg.getString("item", "");
        return new DropActivator(base, itemStr);
    }

    @Override
    public boolean check(Storage event) {
        DropStorage de = (DropStorage) event;
        return checkItem(de.getItem());
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("item", itemStr);
    }

    @Override
    public OldActivatorType getType() {
        return OldActivatorType.DROP;
    }

    private boolean checkItem(ItemStack item) {
        if (this.itemStr.isEmpty()) return true;
        return ItemUtils.compareItemStr(item, this.itemStr, true);
    }
}
