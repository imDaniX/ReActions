package me.fromgate.reactions.module.defaults.storages;

import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.CuboidActivator;
import org.bukkit.entity.Player;

public class CuboidStorage extends Storage {
    public CuboidStorage(Player player) {
        super(player);
    }

    @Override
    public Class<? extends Activator> getType() {
        return CuboidActivator.class;
    }
}
