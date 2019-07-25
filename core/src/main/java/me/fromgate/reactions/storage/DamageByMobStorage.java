package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by MaxDikiy on 2017-06-25.
 */
public class DamageByMobStorage extends RAStorage {
	private final Entity damager;
	private double damage;
	private final DamageCause cause;


	public DamageByMobStorage(Player player, Entity damager, double damage, DamageCause cause) {
		super(player, ActivatorType.DAMAGE_BY_MOB);
		this.damager = damager;
		this.damage = damage;
		this.cause = cause;
	}

	public Entity getDamager() {
		return this.damager;
	}

	public double getDamage() {
		return this.damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public DamageCause getCause() {
		return this.cause;
	}

}
