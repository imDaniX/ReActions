package me.fromgate.reactions.logic.storages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.DoubleValue;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
@Getter
@FieldDefaults(makeFinal=true,level=AccessLevel.PRIVATE)
public class DamageStorage extends Storage {
    public static final String DAMAGE = "damage";

    DamageCause cause;
    String source;
    double damage;

    public DamageStorage(Player player, double damage, DamageCause cause, String source) {
        super(player, ActivatorType.DAMAGE);
        this.damage = damage;
        this.cause = cause;
        this.source = source;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("damage", Double.toString(damage));
        tempVars.put("cause", cause.name());
        tempVars.put("source", source);
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
        changeables.put(DAMAGE, new DoubleValue(damage));
    }
}
