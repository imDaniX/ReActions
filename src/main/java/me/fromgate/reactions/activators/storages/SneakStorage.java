package me.fromgate.reactions.activators.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.triggers.ActivatorType;
import me.fromgate.reactions.util.collections.MapBuilder;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
@Getter
public class SneakStorage extends Storage {

    private final boolean sneaking;

    public SneakStorage(Player player, boolean sneaking) {
        super(player, ActivatorType.SNEAK);
        this.sneaking = sneaking;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        return MapBuilder.single("sneak", Boolean.toString(sneaking));
    }
}