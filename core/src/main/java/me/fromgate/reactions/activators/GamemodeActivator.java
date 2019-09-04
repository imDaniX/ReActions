package me.fromgate.reactions.activators;

import me.fromgate.reactions.Variables;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.storages.GameModeStorage;
import me.fromgate.reactions.storages.Storage;
import me.fromgate.reactions.util.parameter.Param;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GamemodeActivator extends Activator {
	private final GameMode gameMode;

	public GamemodeActivator(ActivatorBase base, GameMode gameMode) {
		super(base);
		this.gameMode = gameMode;
	}

	@Override
	public boolean activate(Storage event) {
		GameModeStorage e = (GameModeStorage) event;
		if (!gameModeCheck(e.getGameMode())) return false;
		Variables.setTempVar("gamemode", e.getGameMode().toString());
		return Actions.executeActivator(e.getPlayer(), getBase());
	}

	private boolean gameModeCheck(GameMode gm) {
		if (gameMode == null) return true;
		return gm == gameMode;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("gamemode", gameMode == null ? "ANY": gameMode.name());
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.GAMEMODE;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" (");
		sb.append("gamemode:").append(gameMode == null ? "ANY": gameMode.name());
		sb.append(")");
		return sb.toString();
	}

	private static GameMode getGameModeByName(String name) {
		name = name.toUpperCase();
		for(GameMode gm : GameMode.values())
			if(gm.name().equals(name)) return gm;
		return null;
	}

	public static GamemodeActivator create(ActivatorBase base, Param param) {
		GameMode gameMode = getGameModeByName(param.getParam("gamemode", "ANY"));
		return new GamemodeActivator(base, gameMode);
	}

	public static GamemodeActivator load(ActivatorBase base, ConfigurationSection cfg) {
		GameMode gameMode = getGameModeByName(cfg.getString("gamemode", "ANY"));
		return new GamemodeActivator(base, gameMode);
	}
}
