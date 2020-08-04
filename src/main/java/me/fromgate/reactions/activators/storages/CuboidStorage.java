package me.fromgate.reactions.activators.storages;

import me.fromgate.reactions.activators.triggers.ActivatorType;
import org.bukkit.entity.Player;

public class CuboidStorage extends Storage {
    public CuboidStorage(Player player) {
        super(player, ActivatorType.CUBOID);
    }
}
