package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.GameModeEvent;
import me.fromgate.reactions.event.RAEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GamemodeActivator extends Activator {
	private GameMode gameMode;

	public GamemodeActivator(String name, String param) {
		super(name, "activators");
		Param params = new Param(param);
		this.gameMode = GameMode.valueOf(params.getParam("gamemode", "ANY").toUpperCase());
	}

	public GamemodeActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	@Override
	public boolean activate(RAEvent event) {
		if (!(event instanceof GameModeEvent)) return false;
		GameModeEvent e = (GameModeEvent) event;
		if (!gameModeCheck(e.getGameMode())) return false;
		Variables.setTempVar("gamemode", e.getGameMode().toString());
		return Actions.executeActivator(e.getPlayer(), this);
	}

	private boolean gameModeCheck(GameMode gm) {
		if (gameMode == null) return true;
		return gm.name().equals(gameMode.name());
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return false;
	}

	@Override
	public void save(ConfigurationSection cfg) {
		cfg.set("gamemode", gameMode.name());
	}

	@Override
	public void load(ConfigurationSection cfg) {
		gameMode = GameMode.valueOf(cfg.getString("gamemode", "ANY").toUpperCase());
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.GAMEMODE;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
		if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		sb.append(" (");
		sb.append("gamemode:").append(this.gameMode.name());
		sb.append(")");
		return sb.toString();
	}

}
