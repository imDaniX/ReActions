package me.fromgate.reactions.util.mob;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Some helpful methods to minify size of code
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
	 * Get {@link LivingEntity} from {@link ProjectileSource}
	 * @param prj Original source's projectile
	 * @return LivingEntity or null if shooter was a block
	 */
	public static LivingEntity getEntityFromProjectile(Projectile prj) {
		return getEntityFromProjectile(prj.getShooter());
	}
}
