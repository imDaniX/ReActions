package me.fromgate.reactions.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by MaxDikiy on 2017-06-25.
 */
public class DamageByMobEvent extends RAEvent {
	private Entity damager;
	private double damage;
	private DamageCause cause;


	public DamageByMobEvent(Player player, Entity damager, double damage, DamageCause cause) {
		super(player);
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
