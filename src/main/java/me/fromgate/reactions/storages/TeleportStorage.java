package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.LocationValue;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Map;

public class TeleportStorage extends Storage {
    public static final String LOCATION_TO = "loc_to";

    @Getter
    private final TeleportCause cause;
    @Getter
    private final String worldTo;
    private final Location to;

    public TeleportStorage(Player player, TeleportCause cause, Location to) {
        super(player, ActivatorType.TELEPORT);
        this.cause = cause;
        this.worldTo = to.getWorld().getName();
        this.to = to;
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
        changeables.put(LOCATION_TO, new LocationValue(to));
    }
}
