package me.fromgate.reactions.module.defaults.activators;

import me.fromgate.reactions.logic.ActivatorLogic;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.storages.GameModeStorage;
import me.fromgate.reactions.util.Utils;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GameModeActivator extends Activator {
    private final GameMode gameMode;

    private GameModeActivator(ActivatorLogic base, GameMode gameMode) {
        super(base);
        this.gameMode = gameMode;
    }

    public static GameModeActivator create(ActivatorLogic base, Parameters param) {
        GameMode gameMode = Utils.getEnum(GameMode.class, param.getString("gamemode", "ANY"));
        return new GameModeActivator(base, gameMode);
    }

    public static GameModeActivator load(ActivatorLogic base, ConfigurationSection cfg) {
        GameMode gameMode = Utils.getEnum(GameMode.class, cfg.getString("gamemode", "ANY"));
        return new GameModeActivator(base, gameMode);
    }

    @Override
    public boolean checkStorage(Storage event) {
        GameModeStorage e = (GameModeStorage) event;
        return gameModeCheck(e.getGameMode());
    }

    private boolean gameModeCheck(GameMode gm) {
        if (gameMode == null) return true;
        return gm == gameMode;
    }

    @Override
    public void saveOptions(ConfigurationSection cfg) {
        cfg.set("gamemode", gameMode == null ? "ANY" : gameMode.name());
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
