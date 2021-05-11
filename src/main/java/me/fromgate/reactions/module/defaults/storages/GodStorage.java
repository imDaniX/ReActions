package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.OldActivatorType;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
@Getter
public class GodStorage extends Storage {

    boolean god;

    public GodStorage(Player player, boolean god) {
        super(player, OldActivatorType.GOD);
        this.god = god;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        return MapBuilder.single("god", Boolean.toString(god));
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return MapBuilder.single(CANCEL_EVENT, new BooleanValue(false));
    }
}
