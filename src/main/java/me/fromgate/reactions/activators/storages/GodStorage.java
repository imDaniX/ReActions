package me.fromgate.reactions.activators.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.triggers.ActivatorType;
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
        super(player, ActivatorType.GOD);
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
