package me.fromgate.reactions.storages;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GodStorage extends Storage {
    @Getter
    @Setter
    private boolean god;

    public GodStorage(Player player, boolean god) {
        super(player, ActivatorType.GOD);
        this.god = god;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("god", Boolean.toString(god));
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
    }
}
