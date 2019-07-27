package me.fromgate.reactions.storage;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

public class FactionCreateStorage extends RAStorage {
	@Getter private final String faction;

	public FactionCreateStorage(String factionName, Player player) {
		super(player, ActivatorType.FCT_CREATE);
		this.faction = factionName;
	}
}
