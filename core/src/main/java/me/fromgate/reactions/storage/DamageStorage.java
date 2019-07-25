package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
public class DamageStorage extends RAStorage {
	private double damage;
	private final DamageCause cause;
	public final String source;

	public DamageStorage(Player player, double damage, DamageCause cause, String source) {
		super(player, ActivatorType.DAMAGE);
		this.damage = damage;
		this.cause = cause;
		this.source = source;
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

	public String getSource() {
		return this.source;
	}

}
