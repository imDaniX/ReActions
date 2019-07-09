package me.fromgate.reactions.event;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

public class FactionCreateEvent extends RAEvent {
	private String faction;

	public FactionCreateEvent(String factionName, Player player) {
		super(player, ActivatorType.FCT_CREATE);
		this.faction = factionName;
	}

	public String getFaction() {
		return this.faction;
	}

}
