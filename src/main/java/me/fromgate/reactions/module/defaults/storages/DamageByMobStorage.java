package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.DamageByMobActivator;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.DoubleValue;
import me.fromgate.reactions.util.location.LocationUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaxDikiy on 2017-06-25.
 */
@Getter
public class DamageByMobStorage extends Storage {

    private final Entity damager;
    private final DamageCause cause;
    private final double damage;

    public DamageByMobStorage(Player player, Entity damager, double damage, DamageCause cause) {
        super(player);
        this.damager = damager;
        this.damage = damage;
        this.cause = cause;
    }

    @Override
    public Class<? extends Activator> getType() {
        return DamageByMobActivator.class;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        tempVars.put("damagerlocation", LocationUtils.locationToString(damager.getLocation()));
        tempVars.put("damagertype", damager.getType().name());
        tempVars.put("entitytype", damager.getType().name());
        Player player = damager instanceof Player ? (Player) damager : null;
        String damagerName = (player == null) ? damager.getCustomName() : player.getName();
        tempVars.put("damagername", damagerName != null && !damagerName.isEmpty() ? damagerName : damager.getType().name());
        tempVars.put("cause", cause.name());
        return tempVars;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return new MapBuilder<String, DataValue>()
                .put(CANCEL_EVENT, new BooleanValue(false))
                .put(DamageStorage.DAMAGE, new DoubleValue(damage))
                .build();
    }
}
