package me.fromgate.reactions.storages;

import lombok.Getter;
import me.fromgate.reactions.activators.ActivatorType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportStorage extends Storage {
	@Getter private final TeleportCause cause;
	@Getter private final String worldTo;

	public TeleportStorage(Player player, TeleportCause cause, String worldTo) {
		super(player, ActivatorType.TELEPORT);
		this.cause = cause;
		this.worldTo = worldTo;
	}
}
