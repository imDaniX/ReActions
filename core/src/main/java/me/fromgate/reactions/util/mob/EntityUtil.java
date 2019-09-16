package me.fromgate.reactions.util.mob;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;
import java.util.HashSet;

/**
 * Some helpful methods related to entities to minify size of code
 */
public class EntityUtil {

	/**
	 * Get maximal health of entity
	 * @param entity Entity to check
	 * @return Maximal health of entity
	 */
	public static double getMaxHealth(LivingEntity entity) {
		return entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
	}

	/**
	 * Get {@link LivingEntity} from {@link ProjectileSource}
	 * @param source Original source
	 * @return LivingEntity or null if shooter was a block
	 */
	public static LivingEntity getEntityFromProjectile(ProjectileSource source) {
		if(source instanceof BlockProjectileSource)
			return null;
		return (LivingEntity)source;
	}

	/**
	 * Get EntityType by it's name
	 * @param type Name of EntityType
	 * @return EntityType or null if there's no types with this name
	 */
	public static EntityType getEntityByName(String type) {
		type = type.toUpperCase();
		for(EntityType eType : EntityType.values())
			if(eType.name().equals(type)) return eType;
		return null;
	}

	/**
	 * Get all entities inside cuboid
	 * @param l1 Point of cuboid
	 * @param l2 Point of cuboid
	 * @return List of entities
	 */
	public static Collection<Entity> getEntities(Location l1, Location l2) {
		Collection<Entity> entities = new HashSet<>();
		if (!l1.getWorld().equals(l2.getWorld())) return entities;
		int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
		int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
		int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
		int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
		int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
		int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
		int chX1 = x1 >> 4;
		int chX2 = x2 >> 4;
		int chZ1 = z1 >> 4;
		int chZ2 = z2 >> 4;
		for (int x = chX1; x <= chX2; x++) {
			for (int z = chZ1; z <= chZ2; z++) {
				for (Entity e : l1.getWorld().getChunkAt(x, z).getEntities()) {
					double ex = e.getLocation().getX();
					double ey = e.getLocation().getY();
					double ez = e.getLocation().getZ();
					if ((x1 <= ex) && (ex <= x2) && (y1 <= ey) && (ey <= y2) && (z1 <= ez) && (ez <= z2)) {
						entities.add(e);
					}
				}
			}
		}
		return entities;
	}

	public static LivingEntity getDamagerEntity(EntityDamageByEntityEvent event) {
		if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
			Projectile prj = (Projectile) event.getDamager();
			return getEntityFromProjectile(prj.getShooter());
		} else if (event.getCause() == EntityDamageEvent.DamageCause.MAGIC) {
			Entity entityDamager = event.getDamager();
			LivingEntity shooterEntity = null;
			if (entityDamager instanceof ThrownPotion)
				shooterEntity = getEntityFromProjectile(((ThrownPotion) entityDamager).getShooter());
			return shooterEntity;
		} else if (event.getDamager() instanceof LivingEntity)
			return (LivingEntity) event.getDamager();
		return null;
	}

	public static String getMobName(LivingEntity mob) {
		return mob.getCustomName() == null ? "" : mob.getCustomName();
	}

	public static LivingEntity getAnyKiller(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
			if (evdmg.getDamager() instanceof LivingEntity) return (LivingEntity) evdmg.getDamager();
			if (evdmg.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
				Projectile prj = (Projectile) evdmg.getDamager();
				return getEntityFromProjectile(prj.getShooter());
			}
		}
		return null;
	}

	public static Player getKiller(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
			if (evdmg.getDamager().getType() == EntityType.PLAYER) return (Player) evdmg.getDamager();
			if (evdmg.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
				Projectile prj = (Projectile) evdmg.getDamager();
				LivingEntity shooterEntity = getEntityFromProjectile(prj.getShooter());
				if (shooterEntity == null) return null;
				if (shooterEntity instanceof Player) return (Player) shooterEntity;
			}
		}
		return null;
	}
}
