package me.fromgate.reactions.event;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

public class FactionDisbandEvent extends RAEvent {
	private String faction;

	public FactionDisbandEvent(String factionName, Player player) {
		super(player, ActivatorType.FCT_DISBAND);
		this.faction = factionName;
	}

	public String getFaction() {
		return this.faction;
	}

}
