package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

public class FactionDisbandStorage extends RAStorage {
	private final String faction;

	public FactionDisbandStorage(String factionName, Player player) {
		super(player, ActivatorType.FCT_DISBAND);
		this.faction = factionName;
	}

	public String getFaction() {
		return this.faction;
	}

}
