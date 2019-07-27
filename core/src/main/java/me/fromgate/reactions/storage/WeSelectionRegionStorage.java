package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.externals.worldedit.WeSelection;
import org.bukkit.entity.Player;

public class WeSelectionRegionStorage extends RAStorage {
	@Getter private final WeSelection selection;

	public WeSelectionRegionStorage(Player player, WeSelection weSelection) {
		super(player, ActivatorType.WE_SELECTION_REGION);
		this.selection = weSelection;
	}
}
