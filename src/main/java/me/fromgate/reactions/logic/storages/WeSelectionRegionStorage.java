package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.logic.triggers.ActivatorType;
import me.fromgate.reactions.externals.worldedit.WeSelection;
import me.fromgate.reactions.util.collections.MapBuilder;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class WeSelectionRegionStorage extends Storage {
    private final WeSelection selection;

    public WeSelectionRegionStorage(Player player, WeSelection weSelection) {
        super(player, ActivatorType.WE_SELECTION_REGION);
        this.selection = weSelection;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        if (selection.isValid()) {
            tempVars.put("seltype", selection.getSelType());
            World world = selection.getWorld();
            tempVars.put("world", (world != null) ? world.getName() : "");
            tempVars.put("selblocks", Integer.toString(selection.getArea()));
            tempVars.put("region", selection.getRegion());
        }
        return tempVars;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return MapBuilder.single(CANCEL_EVENT, new BooleanValue(false));
    }
}
