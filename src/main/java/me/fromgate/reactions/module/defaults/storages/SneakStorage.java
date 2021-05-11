package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.OldActivatorType;
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
        super(player, OldActivatorType.SNEAK);
        this.sneaking = sneaking;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        return MapBuilder.single("sneak", Boolean.toString(sneaking));
    }
}