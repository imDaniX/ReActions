package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
@Getter
public class GameModeStorage extends Storage {

    private GameMode gameMode;

    public GameModeStorage(Player player, GameMode gameMode) {
        super(player, ActivatorType.GAMEMODE);
        this.gameMode = gameMode;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("gamemode", gameMode.name());
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
    }
}
