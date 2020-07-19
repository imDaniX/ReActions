package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class SneakStorage extends Storage {
    @Getter
    private final boolean sneaking;

    public SneakStorage(Player player, boolean sneaking) {
        super(player, ActivatorType.SNEAK);
        this.sneaking = sneaking;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("sneak", Boolean.toString(sneaking));
    }
}