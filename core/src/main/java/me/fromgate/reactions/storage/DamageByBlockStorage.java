package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
public class DamageByBlockStorage extends RAStorage {
	private final Block blockDamager;
	private double damage;
	private final DamageCause cause;


	public DamageByBlockStorage(Player player, Block blockDamager, double damage, DamageCause cause) {
		super(player, ActivatorType.DAMAGE_BY_BLOCK);
		this.blockDamager = blockDamager;
		this.damage = damage;
		this.cause = cause;
	}

	public Block getBlockDamager() {
		return this.blockDamager;
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

	public Location getBlockBreakLocation() {
		return blockDamager.getLocation();
	}
}
