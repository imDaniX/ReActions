package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.logic.storages.TeleportStorage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportActivator extends Activator {
    private final TeleportCause cause;
    private final String worldTo;

    private TeleportActivator(ActivatorBase base, TeleportCause cause, String worldTo) {
        super(base);
        this.cause = cause;
        this.worldTo = worldTo;
    }

    public static TeleportActivator create(ActivatorBase base, Parameters param) {
        TeleportCause cause = Utils.getEnum(TeleportCause.class, param.getString("cause"));
        String worldTo = param.getString("world");
        return new TeleportActivator(base, cause, worldTo);
    }

    public static TeleportActivator load(ActivatorBase base, ConfigurationSection cfg) {
        TeleportCause cause = Utils.getEnum(TeleportCause.class, cfg.getString("cause"));
        String worldTo = cfg.getString("world");
        return new TeleportActivator(base, cause, worldTo);
    }

    @Override
    public boolean proceed(Storage storage) {
        TeleportStorage tpStorage = (TeleportStorage) storage;
        if (cause != null && tpStorage.getCause() != cause) return false;
        return worldTo == null || tpStorage.getWorldTo().equalsIgnoreCase(worldTo);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.TELEPORT;
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        if (cause != null) cfg.set("cause", cause.name());
        if (worldTo != null) cfg.set("world", worldTo);
    }
}
