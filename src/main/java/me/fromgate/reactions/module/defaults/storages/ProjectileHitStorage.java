package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.ProjectileHitActivator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

// TODO
@Getter
public class ProjectileHitStorage extends Storage {

    private final EntityType projType;
    private final Block block;
    private final BlockFace blockFace;
    private final Entity entity;
    private final boolean entityHit;

    public ProjectileHitStorage(Player player, EntityType projType, Block block, BlockFace face, Entity entity) {
        super(player);
        this.projType = projType;
        this.entityHit = entity != null;
        this.block = block;
        this.blockFace = face;
        this.entity = entity;
    }

    @Override
    public Class<? extends Activator> getType() {
        return ProjectileHitActivator.class;
    }
}
