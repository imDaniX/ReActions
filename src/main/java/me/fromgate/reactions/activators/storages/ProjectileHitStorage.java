package me.fromgate.reactions.activators.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.triggers.ActivatorType;
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
        super(player, ActivatorType.EXEC);
        this.projType = projType;
        this.entityHit = entity != null;
        this.block = block;
        this.blockFace = face;
        this.entity = entity;
    }
}
