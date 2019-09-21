package me.fromgate.reactions.activators;

import me.fromgate.reactions.externals.worldedit.WeSelection;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.storages.WeSelectionRegionStorage;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.configuration.ConfigurationSection;

public class WeSelectionActivator extends Activator {
	private final int maxBlocks;
	private final int minBlocks;
	private final String typeSelection;

	private WeSelectionActivator(ActivatorBase base, int maxBlocks, int minBlocks, String typeSelection) {
		super(base);
		this.maxBlocks = maxBlocks;
		this.minBlocks = minBlocks;
		this.typeSelection = typeSelection;
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
		if (region == null || region.isEmpty()) return false;
		return true;
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

	public static WeSelectionActivator create(ActivatorBase base, Param param) {
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
}
