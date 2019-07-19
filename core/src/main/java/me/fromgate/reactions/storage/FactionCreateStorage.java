package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

public class FactionCreateStorage extends RAStorage {
	private String faction;

	public FactionCreateStorage(String factionName, Player player) {
		super(player, ActivatorType.FCT_CREATE);
		this.faction = factionName;
	}

	public String getFaction() {
		return this.faction;
	}

}
