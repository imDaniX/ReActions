package me.fromgate.reactions.logic.storages;

import me.fromgate.reactions.logic.ActivatorType;
import org.bukkit.entity.Player;

public class CuboidStorage extends Storage {
    public CuboidStorage(Player player) {
        super(player, ActivatorType.CUBOID);
    }
}
