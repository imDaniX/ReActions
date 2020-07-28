package me.fromgate.reactions.logic.storages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import me.fromgate.reactions.util.item.ItemUtils;
import me.fromgate.reactions.util.location.LocationUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
@Getter
@FieldDefaults(makeFinal=true,level=AccessLevel.PRIVATE)
public class BlockBreakStorage extends Storage {
    public static final String DO_DROP = "is_drop";

    Block block;
    boolean dropItems;

    public BlockBreakStorage(Player p, Block block, boolean dropItems) {
        super(p, ActivatorType.BLOCK_BREAK);
        this.block = block;
        this.dropItems = dropItems;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        tempVars.put("blocklocation", LocationUtils.locationToString(block.getLocation()));
        tempVars.put("blocktype", block.getType().name());
        tempVars.put("block", ItemUtils.itemFromBlock(block).toString());
        return tempVars;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return new MapBuilder<String, DataValue>(CANCEL_EVENT, new BooleanValue(false))
                .put(DO_DROP, new BooleanValue(dropItems))
                .build();
    }

}
