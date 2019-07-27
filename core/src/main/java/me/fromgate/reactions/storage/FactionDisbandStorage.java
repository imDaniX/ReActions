package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

public class FactionDisbandStorage extends RAStorage {
	@Getter private final String faction;

	public FactionDisbandStorage(String factionName, Player player) {
		super(player, ActivatorType.FCT_DISBAND);
		this.faction = factionName;
	}
}
