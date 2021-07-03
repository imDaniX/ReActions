package me.fromgate.reactions.module.defaults.storages;

import lombok.Getter;
import me.fromgate.reactions.externals.worldedit.WeSelection;
import me.fromgate.reactions.logic.activators.Activator;
import me.fromgate.reactions.logic.activators.Storage;
import me.fromgate.reactions.module.defaults.activators.WeSelectionActivator;
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
        super(player);
        this.selection = weSelection;
    }

    @Override
    public Class<? extends Activator> getType() {
        return WeSelectionActivator.class;
    }

    @Override
    protected Map<String, String> prepareVariables() {
        Map<String, String> tempVars = new HashMap<>();
        if (selection.isValid()) {
            tempVars.put("seltype", selection.selType());
            World world = selection.world();
            tempVars.put("world", (world != null) ? world.getName() : "");
            tempVars.put("selblocks", Integer.toString(selection.area()));
            tempVars.put("region", selection.region());
        }
        return tempVars;
    }

    @Override
    protected Map<String, DataValue> prepareChangeables() {
        return MapBuilder.single(CANCEL_EVENT, new BooleanValue(false));
    }
}
