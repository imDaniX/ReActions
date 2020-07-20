package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.ActivatorType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ProjectileHitStorage extends Storage {
    @Getter
    private final EntityType projType;
    @Getter
    private final Block block;
    @Getter
    private final BlockFace blockFace;
    @Getter
    private final Entity entity;
    @Getter
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
