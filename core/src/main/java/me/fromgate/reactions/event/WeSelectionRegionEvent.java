package me.fromgate.reactions.event;

import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.externals.worldedit.WeSelection;
import org.bukkit.entity.Player;

public class WeSelectionRegionEvent extends RAEvent {

	private WeSelection selection;

	public WeSelectionRegionEvent(Player player, WeSelection weSelection) {
		super(player, ActivatorType.WE_SELECTION_REGION);
		this.selection = weSelection;
	}

	public WeSelection getSelection() {
		return this.selection;
	}

}
