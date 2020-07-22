/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class WeChangeStorage extends Storage {
    @Getter
    private final Location location;
    @Getter
    private final Material blockType;

    public WeChangeStorage(Player player, Location location, Material blockType) {
        super(player, ActivatorType.WE_CHANGE);
        this.location = location;
        this.blockType = blockType;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("blocktype", blockType.name());
        tempVars.put("blocklocation", LocationUtils.locationToString(location));
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(Storage.CANCEL_EVENT, new BooleanValue(false));
    }
}
