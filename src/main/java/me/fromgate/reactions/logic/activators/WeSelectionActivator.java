package me.fromgate.reactions.logic.activators;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.fromgate.reactions.externals.worldedit.WeSelection;
import me.fromgate.reactions.logic.storages.Storage;
import me.fromgate.reactions.logic.storages.WeSelectionRegionStorage;
import me.fromgate.reactions.util.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;

@FieldDefaults(makeFinal=true,level= AccessLevel.PRIVATE)
public class WeSelectionActivator extends Activator {
    int maxBlocks;
    int minBlocks;
    String typeSelection;

    private WeSelectionActivator(ActivatorBase base, int maxBlocks, int minBlocks, String typeSelection) {
        super(base);
        this.maxBlocks = maxBlocks;
        this.minBlocks = minBlocks;
        this.typeSelection = typeSelection;
    }

    public static WeSelectionActivator create(ActivatorBase base, Parameters param) {
        int minBlocks = param.getParam("minblocks", 0);
        int maxBlocks = param.getParam("maxblocks", Integer.MAX_VALUE);
        String typeSelection = param.getParam("type", "ANY");
        return new WeSelectionActivator(base, minBlocks, maxBlocks, typeSelection);
    }

    public static WeSelectionActivator load(ActivatorBase base, ConfigurationSection cfg) {
        int minBlocks = cfg.getInt("min-blocks", 0);
        int maxBlocks = cfg.getInt("max-blocks", Integer.MAX_VALUE);
        String typeSelection = cfg.getString("type", "ANY");
        return new WeSelectionActivator(base, minBlocks, maxBlocks, typeSelection);
    }

    @Override
    public boolean activate(Storage event) {
        WeSelectionRegionStorage e = (WeSelectionRegionStorage) event;
        WeSelection selection = e.getSelection();
        if (!selection.isValid()) return false;
        int selectionBlocks = selection.getArea();
        if (selectionBlocks < minBlocks) return false;
        if (selectionBlocks > maxBlocks && maxBlocks != 0) return false;
        String selType = selection.getSelType();
        if (!checkTypeSelection(selType)) return false;
        String region = selection.getRegion();
        return region != null && !region.isEmpty();
    }

    private boolean checkTypeSelection(String selType) {
        return typeSelection.isEmpty() || typeSelection.equalsIgnoreCase("ANY") || typeSelection.equalsIgnoreCase(selType);
    }

    @Override
    public void save(ConfigurationSection cfg) {
        cfg.set("min-blocks", this.minBlocks);
        cfg.set("max-blocks", this.maxBlocks);
        cfg.set("type", this.typeSelection);
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.WE_SELECTION_REGION;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" (");
        sb.append("minblocks:").append(minBlocks);
        sb.append("; maxblocks:").append(maxBlocks);
        sb.append("; type:").append(typeSelection);
        sb.append(")");
        return sb.toString();
    }
}
