package me.fromgate.reactions.storage;

import lombok.Getter;
import lombok.Setter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GameModeStorage extends RAStorage {
	@Getter @Setter private GameMode gameMode;

	public GameModeStorage(Player player, GameMode gameMode) {
		super(player, ActivatorType.GAMEMODE);
		this.gameMode = gameMode;
	}
}
