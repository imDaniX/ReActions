package me.fromgate.reactions.storage;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
public class DamageByBlockStorage extends RAStorage {
	@Getter private final Block blockDamager;
	@Getter private final DamageCause cause;
	@Getter @Setter private double damage;


	public DamageByBlockStorage(Player player, Block blockDamager, double damage, DamageCause cause) {
		super(player, ActivatorType.DAMAGE_BY_BLOCK);
		this.blockDamager = blockDamager;
		this.damage = damage;
		this.cause = cause;
	}

	public Location getBlockBreakLocation() {
		return blockDamager.getLocation();
	}
}
