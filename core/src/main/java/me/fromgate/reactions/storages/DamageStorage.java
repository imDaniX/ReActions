package me.fromgate.reactions.storages;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
public class DamageStorage extends Storage {
	@Getter private final DamageCause cause;
	@Getter private final String source;
	@Getter @Setter private double damage;

	public DamageStorage(Player player, double damage, DamageCause cause, String source) {
		super(player, ActivatorType.DAMAGE);
		this.damage = damage;
		this.cause = cause;
		this.source = source;
	}
}
