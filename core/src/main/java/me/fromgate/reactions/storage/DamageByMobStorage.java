package me.fromgate.reactions.storage;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by MaxDikiy on 2017-06-25.
 */
public class DamageByMobStorage extends RAStorage {
	@Getter private final Entity damager;
	@Getter private final DamageCause cause;
	@Getter @Setter private double damage;


	public DamageByMobStorage(Player player, Entity damager, double damage, DamageCause cause) {
		super(player, ActivatorType.DAMAGE_BY_MOB);
		this.damager = damager;
		this.damage = damage;
		this.cause = cause;
	}
}
