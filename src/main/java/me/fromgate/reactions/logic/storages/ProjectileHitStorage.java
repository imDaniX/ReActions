package me.fromgate.reactions.logic.storages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
// TODO
@Getter
@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class ProjectileHitStorage extends Storage {

    EntityType projType;
    Block block;
    BlockFace blockFace;
    Entity entity;
    boolean entityHit;

    public ProjectileHitStorage(Player player, EntityType projType, Block block, BlockFace face, Entity entity) {
        super(player, ActivatorType.EXEC);
        this.projType = projType;
        this.entityHit = entity != null;
        this.block = block;
        this.blockFace = face;
        this.entity = entity;
    }
}
