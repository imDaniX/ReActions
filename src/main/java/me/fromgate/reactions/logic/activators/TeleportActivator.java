package me.fromgate.reactions.logic.activators;

import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.logic.storages.TeleportStorage;
import me.fromgate.reactions.util.Util;
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
        TeleportCause cause = getCauseByName(param.getParam("cause"));
        String worldTo = param.getParam("world");
        return new TeleportActivator(base, cause, worldTo);
    }

    public static TeleportActivator load(ActivatorBase base, ConfigurationSection cfg) {
        TeleportCause cause = getCauseByName(cfg.getString("cause"));
        String worldTo = cfg.getString("world");
        return new TeleportActivator(base, cause, worldTo);
    }

    private static TeleportCause getCauseByName(String name) {
        if (Util.isStringEmpty(name)) return null;
        name = name.toUpperCase();
        for (TeleportCause cause : TeleportCause.values())
            if (cause.name().equals(name)) return cause;
        return null;
    }

    @Override
    public boolean activate(Storage storage) {
        TeleportStorage tpStorage = (TeleportStorage) storage;
        if (cause != null && tpStorage.getCause() != cause) return false;
        return worldTo == null || tpStorage.getWorldTo().equalsIgnoreCase(worldTo);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.TELEPORT;
    }

    @Override
    public void save(ConfigurationSection cfg) {
        if (cause != null) cfg.set("cause", cause.name());
        if (worldTo != null) cfg.set("world", worldTo);
    }
}
