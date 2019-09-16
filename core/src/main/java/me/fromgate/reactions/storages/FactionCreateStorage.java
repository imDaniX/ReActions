package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

import java.util.Map;

public class FactionCreateStorage extends Storage {
	@Getter private final String faction;

	public FactionCreateStorage(String factionName, Player player) {
		super(player, ActivatorType.FCT_CREATE);
		this.faction = factionName;
	}

	@Override
	void defaultVariables(Map<String, String> tempVars) {
		tempVars.put("faction", faction);
	}
}
