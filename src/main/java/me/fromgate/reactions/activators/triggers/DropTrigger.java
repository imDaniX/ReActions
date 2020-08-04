package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.DropStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-05-01.
 */
public class DropTrigger extends Trigger {

    private final String itemStr;

    private DropTrigger(ActivatorBase base, String itemStr) {
        super(base);
        this.itemStr = itemStr;
    }

    public static DropTrigger create(ActivatorBase base, Parameters param) {
        String itemStr = param.getString("item", param.toString());
        return new DropTrigger(base, itemStr);
    }

    public static DropTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        String itemStr = cfg.getString("item", "");
        return new DropTrigger(base, itemStr);
    }

    @Override
    public boolean proceed(Storage event) {
        DropStorage de = (DropStorage) event;
        return checkItem(de.getItem());
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
        cfg.set("item", this.itemStr);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.DROP;
    }

    private boolean checkItem(ItemStack item) {
        if (this.itemStr.isEmpty()) return true;
        return ItemUtils.compareItemStr(item, this.itemStr, true);
    }
}
