package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ProjectileHitStorage extends RAStorage {
	private final EntityType projType;
	private final Block block;
	private final BlockFace face;
	private final Entity entity;
	private final boolean entityHit;

	public ProjectileHitStorage(Player player, EntityType projType, Block block, BlockFace face, Entity entity) {
		super(player, ActivatorType.PROJECTILE_HIT);
		this.projType = projType;
		this.entityHit = entity != null;
		this.block = block;
		this.face = face;
		this.entity = entity;
	}

	public EntityType getProjType() {
		return projType;
	}

	public Block getBlock() {
		return block;
	}

	public BlockFace getBlockFace() {
		return face;
	}

	public Entity getEntity() {
		return entity;
	}

	public boolean isEntityHit() {
		return entityHit;
	}

	public Location getHitLocation() {
		return entityHit ? entity.getLocation() : block.getLocation();
	}
}
