/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.activators.storages.WeChangeStorage;
import me.fromgate.reactions.externals.worldguard.RaWorldGuard;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Locale;

public class WeChangeTrigger extends Trigger {

    private final Material blockType;
    private final String region;

    private WeChangeTrigger(ActivatorBase base, Material blockType, String region) {
        super(base);
        this.blockType = blockType;
        this.region = region;
    }

    public static WeChangeTrigger create(ActivatorBase base, Parameters param) {
        Material blockType = ItemUtils.getMaterial(param.getString("blocktype"));
        String region = param.getString("region", "");
        return new WeChangeTrigger(base, blockType, region);
    }

    public static WeChangeTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        Material blockType = ItemUtils.getMaterial(cfg.getString("block-type"));
        String region = cfg.getString("region", "");
        return new WeChangeTrigger(base, blockType, region);
    }

    @Override
    public boolean proceed(Storage event) {
        WeChangeStorage e = (WeChangeStorage) event;
        if (!checkBlockType(e.getBlockType())) return false;
        return region.isEmpty() || RaWorldGuard.isLocationInRegion(e.getLocation(), region);
    }

    private boolean checkBlockType(Material check) {
        return blockType == null || blockType == check;
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
        if (blockType != null) cfg.set("block-type", this.blockType.name());
        cfg.set("region", this.region);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.WE_CHANGE;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("block-type:").append(blockType != null ? blockType : "ANY");
        sb.append(" region:").append(region.isEmpty() ? "-" : region.toUpperCase(Locale.ENGLISH));
        sb.append(")");
        return sb.toString();
    }
}
