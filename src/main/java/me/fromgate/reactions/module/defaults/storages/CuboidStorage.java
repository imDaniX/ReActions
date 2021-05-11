package me.fromgate.reactions.module.defaults.storages;

import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.OldActivatorType;
import org.bukkit.entity.Player;

public class CuboidStorage extends Storage {
    public CuboidStorage(Player player) {
        super(player, OldActivatorType.CUBOID);
    }
}
