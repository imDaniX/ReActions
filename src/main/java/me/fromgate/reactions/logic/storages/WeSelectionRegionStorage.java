package me.fromgate.reactions.logic.storages;

import lombok.Getter;
import me.fromgate.reactions.externals.worldedit.WeSelection;
import me.fromgate.reactions.logic.activators.ActivatorType;
import me.fromgate.reactions.util.data.BooleanValue;
import me.fromgate.reactions.util.data.DataValue;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;

public class WeSelectionRegionStorage extends Storage {
    @Getter
    private final WeSelection selection;

    public WeSelectionRegionStorage(Player player, WeSelection weSelection) {
        super(player, ActivatorType.WE_SELECTION_REGION);
        this.selection = weSelection;
    }

    @Override
    void defaultVariables(Map<String, String> tempVars) {
        if (selection.isValid()) {
            tempVars.put("seltype", selection.getSelType());
            World world = selection.getWorld();
            tempVars.put("world", (world != null) ? world.getName() : "");
            tempVars.put("selblocks", Integer.toString(selection.getArea()));
            tempVars.put("region", selection.getRegion());
        }
    }

    @Override
    void defaultChangeables(Map<String, DataValue> changeables) {
        changeables.put(Storage.CANCEL_EVENT, new BooleanValue(false));
    }
}
