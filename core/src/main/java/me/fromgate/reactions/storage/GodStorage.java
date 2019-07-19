package me.fromgate.reactions.storage;

import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GodStorage extends RAStorage {
	private boolean god;

	public GodStorage(Player player, boolean god) {
		super(player, ActivatorType.GOD);
		this.god = god;
	}

	public boolean isGod() {
		return god;
	}

	public void setGod(boolean god) {
		this.god = god;
	}
}
