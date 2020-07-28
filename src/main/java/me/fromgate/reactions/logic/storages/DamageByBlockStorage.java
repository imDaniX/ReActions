package me.fromgate.reactions.logic.storages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.data.DoubleValue;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.location.LocationUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Map;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
@Getter
@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class DamageByBlockStorage extends Storage {

    Block blockDamager;
    DamageCause cause;
    double damage;


    public DamageByBlockStorage(Player player, Block blockDamager, double damage, DamageCause cause) {
        super(player, ActivatorType.DAMAGE_BY_BLOCK);
        this.blockDamager = blockDamager;
        this.damage = damage;
        this.cause = cause;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        tempVars.put("blocklocation", LocationUtils.locationToString(blockDamager.getLocation()));
        tempVars.put("blocktype", blockDamager.getType().name());
        tempVars.put("block", ItemUtils.itemFromBlock(blockDamager).toString());
        tempVars.put("cause", cause.name());
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(CANCEL_EVENT, new BooleanValue(false));
        changeables.put(DamageStorage.DAMAGE, new DoubleValue(damage));
    }
}
