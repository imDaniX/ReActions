package me.fromgate.reactions.logic.storages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.LocationValue;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Map;

@Getter
@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class TeleportStorage extends Storage {
    public static final String LOCATION_TO = "loc_to";

    TeleportCause cause;
    String worldTo;
    Location to;

    public TeleportStorage(Player player, TeleportCause cause, Location to) {
        super(player, ActivatorType.TELEPORT);
        this.cause = cause;
        this.worldTo = to.getWorld().getName();
        this.to = to;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return new MapBuilder<String, DataValue>()
                .put(CANCEL_EVENT, new BooleanValue(false))
                .put(LOCATION_TO, new LocationValue(to))
                .build();
    }
}
