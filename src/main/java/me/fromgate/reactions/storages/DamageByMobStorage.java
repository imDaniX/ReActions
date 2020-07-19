package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.DoubleValue;
import me.fromgate.reactions.util.location.LocationUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-06-25.
 */
public class DamageByMobStorage extends Storage {
	@Getter private final Entity damager;
	@Getter private final DamageCause cause;
	@Getter private final double damage;


	public DamageByMobStorage(Player player, Entity damager, double damage, DamageCause cause) {
		super(player, ActivatorType.DAMAGE_BY_MOB);
		this.damager = damager;
		this.damage = damage;
		this.cause = cause;
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		tempVars.put("damagerlocation", (damager != null) ? LocationUtil.locationToString(damager.getLocation()) : "");
		tempVars.put("damagertype", (damager != null) ? damager.getType().name() : "");
		tempVars.put("entitytype", damager.getType().name());
		Player player = damager instanceof Player ? (Player) damager : null;
		String damagerName = (player == null) ? ((damager != null) ? damager.getCustomName() : "") : player.getName();
		tempVars.put("damagername", damagerName != null && !damagerName.isEmpty() ? damagerName : ((damager != null) ? damager.getType().name() : ""));
		tempVars.put("cause", cause.name());
	}

	@Override
	void defaultChangeables(Map<String, DataValue> changeables) {
		changeables.put(CANCEL_EVENT, new BooleanValue(false));
		changeables.put(DamageStorage.DAMAGE, new DoubleValue(damage));
	}
}
