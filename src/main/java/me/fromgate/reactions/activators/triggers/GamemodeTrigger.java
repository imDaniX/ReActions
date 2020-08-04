package me.fromgate.reactions.activators.triggers;

import me.fromgate.reactions.activators.storages.GameModeStorage;
import me.fromgate.reactions.activators.storages.Storage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GamemodeTrigger extends Trigger {
    private final GameMode gameMode;

    private GamemodeTrigger(ActivatorBase base, GameMode gameMode) {
        super(base);
        this.gameMode = gameMode;
    }

    public static GamemodeTrigger create(ActivatorBase base, Parameters param) {
        GameMode gameMode = Utils.getEnum(GameMode.class, param.getString("gamemode", "ANY"));
        return new GamemodeTrigger(base, gameMode);
    }

    public static GamemodeTrigger load(ActivatorBase base, ConfigurationSection cfg) {
        GameMode gameMode = Utils.getEnum(GameMode.class, cfg.getString("gamemode", "ANY"));
        return new GamemodeTrigger(base, gameMode);
    }

    @Override
    public boolean proceed(Storage event) {
        GameModeStorage e = (GameModeStorage) event;
        return gameModeCheck(e.getGameMode());
    }

    private boolean gameModeCheck(GameMode gm) {
        if (gameMode == null) return true;
        return gm == gameMode;
    }

    @Override
    public void saveTrigger(ConfigurationSection cfg) {
        cfg.set("gamemode", gameMode == null ? "ANY" : gameMode.name());
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.GAMEMODE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("gamemode:").append(gameMode == null ? "ANY" : gameMode.name());
        sb.append(")");
        return sb.toString();
    }
}
